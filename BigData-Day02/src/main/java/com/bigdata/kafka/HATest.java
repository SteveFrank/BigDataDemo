package com.bigdata.kafka;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Created by qian on 2017/4/3.
 */
public class HATest {
    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://namenode");
        configuration.set("HADOOP_USER_NAME", "root");
        FileSystem fs = FileSystem.get(configuration);

        fs.copyFromLocalFile(new Path("/Users/qian/Downloads/HA-etc"), new Path("/"));
//        fs.delete(new Path("/friends"));
        fs.close();
    }
}
