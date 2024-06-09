package com.argus.deal.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(public * com.argus.deal.service.*.*(..)) || execution(public * com.argus.deal.controller.*.*(..))")
    private void publicMethodsFromServiceAndControllerPackage() {
    }

    @Before(value = "publicMethodsFromServiceAndControllerPackage()")
    public void logBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        log.info(">> {}.{}() - Received: {}", className, methodName, Arrays.toString(args));
    }

    @AfterReturning(value = "publicMethodsFromServiceAndControllerPackage()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        log.info("<< {}.{}() - Returned: {}", className, methodName, result);
    }
}
