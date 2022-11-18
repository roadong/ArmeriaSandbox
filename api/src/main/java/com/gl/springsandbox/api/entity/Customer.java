package com.gl.springsandbox.api.entity;

import com.gl.springsandbox.api.dto.User;
import lombok.*;
import net.bytebuddy.build.ToStringPlugin;

import javax.persistence.*;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Entity(name = "CUSTOMER")
@Table(name = "CUSTOMER", uniqueConstraints = @UniqueConstraint(columnNames = { "EMAIL" }))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Customer extends TimeAuditor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @Getter
    private long id;
    @Column(name = "NAME")
    @Getter
    private String name;

    @Column(name = "EMAIL")
    @Getter
    private String email;

    @Column(name = "PASSWORD")
    @Getter
    private String password;

    @Column(name = "ADDRESS")
    @Getter
    private String address;

    @Column(name = "ROLE")
    @Getter
    private String role;

    @Column(name = "ENABLED")
    @Getter
    private boolean enabled;

    @Column(name = "OPERATION_USER")
    @Getter
    private String updateBy;

    @Builder
    public Customer(User user) {
        if(nonNull(user.getUserId())) {
            this.id = user.getUserId();
        }
        this.name = user.getUserName();
        this.address = user.getAddress();
        this.updateBy = user.getOperator();
    }
}
