package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

//自定义切面类
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    //定义切点
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("进入自动填充功能的切面");
        //获得当前被拦截方法的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//获取方法签名
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取方法上的注解
        OperationType operationType = autoFill.value();//获取操作类型
        //获取当前被拦截方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if (args != null || args.length == 0) {
            return;
        }
        Object entity = args[0];//获取实体对象

        //根据不同的操作类型，进行不同的自动填充
        switch (operationType) {
            case INSERT:
                try {
                    //反射机制，调用实体类的setCreateTime、setUpdateTime、setCreateUser、setUpdateUser方法
                    entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, java.time.LocalDateTime.class).invoke(entity, java.time.LocalDateTime.now());
                    entity.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, java.time.LocalDateTime.class).invoke(entity, java.time.LocalDateTime.now());
                    entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, Long.class).invoke(entity, 1L);//假设当前用户ID为1
                    entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(entity, 1L);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case UPDATE:
                try {
                    //反射机制，调用实体类的setUpdateTime、setUpdateUser方法
                    entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, java.time.LocalDateTime.class).invoke(entity, java.time.LocalDateTime.now());
                    entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(entity, 1L);//假设当前用户ID为1
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                break;
        }
    }
}
