package com.gl.springsandbox.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gl.springsandbox.api.entity.Customer;
import lombok.Builder;
import org.h2.engine.UserBuilder;

/**
 * 유저 레코드 dto
 * @param userId 유저 아이디
 * @param userName 유저 이름
 * @param address 주소
 */

@Builder
public record User(Long userId,
                   @JsonProperty("name") String userName,
                   String address) {
    public static User convertEntityToDto(Customer customer) {
        return new User(customer.getId(), customer.getName(), customer.getAddress());
    }
}
