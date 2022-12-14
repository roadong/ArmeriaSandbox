package com.gl.springsandbox.api.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;


/**
 * API 요청에 대한 흐름을 관심사(Pointcut)로 두고 과정에 대해 로그를 남기는(Advice) 컴포넌트 클래스
 */
@Aspect
@Component
@Slf4j
public class RequestFlowLogger {

    private String combineParameterInfo(JoinPoint joinPoint) {
        List<Object> paramNameList = getParameterValues(joinPoint);
        List<String> paramValueList = getParameterNameList(joinPoint);
        StringJoiner joiner = new StringJoiner(",");
        for (int idx = 0; idx < paramNameList.size(); idx++) {
            joiner.add("[%s=%s]".formatted(paramNameList.get(idx), paramValueList.get(idx)));
        }
        return joiner.toString();
    }

    private List<Object> getParameterValues(JoinPoint joinPoint) {
        return Arrays.asList(joinPoint.getArgs());
    }

    private List<String> getParameterNameList(JoinPoint joinPoint) {
        MethodSignature signature = ((MethodSignature) joinPoint.getSignature());
        return Arrays.asList(signature.getParameterNames());
    }

    private String getMethodName(JoinPoint joinPoint) {
        MethodSignature signature = ((MethodSignature) joinPoint.getSignature());
        return signature.getMethod().getName();
    }

    @AfterReturning(pointcut = "within(com.gl.springsandbox.api.controller..*)", returning = "response")
    public void flowController(JoinPoint joinPoint, ResponseEntity<?> response) {
        log.info("### API 요청 처리 완료 - controller: [%s] / response: [%s]".formatted(getMethodName(joinPoint), response));
    }

    @AfterThrowing(pointcut = "within(com.gl.springsandbox.api.controller.*)", throwing = "exception")
    public void flowControllerException(JoinPoint joinPoint, Throwable exception) {
        log.error("### API 요청 처리 중 예외 발생", exception);
    }

    @AfterReturning(pointcut = "within(com.gl.springsandbox.api.service..*)", returning = "result")
    public void flowService(JoinPoint joinPoint, Object result) {
        log.info("### 서비스 요청 처리 완료 - service: [%s] / response: [%s]".formatted(getMethodName(joinPoint), result));
    }

    @AfterThrowing(pointcut = "within(com.gl.springsandbox.api.service..*)", throwing = "exception")
    public void flowServiceException(JoinPoint joinPoint, Throwable exception) {
        log.error("### 서비스 처리 중 예외 발생", exception);
    }

//    @AfterReturning(pointcut = "within(com.gl.springsandbox.api.repository..*)",
//            returning = "result")
//    public void flowRepository(JoinPoint joinPoint, Object result) {
//
//        log.info("### DB 요청 처리");
//    }
//
//    @AfterThrowing(pointcut = "within(com.gl.springsandbox.api.repository..*)",
//            throwing = "exception")
//    public void flowRepositoryException(JoinPoint joinPoint, Throwable exception) {
//        log.error("### DB 처리 중 예외 발생", exception);
//    }
}
