package com.gl.springsandbox.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@Slf4j
public class APIApplication {
    public static void main(String[] args) {
        log.info("### start");
        SpringApplication.run(APIApplication.class, args);
    }
}
