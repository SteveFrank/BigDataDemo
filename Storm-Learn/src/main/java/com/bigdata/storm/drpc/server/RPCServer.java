package com.bigdata.storm.drpc.server;

import com.bigdata.storm.drpc.UserService;
import com.bigdata.storm.drpc.UserServiceImpl;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;

/**
 * RPC Server 服务
 * @author yangqian
 * @date 2018-11-25
 */
public class RPCServer {

    public static void main(String[] args) throws IOException {

        Configuration configuration = new Configuration();
        RPC.Builder builder = new RPC.Builder(configuration);
        long clientVersion = 1992L;
        // Java Builder 模式
        RPC.Server server = builder.setProtocol(UserService.class)
                .setInstance(new UserServiceImpl())
                .setBindAddress("localhost")
                .setPort(9999).build();
        server.start();
    }

}
