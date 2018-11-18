package com.bigdata.WcDemo;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by qian on 2017/3/25.
 *
 * KEYIN , VALUEIN  对应的是mapper输出的KEYOUT， VALUEOUT类型相互对应
 * KEYOUT, VALUEOUT 是自定义的reduce逻辑处理的输出数据类型
 *
 * KEYOUT 是单词
 * VLAUEOUT 是总次数
 *
 */
public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    /**
     * 该函数入参分析
     * <hello, 1><hello, 1>... ...
     *     <banana, 1><banana, 1>... ...
     * 入参key，是一组相同单词kv对应的key
     * 对于出现的相同的key进行处理
     * @param key
     * @param values
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        int count = 0;

        for (IntWritable value : values) {
            count += value.get();
        }

        context.write(key, new IntWritable(count));
    }
}
