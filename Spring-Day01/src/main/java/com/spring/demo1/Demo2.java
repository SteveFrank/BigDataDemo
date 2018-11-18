package com.spring.demo1;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by qian on 2017/4/6.
 */
public class Demo2 {

    @Test
    public void run1() {
        UserService userService = new UserServiceImpl();
        userService.sayHello();
    }

    @Test
    public void run2() {
        // 使用Spring方式
        // 1、创建工厂
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 2、从工厂中获取对象
        UserService userService = (UserServiceImpl) applicationContext.getBean("userService");
        // 3、调用对象方法执行
        userService.sayHello();
    }


}
