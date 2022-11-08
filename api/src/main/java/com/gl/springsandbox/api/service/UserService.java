package com.gl.springsandbox.api.service;

import com.gl.springsandbox.api.dto.User;
import com.gl.springsandbox.api.entity.Customer;
import com.gl.springsandbox.api.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final CustomerRepository customerRepository;

    public UserService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public long insertUser(User userInfo) {
        Customer insertUser = customerRepository.save(Customer.builder().user(userInfo).build());
        return insertUser.getId();
    }

    @Transactional(readOnly = true)
    public User getUser(long id) {
        var user = customerRepository.findById(id).orElseThrow();
        return User.entityToDtoBuilder().customer(user).build();
    }

    @Transactional
    public User modifyUser(User userInfo) {
        var updateUser = customerRepository.save(Customer.builder()
                .user(userInfo)
                .operator("operator").build());
        return User.entityToDtoBuilder().customer(updateUser).build();
    }

    @Transactional
    public boolean deleteUser(long id) {
        customerRepository.deleteById(id);
        return true;
    }
}
