package com.gl.springsandbox.api.dto;

import com.gl.springsandbox.api.entity.Customer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAuthInfo implements UserDetails {

    private String userName;
    private String email;
    private String password;
    private String role;
    private boolean enabled;


    @Builder
    public UserAuthInfo(Customer customer) {
        this.userName = customer.getName();
        this.password = customer.getPassword();
        this.role = customer.getRole();
        this.email = customer.getEmail();
        this.enabled = customer.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: 역할이 여러개 일 때 변경
        return Collections.singletonList(() -> role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // withdraw 기능인데 이건 enabled 와 차별점이 생기면 따로 적용
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 필요하다면 password 횟수 체크 policy 만들고 추가해서 체크
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 필요하다면 password 업데이트 컬럼 만들고 policy 추가해서 체크
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
