package com.bigdata.ProvinceFlow;

import com.bigdata.FlowCount.bean.FlowBean;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

import java.util.HashMap;

/**
 * <K2 , V2> 对应的是Map的输出类型（KV）
 * Created by qian on 2017/3/27.
 */
public class ProvincePartioner extends Partitioner<Text, FlowBean> {

    public static HashMap<String, Integer> proviceDicationary =
            new HashMap<String, Integer>();

    static {
        proviceDicationary.put("136", 0);
        proviceDicationary.put("137", 1);
        proviceDicationary.put("138", 2);
        proviceDicationary.put("139", 3);
    }

    @Override
    public int getPartition(Text key, FlowBean flowBean, int numPartitions) {

        String prefix = key.toString().substring(0, 3);
        Integer provinceID;
        if (proviceDicationary.keySet().contains(prefix)) {
            provinceID = proviceDicationary.get(prefix);
        } else {
            provinceID = 4;
        }

        return provinceID;
    }
}
