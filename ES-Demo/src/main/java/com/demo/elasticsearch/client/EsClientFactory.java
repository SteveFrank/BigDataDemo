package com.demo.elasticsearch.client;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @author yangqian
 * @date 2018/11/21
 */
class EsClientFactory {

    /**
     * 无模板创建模式
     * @return 返回客户端
     */
    static Client create() {
        Settings settings = Settings.builder()
                .put("client.transport.sniff", true)
                .put("cluster.name", "es-application")
                .build();
        try {
            return new PreBuiltTransportClient(settings).addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据模板创建客户端
     * @return
     */
    static Client createByTemplate() {
        try {
            Settings settings = Settings.builder().loadFromSource(jsonBuilder()
                    .startObject().startObject("analysis").startObject("analyzer").startObject("synonym")
                    .field("tokenizer", "whitespace")
                    .field("filter", new String[] {"synonym"})
                    .endObject().endObject().endObject()
                    .startObject("filter").startObject("synonym")
                    .field("type", "synonym").field("synonyms", new String[] {"Highlandstreet, ravioli"}).field("ignore_cate",true).field("expand", true)
                    .endObject().endObject().endObject().endObject().string()
                    , XContentType.JSON).build();
            return new PreBuiltTransportClient(settings).addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
