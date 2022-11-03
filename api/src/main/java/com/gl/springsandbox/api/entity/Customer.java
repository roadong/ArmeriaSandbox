package com.gl.springsandbox.api.entity;

import com.gl.springsandbox.api.dto.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Entity(name = "CUSTOMER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends TimeAuditor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @Getter
    private long id;
    @Column(name = "NAME")
    @Getter
    private String name;
    @Column(name = "ADDRESS")
    @Getter
    private String address;

    @Column(name = "OPERATION_USER")
    @Getter
    private String updateBy;

    @Builder
    public Customer(User user, String operator) {
        if(nonNull(user.userId())) {
            this.id = user.userId();
        }
        this.name = user.userName();
        this.address = user.address();
        this.updateBy = operator;
    }
}
