package com.bigdata.storm.drpc;

/**
 * @author yangqian
 * @date 2018-11-25
 */
public class UserServiceImpl implements UserService {
    public void addUser(String name, int age) {
        System.out.print("name : " + name + ", age : " + age);
    }
}
