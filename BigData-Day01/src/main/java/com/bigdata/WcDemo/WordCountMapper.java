package com.bigdata.WcDemo;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by qian on 2017/3/25.
 *
 * Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
 *
 *     KEYIN : 默认情况下，是MR框架所读到的一行文本的起始偏移量 Long
 *             使用具有序列化功能的 LongWritable
 *     VALUEIN : 默认情况下，是MR框架所读到的文本内容 String
 *             使用具有序列化功能的 Text
 *
 *     KEYOUT : 是用户自定义逻辑处理完成之后输出的key，
 *              在本处是单词，String
 *     VALUEOUT : 使用户自定义逻辑处理完之后输出数据中的value，
 *              在此处是单词数，Integer
 *
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    /**
     * MAP 阶段的自定义逻辑在此处进行实现
     * @param key
     * @param value
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        // 讲maptask传递过来的文本内容先转化为String形式
        String line = value.toString();
        // 根据空格讲这一行且分为单词
        String[] words = line.split(" ");

        // 讲单词输出为<单词, 1>
        for (String word: words) {
            // 讲单词作为key，将次数作为value，以方便后续的数据继续进行分发，可以进行单词分发，
            // 以便于相同的单词会进入相同的reduce Map中
            context.write(new Text(word), new IntWritable(1));
        }
    }
}
