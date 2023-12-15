package com.example.springboot2aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {


    /**
     * Run before the method execution.
     *
     * @param joinPoint
     */
    @Before("execution(* com.example.springboot2aop.service.EmployeeService.createEmployee(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.debug("logBefore running .....");
        log.debug("Enter: {}.{}() With argument[s] = {}",
                joinPoint.getSignature().getDeclaringType(), // path of Class => c.e.s.aspect.LoggingAspect
                joinPoint.getSignature().getName(), // path odf method => Enter: class com.example.springboot2aop.service.EmployeeService.createEmployee()
                Arrays.toString(joinPoint.getArgs()));// [Employee [id=null, firstName=admin, lastName=admin, emailId=admin@gmail.com]]
    }

    /**
     * Run after the method returned a result.
     *
     * @param joinPoint
     */
    @After("execution(* com.example.springboot2aop.service.EmployeeService.createEmployee(..))")
    public void logAfter(JoinPoint joinPoint) {
        log.debug("logAfter running .....");
        log.debug("Enter: {}.{}() With argument[s] = {}",
                joinPoint.getSignature().getDeclaringTypeName(), // same of Before
                joinPoint.getSignature().getName(), // same of Before
                Arrays.toString(joinPoint.getArgs())); // [Employee [id=1, firstName=admin, lastName=admin, emailId=admin@gmail.com]]
    }

    /**
     * Run around the method execution.
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.example.springboot2aop.service.EmployeeService.getEmployeeById(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("logAround running ....");
        if (log.isDebugEnabled()) {
            log.debug
                    ("Enter: {}.{}() With argument[s] = {}",
                            joinPoint.getSignature().getDeclaringType(),
                            joinPoint.getSignature().getName(), // Enter: class com.example.springboot2aop.service.EmployeeService.getEmployeeById()
                            Arrays.toString(joinPoint.getArgs())); // [1]
        }
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{} with result = {}",
                        joinPoint.getSignature().getDeclaringType(),
                        joinPoint.getSignature().getName(), // Exit: class com.example.springboot2aop.service.EmployeeService.getEmployeeById
                        result // Optional[Employee [id=1, firstName=admin, lastName=admin, emailId=admin@gmail.com]]
                );
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()",
                    Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
            throw e;
        }
    }

    /**
     * Run after the method returned a result, intercept the returned result as well.
     *
     * @param joinPoint
     * @param result
     */
    @AfterReturning(pointcut = "execution(* com.example.springboot2aop.service.EmployeeService.deleteEmployee(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.debug("logAfterReturning running ....");
        log.debug("Enter: {}.{}() with argument[s] = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), // Enter: com.example.springboot2aop.service.EmployeeService.deleteEmployee()
                Arrays.toString(joinPoint.getArgs()) // [1]
        );
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "execution(* com.example.springboot2aop.service.EmployeeService.updateEmployee(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        log.debug("logAfterThrowing running .....");
        log.error("Exception in {}.{}() with cause = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                error.getCause() != null ? error.getCause() : "NULL");
    }

}
