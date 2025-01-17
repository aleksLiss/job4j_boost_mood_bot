package ru.job4j.aspect.exception;

import org.aspectj.lang.annotation.AfterThrowing;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionHandlingAspect {

    public static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlingAspect.class);

    @AfterThrowing(pointcut = "execution(ru.job4j.business.*.*(..))", throwing = "ex")
    public void handleException(Exception ex) {
        LOG.error("An error occured: {}", ex.getMessage());
    }
}
