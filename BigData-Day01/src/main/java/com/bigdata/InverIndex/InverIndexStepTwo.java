package com.bigdata.InverIndex;

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
 * Created by qian on 2017/4/1.
 */
public class InverIndexStepTwo {

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://hadoop01:9000");
        configuration.set("HADOOP_USER_NAME", "root");

        if (args == null || args.length == 0) {
            args = new String[2];
            args[0] = "hdfs://hadoop01:9000/inverindexinput/step_one/output/";
            args[1] = "hdfs://hadoop01:9000/inverindexinput/step_two/output/";
        }

        Job job = null;
        try {

            job = Job.getInstance(configuration);

            // 指定本程序的jar包所在路径
//            job.setJarByClass(RJoin.class);

            job.setJar("/Users/qian/.m2/repository/org/bigdata/day01/bigdata-day01/1.0-SNAPSHOT/bigdata-day01-1.0-SNAPSHOT.jar");

            // 指定本业务job需要使用的Mapper/Reduce业务类
            job.setMapperClass(InverIndexStepTwo.InverIndexStepTwoMapper.class);
            job.setReducerClass(InverIndexStepTwo.InverIndexStepTwoReducer.class);

            // 指定mapper输出的数据的key-value类型
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);

            // 指定最终输出的数据的key-value类型
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

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


    static class InverIndexStepTwoMapper extends Mapper<LongWritable, Text, Text, Text> {

        Text k = new Text();
        Text v = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] values = line.split("\t");

            String[] wordsFiles = values[0].split("--");
            k.set(wordsFiles[0]);
            v.set(wordsFiles[1] + "-->" + values[1]);
            context.write(k,v);

        }
    }

    static class InverIndexStepTwoReducer extends Reducer<Text, Text, Text, Text> {

        Text v = new Text();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String count = "";
            for (Text value: values) {
                count += value.toString()+";";
            }
            v.set(count);
            context.write(key, v);
        }
    }

}


