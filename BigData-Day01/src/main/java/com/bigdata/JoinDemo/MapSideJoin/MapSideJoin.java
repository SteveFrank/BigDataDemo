package com.bigdata.JoinDemo.MapSideJoin;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qian on 2017/3/30.
 */
public class MapSideJoin {

    static class MapSideJoinMapper
            extends Mapper<LongWritable, Text, Text, NullWritable> {
        // 使用hashMap加载缓存产品信息表
        Map<String, String> pdInfoMap = new HashMap<String, String>();
        Text keyText = new Text();
        /**
         * 阅读父类的Mapper源码
         * 先调用的setup
         * 之后依次调用
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void setup(Context context)
                throws IOException, InterruptedException {
            BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader
                        (new FileInputStream("pds.txt")));
            String line = null;

            while (StringUtils.isNotEmpty(line = bufferedReader.readLine())) {
                String[] fields = line.split(",");
                pdInfoMap.put(fields[0], fields[1]);
            }

            bufferedReader.close();
        }

        /**
         * 因为已经持有完整的产品信息表，所以在Map方法中就能够实现join逻辑
         * @param key
         * @param value
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            // 由于已经持有完整的产品信息表，所以在Map方法中就能够实现join逻辑
            String orderLine = value.toString();
            String[] fields = orderLine.split(",");
            String pdName = pdInfoMap.get(fields[1]);

            keyText.set(orderLine+","+pdName);
            context.write(keyText, NullWritable.get());
        }
    }

    public static void main(String[] args)
            throws Exception {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://hadoop01:9000");
        configuration.set("HADOOP_USER_NAME", "root");

        if (args == null || args.length == 0) {
            args = new String[2];
            args[0] = "hdfs://hadoop01:9000/orderproduct/input/";
            args[1] = "hdfs://hadoop01:9000/orderproduct/output-joinSide/";
        }

        Job job = Job.getInstance();
        job.setJar("/Users/qian/.m2/repository/org/bigdata/day01/bigdata-day01/1.0-SNAPSHOT/bigdata-day01-1.0-SNAPSHOT.jar");
        job.setJarByClass(MapSideJoin.class);

        job.setMapperClass(MapSideJoinMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        Path outputPath = new Path(args[1]);
        FileSystem fileSystem = FileSystem.get(configuration);
        if (fileSystem.exists(outputPath)) {
            fileSystem.delete(outputPath, true);
        }
        FileOutputFormat.setOutputPath(job, outputPath);


        // 指定需要缓存一个文件到所有的maptask运行节点工作目录
//        job.addArchiveToClassPath(archive);  // 缓存jar包到task运行节点的classpath中
//        job.addFileToClassPath(file); // 缓存普通文件到task运行的classpath中
//        job.addCacheArchive(uri);     // 缓存压缩包文件到task运行节点的工作目录

        // 缓存普通文件到task运行节点目录
        // 讲产品表文件缓存到task工作节点的工作目录中去
        job.addCacheFile(new URI("hdfs://hadoop01:9000/orderproduct/cache/pds.txt"));

        // map段join的逻辑不需要reduce阶段，设置reduce task数量为0
        job.setNumReduceTasks(0);

        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }

}
