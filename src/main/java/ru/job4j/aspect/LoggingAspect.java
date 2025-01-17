package ru.job4j.aspect;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingAspect.class.getName());

    @Pointcut("execution(ru.job4j.business.*Service)")
    private void loggingServices() {
    }

    @Before("loggingServices()")
    public void loggingBeforeAllServiceMethods(JoinPoint joinPoint) {
        String prefix = new StringBuilder(joinPoint.getSignature().getName())
                .append(" was called with arguments: ").toString();
        StringBuilder infoAboutMethod = new StringBuilder(prefix);
        Arrays.stream(joinPoint.getArgs())
                .forEach(infoAboutMethod::append);
        LOG.info(infoAboutMethod.toString());
    }
}
