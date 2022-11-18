package com.gl.springsandbox.api.repository;

import com.gl.springsandbox.api.entity.AccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface TokenRepository extends CrudRepository<AccessToken, String> {
}
