package com.security.javasecurity.dao;

import com.security.javasecurity.models.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface CustomerDAO extends JpaRepository<Customer, Integer> {

    Customer findCustomerByEmail(String email);
}
