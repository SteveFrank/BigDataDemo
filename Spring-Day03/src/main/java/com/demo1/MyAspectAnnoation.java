package com.demo1;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Created by qian on 2017/4/13.
 */
@Aspect
@Component("myAspectAnnoation")
public class MyAspectAnnoation {

    /**
     * 通知类型：@Before前置通知
     */
    @Before(value="MyAspectAnnoation.point()")
    public void log() {
        System.out.println("Record log in Before Advice ... ...");
    }

    @After(value="MyAspectAnnoation.point()")
    public void after() {
        System.out.println("after advice ... ...");
    }

    /**
     * 环绕通知
     */
    @Around(value="MyAspectAnnoation.point()")
    public void around(ProceedingJoinPoint joinPoint) {
        System.out.println("around Advice Begin ... ...");
        try {
            // 目标对象执行
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("around Advice End ... ...");
    }

    /**
     * 自定义切入点 (通用切入点配置)
     */
    @Pointcut(value="(execution(void *.*.*.save*()))")
    public void point() {}
}
