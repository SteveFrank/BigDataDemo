package com.bigdata.FlowCountSort;

import com.bigdata.FlowCount.bean.FlowBean;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by qian on 2017/3/29.
 *
 */
public class FlowCountSort {

    static FlowBean flowBean = new FlowBean();
    static Text text = new Text();

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
//        configuration.set("mapreduce.framework.name", "local");
        configuration.set("fs.defaultFS", "hdfs://hadoop01:9000");
        configuration.set("HADOOP_USER_NAME", "root");

        if (args == null || args.length == 0) {
            args = new String[2];
            args[0] = "hdfs://hadoop01:9000/httpcount/output/";
            args[1] = "hdfs://hadoop01:9000/httpcount/output-sort/";
        }

        Job job = null;
        try {

            job = Job.getInstance(configuration);

            // 指定本程序的jar包所在路径
//            job.setJarByClass(FlowCountSort.class);
            job.setJar("/Users/qian/.m2/repository/org/bigdata/day01/bigdata-day01/1.0-SNAPSHOT/bigdata-day01-1.0-SNAPSHOT.jar");
            // 指定本业务job需要使用的Mapper/Reduce业务类
            job.setMapperClass(FlowCountSort.FlowCountSortMapper.class);
            job.setReducerClass(FlowCountSort.FlowCountSortReducer.class);

            // 指定mapper输出的数据的key-value类型
            job.setMapOutputKeyClass(FlowBean.class);
            job.setMapOutputValueClass(Text.class);

            // 指定自定义数据分析器
//            job.setPartitionerClass(ProvincePartioner.class);
            // 同时指定相应分区数量的reduce
//            job.setNumReduceTasks(5);

            // 指定最终输出的数据的key-value类型
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(FlowBean.class);

            // 指定job的输入原始文件所在目录
            FileInputFormat.setInputPaths(job, new Path(args[0]));
            // 指定job的输出原始文件所在目录

            Path outputPath = new Path(args[1]);
            FileSystem fileSystem = FileSystem.get(configuration);
            if (fileSystem.exists(outputPath)) {
                fileSystem.delete(outputPath, true);
            }
            FileOutputFormat.setOutputPath(job, outputPath);

            // 讲job中配置的相关参数，以及job所用的java类所在的jar包，提交给yarn进行提交
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

    static class FlowCountSortMapper extends Mapper<LongWritable, Text, FlowBean, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // 拿到的是上一个统计程序的输出信息，现在已经是手机号的总流量的输出信息
            String line = value.toString();

            String[] fields = line.split("\t");

            String phoneNumber = fields[0];

            long upFlow = Long.parseLong(fields[1]);
            long downFlow = Long.parseLong(fields[2]);

            flowBean.set(upFlow, downFlow);
            text.set(phoneNumber);

            context.write(flowBean, text);
        }
    }

    static class FlowCountSortReducer extends Reducer<FlowBean, Text, Text, FlowBean> {

        @Override
        protected void reduce(FlowBean bean, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            context.write(values.iterator().next(), bean);
        }

    }
}
