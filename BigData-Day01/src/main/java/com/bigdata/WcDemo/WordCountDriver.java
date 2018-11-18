package com.bigdata.WcDemo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 相当于是一个yarn集群的客户端
 *
 * 需要在此处封装我们的mr程序相关运行参数，指定jar包
 * 最后提交给yarn
 *
 * Created by qian on 2017/3/25.
 */
public class WordCountDriver {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFs", "hdfs://hadoop01:9000");
        configuration.set("HADOOP_USER_NAME", "root");
        configuration.set("mapreduce.framework.name", "yarn");
        configuration.set("yarn.resourcemanager.hostname", "hadoop01");

        if (args == null || args.length == 0) {
            args = new String[2];
            args[0] = "hdfs://hadoop01:9000/wordcount/input/";
            args[1] = "hdfs://hadoop01:9000/wordcount/output/";
        }

        Job job = null;
        try {

            job = Job.getInstance(configuration);

            // 指定本程序的jar包所在路径
            job.setJarByClass(WordCountDriver.class);

            // 指定本业务job需要使用的Mapper/Reduce业务类
            job.setMapperClass(WordCountMapper.class);
            job.setReducerClass(WordCountReducer.class);

            // 指定mapper输出的数据的key-value类型
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);

            // 指定最终输出的数据的key-value类型
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            // 指定job的输入原始文件所在目录
            FileInputFormat.setInputPaths(job, new Path(args[0]));
            // 指定job的输出原始文件所在目录
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            // 讲job中配置的相关参数，以及job所用的java类所在的jar包，提交给yarn进行提交
//            job.submit();
            boolean result = job.waitForCompletion(true);
            System.exit(result ? 0 : 1);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
