package com.bigdata.storm.drpc.client;

import com.bigdata.storm.drpc.UserService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * RPC Client 使用Server服务
 * @author yangqian
 * @date 2018-11-25
 */
public class RPCClient {
    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration();
        long clientVersion = 88888888;
        UserService userService = RPC.getProxy(UserService.class, clientVersion,
                new InetSocketAddress("localhost",9999),
                configuration);
        System.out.println("======Client=====");
        userService.addUser("zhangsan", 12);
        System.out.println("======Client=====");
        RPC.stopProxy(userService);
    }
}
