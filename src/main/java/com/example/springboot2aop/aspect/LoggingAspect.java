package com.example.springboot2aop.aspect;

import com.example.springboot2aop.bean.Employee;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * ==> Common AspectJ annotations :
 *
 * @Before – Run before the method execution => Advice that runs before a join point but that does not have the ability to prevent execution flow from proceeding to the join point (unless it throws an exception).
 * @After – Run after the method returned a result => Advice to be executed regardless of the means by which a join point exits (normal or exceptional return).
 * @AfterReturning – Run after the method returned a result, and intercept the returned result as well. => Advice to be run after a join point completes normally (for example, if a method returns without throwing an exception).
 * @AfterThrowing – Run after the method throws an exception => Advice to be executed if a method exists by throwing an exception.
 * @Around – Run around the method execution, and combine all three advices above. =>
 * <p>
 * <p>
 * Join point is a point during the execution of a program,
 * such as the execution of a method or the handling of an exception.
 * In Spring AOP, a join point always represents a method execution.
 */

/**
 * Before advice: Advice that runs before a join point but that does not have the ability to prevent execution flow from proceeding to the join point (unless it throws an exception).
 * After (finally) advice: Advice to be executed regardless of the means by which a join point exits (normal or exceptional return).
 * After returning advice: Advice to be run after a join point completes normally (for example, if a method returns without throwing an exception).
 * After throwing advice: Advice to be executed if a method exists by throwing an exception.
 * Around advice: Advice that surrounds a join point such as a method invocation. This is the most powerful kind of advice. Around advice can perform custom behavior before and after the method invocation. It is also responsible for choosing whether to proceed to the join point or to shortcut the advised method execution by returning its own return value or throwing an exception.
 */

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
     * @Around annotated methods run before and after all methods matching with a pointcut expression
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

    @Before("execution(* com.example.springboot2aop.service.EmployeeService.test(..)) && args(employee) ")
    public void validateFields(Employee employee) {
        if (employee.getFirstName() == null && employee.getEmailId() == null) {
            log.info("FirstName and Email must not be null");
            throw new IllegalArgumentException("FirstName and Email must not be null");
        } else if (isEmpty(employee.getEmailId())) {
            log.info("Email must not be null");
            throw new IllegalArgumentException("Email must not be null");
        }
    }

    @Before("@annotation(com.example.springboot2aop.aspect.CheckValidationEmployeeFields) && args(employee)")
    public void validateFieldsAnnotation(Employee employee) {
        if (employee.getFirstName() == null && employee.getEmailId() == null) {
            log.info("FirstName and Email must not be null");
            throw new IllegalArgumentException("FirstName and Email must not be null");
        } else if (isEmpty(employee.getEmailId())) {
            log.info("Email must not be null");
            throw new IllegalArgumentException("Email must not be null");
        }
    }

    private static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    private static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

}
