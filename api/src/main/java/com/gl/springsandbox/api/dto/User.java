package com.gl.springsandbox.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gl.springsandbox.api.entity.Customer;
import lombok.*;

/**
 * 유저 DTO
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
public class User extends Operator {

    private Long userId;
    private String userName;
    private String address;

    @Builder
    public User(Long userId,
                @JsonProperty("name") String userName,
                String address) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.address = address;
    }

    @Builder(builderClassName = "EntityToDtoBuilder", builderMethodName = "entityToDtoBuilder")
    public User(Customer customer) {
        super();
        this.userId = customer.getId();
        this.userName = customer.getName();
        this.address = customer.getAddress();
    }
}
