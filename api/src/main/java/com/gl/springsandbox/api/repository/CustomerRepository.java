package com.gl.springsandbox.api.repository;

import com.gl.springsandbox.api.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    public Optional<Customer> getCustomersByEmail(String email);
}
