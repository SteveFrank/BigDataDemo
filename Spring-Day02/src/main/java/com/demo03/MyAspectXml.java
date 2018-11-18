package com.demo03;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by qian on 2017/4/10.
 * 切面类：切入点 + 通知
 */
public class MyAspectXml {
    /**
     * 通知：具体的增强
     */
    public void log() {
        System.out.println("... ... Record Log From Thie System ... ...");
    }

    /**
     * 最终通知：方法执行成功或者出现异常，都会执行
     */
    public void after() {
        System.out.println("... ... after Method ... ...");
    }

    /**
     * 方法执行之后，执行后置通知。程序出现异常，后置通知不会执行
     * 可以对于结果进行一定的处理
     */
    public void afterReturn() {
        System.out.println("... ... 后置通知 ... ...");
    }

    /**
     * 环绕通知：方法执行之前和方法执行之后的通知
     * 默认的情况下，目标对象方法不能够执行，需要手动让目标对象的方法执行
     */
    public void around(ProceedingJoinPoint proceedingJoinPoint) {
        System.out.println("环绕通知1 ... ...  ");

        try {
            // 手动让目标对象的方法去执行
            proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        System.out.println("环绕通知2 ... ...  ");
    }
}
