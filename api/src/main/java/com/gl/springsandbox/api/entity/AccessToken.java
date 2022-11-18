package com.gl.springsandbox.api.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@RedisHash(value = "accessToken", timeToLive = 60 * 60 * 24)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class AccessToken {
    @Id
    private String token;

    private String userDetails;

    @Builder
    public AccessToken(String userDetailsAsString, String token) {
        this.token = token;
        this.userDetails = userDetailsAsString;
    }

}
