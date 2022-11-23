package com.gl.springsandbox.api.repository;

import com.gl.springsandbox.api.entity.AccessToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface TokenRepository extends CrudRepository<AccessToken, String> {
    Optional<AccessToken> findAccessTokenByToken(String token);
}
