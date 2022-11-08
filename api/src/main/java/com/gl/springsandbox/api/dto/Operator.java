package com.gl.springsandbox.api.dto;


import lombok.Getter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;


// TODO: 서버 진입 (컨트롤러) 부터 세션 정보를 넣느냐 / 엔티티 (서비스 - 리포지토리) 단계에서 정보를 넣느냐 \
// 합리적인 이유를 성립하고 선택해야할듯?
public abstract class Operator {
    @Getter
    private final String operator;

    public Operator() {
        this.operator = SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
