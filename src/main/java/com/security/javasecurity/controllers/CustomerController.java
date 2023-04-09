package com.security.javasecurity.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.security.javasecurity.models.Customer;
import com.security.javasecurity.models.dto.CustomerDTO;
import com.security.javasecurity.services.CustomerService;
import com.security.javasecurity.views.Views;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CustomerController {

    private CustomerService customerService;

    @PostMapping("customers/register")
    @JsonView(value = {Views.User.class})
    private void registerClient(@RequestBody CustomerDTO customerDto) {
        customerService.register(customerDto);
    }

    @PostMapping("customers/login")
    @JsonView(value = {Views.User.class})
    private ResponseEntity<String> loginClient(@RequestBody CustomerDTO customerDto) {
        return customerService.login(customerDto);
    }

    @JsonView(value = {Views.User.class})
    @GetMapping("customers/all")
    private ResponseEntity<List<Customer>> getAllCustomersWithoutSensitiveInfo() {
        return customerService.findAll();
    }
    //ADMIN

    @JsonView(value = {Views.Admin.class})
    @GetMapping("admin/all")
    private ResponseEntity<List<Customer>> getAllCustomers() {
        return customerService.findAll();
    }
}
