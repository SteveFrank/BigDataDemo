package com.bigdata.storm.drpc;

/**
 * @author yangqian
 * @date 2018-11-25
 */
public interface UserService {

    public static final long versionID = 88888888;

    public void addUser(String name, int age);
}
