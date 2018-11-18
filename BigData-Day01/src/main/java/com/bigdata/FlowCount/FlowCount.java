package com.bigdata.FlowCount;

import com.bigdata.FlowCount.bean.FlowBean;
import com.bigdata.ProvinceFlow.ProvincePartioner;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by qian on 2017/3/27.
 */
public class FlowCount {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFs", "hdfs://hadoop01:9000");
        configuration.set("HADOOP_USER_NAME", "root");
//        configuration.set("mapreduce.framework.name", "yarn");
//        configuration.set("yarn.resourcemanager.hostname", "hadoop01");

        if (args == null || args.length == 0) {
            args = new String[2];
            args[0] = "hdfs://hadoop01:9000/httpcount/input/";
            args[1] = "hdfs://hadoop01:9000/httpcount/output/";
        }

        Job job = null;
        try {

            job = Job.getInstance(configuration);

            // 指定本程序的jar包所在路径
            job.setJarByClass(FlowCount.class);

            // 指定本业务job需要使用的Mapper/Reduce业务类
            job.setMapperClass(FlowCountMapper.class);
            job.setReducerClass(FlowCountReducer.class);

            // 指定mapper输出的数据的key-value类型
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(FlowBean.class);

            // 指定自定义数据分析器
            job.setPartitionerClass(ProvincePartioner.class);
            // 同时指定相应分区数量的reduce
            job.setNumReduceTasks(5);

            // 指定最终输出的数据的key-value类型
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(FlowBean.class);

            // 指定job的输入原始文件所在目录
            FileInputFormat.setInputPaths(job, new Path(args[0]));

            Path outputPath = new Path(args[1]);
            FileSystem fileSystem = FileSystem.get(configuration);
            if (fileSystem.exists(outputPath)) {
                fileSystem.delete(outputPath, true);
            }
            // 指定job的输出原始文件所在目录
            FileOutputFormat.setOutputPath(job, outputPath);

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

    static class FlowCountMapper extends Mapper<LongWritable, Text, Text, FlowBean> {

        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            // 将一行内容转化为String
            String line = value.toString();
            // 切分字段
            String[] fileds = line.split("\t");
            // 取出手机号码
            String phoneNumber = fileds[1];
            // 取出上行流量和下行流量
            long upFlow   = Long.parseLong(fileds[fileds.length - 3]);
            long downFlow = Long.parseLong(fileds[fileds.length - 2]);

            context.write(new Text(phoneNumber), new FlowBean(upFlow, downFlow));
        }
    }

    static class FlowCountReducer extends Reducer<Text, FlowBean, Text, FlowBean> {

        @Override
        protected void reduce(Text key, Iterable<FlowBean> values, Context context)
                throws IOException, InterruptedException {
            long sum_upFlow   = 0;
            long sum_downFlow = 0;
            // 遍历所有bean，将其中的上行流量，下行流量分别累加

            for (FlowBean flowbean : values) {
                sum_upFlow += flowbean.getUpFlow();
                sum_downFlow += flowbean.getDownFlow();
            }

            FlowBean resultBean = new FlowBean(sum_upFlow, sum_downFlow, sum_downFlow+sum_upFlow);
            context.write(key, resultBean);
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            super.cleanup(context);
        }
    }

}
