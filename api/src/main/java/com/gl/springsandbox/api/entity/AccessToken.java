package com.gl.springsandbox.api.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;


@RedisHash(value = "accessToken")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class AccessToken {
    @Id
    private String token;

    @Indexed
    private String userName;

    private String userDetails;

    @TimeToLive
    private int expired;

    @Builder
    public AccessToken(String userDetailsAsString,
                       String userName,
                       String token,
                       int ttlSeconds) {
        this.token = token;
        this.userDetails = userDetailsAsString;
        this.userName = userName;
        this.expired = ttlSeconds;
    }

}
