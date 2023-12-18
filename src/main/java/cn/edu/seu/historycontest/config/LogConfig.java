package cn.edu.seu.historycontest.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
@Slf4j
public class LogConfig {

    public LogConfig() {
    }


    @Pointcut("execution(public * cn.edu.seu.historycontest.controller.*.*(..))")
    public void webLogPointcut() {
    }


    @After("webLogPointcut()")
    public void doAfter(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null)
            return;
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();


        log.info("{} {}({}) : {} {} : {}",
                response != null ? String.valueOf(response.getStatus()) : "(empty)",
                request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "(empty)",
                request.getRemoteAddr(),
                request.getMethod(),
                request.getRequestURI(),
                joinPoint.getArgs());
    }

    @AfterThrowing(value = "webLogPointcut()", throwing = "throwable")
    public void doAfterThrowing(Throwable throwable) {
        log.error(throwable.getMessage());
    }

}
