package com.demo.elasticsearch.client;

import org.elasticsearch.client.Client;

/**
 * 使用单例类，避免客户端的ElasticSearch耗时过长
 * @author yangqian
 * @date 2018/11/21
 */
public class EsClientBuilder {

    private static class StaticHolder {
        static final Client INSTANCE = EsClientFactory.create();
    }
    public static Client getSingleton() {
        return StaticHolder.INSTANCE;
    }
}
