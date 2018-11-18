package com.demo01;

import org.springframework.stereotype.Component;

/**
 * Created by qian on 2017/4/10.
 * 组件注解：标记类
 */
@Component(value="userService")
public class UserServiceImpl implements UserService {
    public void save() {
        System.out.println("Hello Spring ... ");
    }
}
