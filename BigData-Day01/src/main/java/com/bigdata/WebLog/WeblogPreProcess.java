package com.bigdata.WebLog;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WeblogPreProcess {

	static class WeblogPreProcessMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
		Text k = new Text();
		NullWritable v = NullWritable.get();

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			String line = value.toString();
			WebLogBean webLogBean = WebLogParser.parser(line);

			//可以插入一个静态资源过滤（.....）
			/**
			 *
			 * WebLogParser.filterStaticResource(webLogBean);
			 *
			 **/

			if (!webLogBean.isValid())
				return;
			k.set(webLogBean.toString());
			context.write(k, v);

		}

	}

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop01:9000");
		conf.set("HADOOP_USER_NAME", "root");

		Job job = Job.getInstance(conf);

		job.setJar("/Users/qian/.m2/repository/org/bigdata/day01/bigdata-day01/1.0-SNAPSHOT/bigdata-day01-1.0-SNAPSHOT.jar");
		job.setJarByClass(WeblogPreProcess.class);
		job.setMapperClass(WeblogPreProcessMapper.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		FileInputFormat.setInputPaths(job, new Path("hdfs://hadoop01:9000/webLog/input"));
		FileOutputFormat.setOutputPath(job, new Path("hdfs://hadoop01:9000/webLog/output"));

		job.waitForCompletion(true);
	}
}
