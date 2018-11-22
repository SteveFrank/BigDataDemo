package com.demo.elasticsearch.client.test;


import com.demo.elasticsearch.client.EsClientBuilder;
import com.google.gson.Gson;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.Client;
import org.junit.Test;

import java.net.UnknownHostException;

/**
 * @author yangqian
 * @date 2018/11/21
 */
public class ESClientTest {

    private Client client = EsClientBuilder.getSingleton();

    @Test
    public void test_create_index() throws UnknownHostException {
        // 创建一个test索引
        CreateIndexRequestBuilder createIndexRequestBuilder
                = client.admin().indices().prepareCreate("test");
        CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();
        System.out.println(response.isAcknowledged());
    }

    @Test
    public void test_creat_index_json_template() {
        // 根据模板创建索引

    }

    @Test
    public void test_insert_document() {


    }

}
