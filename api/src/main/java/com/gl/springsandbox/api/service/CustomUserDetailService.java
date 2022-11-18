package com.gl.springsandbox.api.service;

import com.gl.springsandbox.api.dto.UserAuthInfo;
import com.gl.springsandbox.api.entity.Customer;
import com.gl.springsandbox.api.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final CustomerRepository customerRepository;


    public CustomUserDetailService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;

    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer user = customerRepository.getCustomersByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("#### 인증 시도할 유저를 찾을 수 없습니다"));
        return UserAuthInfo.builder().customer(user).build();
    }
}
