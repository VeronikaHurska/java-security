package com.security.javasecurity.security.filters;

import com.security.javasecurity.dao.CustomerDAO;
import com.security.javasecurity.models.Customer;
import com.security.javasecurity.services.CustomerService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private CustomerDAO customerDAO;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.replace("Bearer ", "");

            String subject = Jwts.parser()
                    .setSigningKey("jopa".getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            Customer customer = customerDAO.findCustomerByEmail(subject);
            if (customer != null) {
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                                new UsernamePasswordAuthenticationToken(
                                        customer.getUsername(),
                                        customer.getPassword(),
                                        customer.getAuthorities()
                                )
                        );
            }
        }

        filterChain.doFilter(request, response);
    }
}
