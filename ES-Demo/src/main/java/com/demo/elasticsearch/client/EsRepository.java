package com.demo.elasticsearch.client;

import com.google.gson.Gson;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;

import java.util.Map;

/**
 * @author yangqian
 * @date 2018/11/21
 */
public class EsRepository {

    private Client client = EsClientBuilder.getSingleton();

    /**
     * 单条数据插入处理
     * @param index
     * @param type
     * @param data
     */
    public void insert(String index, String type, Map<String, String> data) {
        Gson gson = new Gson();
        // 首先进行数据转换
        String json = gson.toJson(data);
        // 索引处理
        IndexRequestBuilder indexRequestBuilder
                = client.prepareIndex(index, type).setSource(json);
        // 处理结果
        IndexResponse response = indexRequestBuilder.execute().actionGet();
        System.out.println(response.getIndex());
    }
}
