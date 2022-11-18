package com.gl.springsandbox.api.repository;

import com.gl.springsandbox.api.entity.AccessToken;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;


public interface TokenRepository extends CrudRepository<AccessToken, String> {
}
