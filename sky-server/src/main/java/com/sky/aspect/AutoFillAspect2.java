package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;


@Aspect
@Component
@Slf4j
public class AutoFillAspect2 {


    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    void pointcut(){}

    @Before("pointcut()")
    public void autofill(JoinPoint joinPoint) throws Exception {
        MethodSignature method = (MethodSignature) joinPoint.getSignature();
        AutoFill annotation = joinPoint.getClass().getAnnotation(AutoFill.class);
        OperationType value = annotation.value();
        Object[] args = joinPoint.getArgs();

        if (args.length == 0 && args == null){
            return;
        }
        Object entity = args[0];
        Long currentId = BaseContext.getCurrentId();
        LocalDateTime now = LocalDateTime.now();

        if (value == OperationType.INSERT) {
            Method setCreateTime = entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", LocalDateTime.class);
            Method setCreateUser = entity.getClass().getDeclaredMethod("setCreateUser", LocalDateTime.class);
            Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);

            setCreateTime.invoke(entity,now);
            setCreateUser.invoke(entity,currentId);
            setUpdateUser.invoke(entity,currentId);
            setUpdateTime.invoke(entity,now);
        } else {

        }

    }



}
