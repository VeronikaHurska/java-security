package com.security.javasecurity.services;

import com.security.javasecurity.dao.CustomerDAO;
import com.security.javasecurity.models.Customer;
import com.security.javasecurity.models.dto.CustomerDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CustomerService implements UserDetailsService {

    private CustomerDAO customerDAO;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    @Autowired
    public CustomerService(CustomerDAO customerDAO, PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authenticationManager) {
        this.customerDAO = customerDAO;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customerByEmail = customerDAO.findCustomerByEmail(username);
        return customerByEmail;
    }

    public void register(CustomerDTO customerDto) {
        if (customerDto == null) {
            throw new RuntimeException();
        } else {
            Customer customer = new Customer();
            customer.setEmail(customerDto.getEmail());
            customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
            customerDAO.save(customer);
        }
    }

    public ResponseEntity<String> login(CustomerDTO customerDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(customerDTO.getEmail(),
                        customerDTO.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        System.out.println(authenticate);
        if (authenticate != null) {
            String token = Jwts.builder()
                    .setSubject(authenticate.getName())
                    .signWith(SignatureAlgorithm.HS512, "jopa".getBytes(StandardCharsets.UTF_8))
                    .compact();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            return new ResponseEntity<>("login", headers, HttpStatus.OK);
        }
        return new ResponseEntity<>("Denied", HttpStatus.FORBIDDEN);

    }

    public ResponseEntity<List<Customer>> findAll() {
        return new ResponseEntity<>(customerDAO.findAll(), HttpStatus.OK);
    }

}

