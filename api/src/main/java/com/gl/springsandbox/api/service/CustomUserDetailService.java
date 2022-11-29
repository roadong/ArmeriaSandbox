package com.gl.springsandbox.api.service;

import com.gl.springsandbox.api.dto.UserAuthInfo;
import com.gl.springsandbox.api.entity.Customer;
import com.gl.springsandbox.api.repository.CustomerRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.ValidationException;

@Service
public class CustomUserDetailService implements UserDetailsService, UserDetailsPasswordService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;


    public CustomUserDetailService(CustomerRepository customerRepository,
                                   PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer user = customerRepository.getCustomersByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("#### 인증 시도할 유저를 찾을 수 없습니다"));
        return UserAuthInfo.builder().customer(user).build();
    }

    public void validPassword(UserDetails userDetails, String challenge) {
        if(!passwordEncoder.matches(challenge, userDetails.getPassword())) {
            throw new IllegalStateException("### 비밀번호가 틀립니다");
        }
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }
}
