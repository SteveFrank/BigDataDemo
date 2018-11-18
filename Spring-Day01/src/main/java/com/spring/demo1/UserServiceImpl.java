package com.spring.demo1;

/**
 * Created by qian on 2017/4/6.
 */
public class UserServiceImpl implements UserService {

    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void sayHello() {
        System.out.println("Hi ... ..." + name);
    }
}
