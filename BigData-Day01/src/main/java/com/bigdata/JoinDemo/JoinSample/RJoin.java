package com.bigdata.JoinDemo.JoinSample;

import com.bigdata.JoinDemo.bean.InfoBean;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.mortbay.log.Log;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by qian on 2017/3/29.
 */
public class RJoin {

    static class RJoinMapper extends Mapper<LongWritable, Text, Text, InfoBean> {

        InfoBean bean = new InfoBean();
        Text k = new Text();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String line = value.toString();

            FileSplit inputSplit = (FileSplit) context.getInputSplit();
            String name = inputSplit.getPath().getName();

            String pid = "";

            /**
             private int order_id;
             private String dateString;
             private String p_id;
             private int amount;
             private String pname;
             private int category_id;
             private float price;
             */
            // 通过文件名判断是哪种数据
            if (name.startsWith("order")) {
                String[] fields = line.split(",");
                Log.info("======================");
                Log.info(fields.toString());
                Log.info("======================");
                pid = fields[2];
                // id date pid amount
                bean.set(Integer.parseInt(fields[0]), fields[1],
                        pid, Integer.parseInt(fields[3]),
                        "", 0, 0f, "0" );
            } else {
                String[] fields = line.split(",");
                // id date pid amount
                pid = fields[0];
                bean.set(0,"",pid,0,
                        fields[1],Integer.parseInt(fields[2]),
                        Float.parseFloat(fields[3]),"1");
            }

            k.set(pid);
            context.write(k, bean);
        }
    }

    static class RJoinReducer extends Reducer<Text, InfoBean, InfoBean, NullWritable> {
        @Override
        protected void reduce(Text pid, Iterable<InfoBean> beans, Context context) throws IOException, InterruptedException {
            InfoBean obean = new InfoBean();
            InfoBean pbean = new InfoBean();
            ArrayList<InfoBean> orderBeans = new ArrayList<InfoBean>();
            for (InfoBean bean: beans) {
                if ("1".equals(bean.getFlag())) {
                    try {
                        BeanUtils.copyProperties(pbean, bean);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        BeanUtils.copyProperties(obean, bean);
                        orderBeans.add(obean);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 拼接两类数据形成最终结果
            for (InfoBean infoBean : orderBeans) {

                infoBean.setPname(pbean.getPname());
                infoBean.setCategory_id(pbean.getCategory_id());
                infoBean.setPrice(pbean.getPrice());

                context.write(infoBean, NullWritable.get());
            }
        }
    }

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
//        configuration.set("mapreduce.framework.name", "local");
        configuration.set("fs.defaultFS", "hdfs://hadoop01:9000");
        configuration.set("HADOOP_USER_NAME", "root");

        if (args == null || args.length == 0) {
            args = new String[2];
            args[0] = "hdfs://hadoop01:9000/orderproduct/input/";
            args[1] = "hdfs://hadoop01:9000/orderproduct/output/";
        }

        Job job = null;
        try {

            job = Job.getInstance(configuration);

            // 指定本程序的jar包所在路径
//            job.setJarByClass(RJoin.class);

            job.setJar("/Users/qian/.m2/repository/org/bigdata/day01/bigdata-day01/1.0-SNAPSHOT/bigdata-day01-1.0-SNAPSHOT.jar");

            // 指定本业务job需要使用的Mapper/Reduce业务类
            job.setMapperClass(RJoin.RJoinMapper.class);
            job.setReducerClass(RJoin.RJoinReducer.class);

            // 指定mapper输出的数据的key-value类型
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(InfoBean.class);

            // 指定自定义数据分析器
//            job.setPartitionerClass(ProvincePartioner.class);
            // 同时指定相应分区数量的reduce
//            job.setNumReduceTasks(5);

            // 指定最终输出的数据的key-value类型
            job.setOutputKeyClass(InfoBean.class);
            job.setOutputValueClass(NullWritable.class);

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

}
