package com.bigdata.storm.demo02;

import org.apache.commons.io.FileUtils;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author yangqian
 * @date 2018/11/3
 */
public class LocalWordCountStormTopology {

    public static class DataSource extends BaseRichSpout {

        private SpoutOutputCollector spoutOutputCollector;

        public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
            this.spoutOutputCollector = collector;
        }

        /**
         * 业务:
         * 1) 读取指定目录的文件夹下的数据
         * 2) 把每一行数据发射出去
         */
        public void nextTuple() {
            Collection<File> files
                    = FileUtils.listFiles(
                            new File("/Users/qian/Workspaces/IdeaWorkSpace/BigData/Storm-Learn/src/main/resources/wc"),
                            new String[]{"txt"},
                            true);
            for (File file : files) {
                try {
                    List<String> lines = FileUtils.readLines(file);
                    for (String line : lines) {
                        this.spoutOutputCollector.emit(new Values(line));
                    }
                    // 处理以及处理的数据文件
                    FileUtils.moveFile(file, new File(file.getAbsolutePath() + System.currentTimeMillis()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("wcline"));
        }
    }

    /**
     * 对数据进行分割
     */
    public static class SplitBolt extends BaseRichBolt {

        private OutputCollector outputCollector;

        public void prepare(
                Map stormConf, TopologyContext context, OutputCollector collector) {
            this.outputCollector = collector;
        }

        public void execute(
                Tuple input) {
            String line = input.getStringByField("wcline");
            String[] words = line.split(",");

            for (String word : words) {
                this.outputCollector.emit(new Values(word));
            }
        }

        public void declareOutputFields(
                OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("word"));
        }
    }

    public static class CountBolt extends BaseRichBolt {

        private OutputCollector outputCollector;

        public void prepare(
                Map stormConf, TopologyContext context, OutputCollector collector) {

        }

        Map<String, Integer> map = new HashMap<String, Integer>();

        public void execute(
                Tuple input) {
            String word = input.getStringByField("word");
            Integer count = map.get(word);
            if (count == null) {
                count = 1;
            } else {
                count = count + 1;
            }

            map.put(word, count);

            // 输出
            Set<Map.Entry<String, Integer>> entries = map.entrySet();
            System.out.println("==========================");
            for (Map.Entry<String, Integer> entry : entries) {
                System.out.println(entry);
            }
            System.out.println("==========================");
        }

        public void declareOutputFields(
                OutputFieldsDeclarer declarer) {

        }
    }

    public static void main(String[] args) {
        // TopologyBuilder 根据Spout和Bolt来构建Topology
        // Storm中任何一个作业都是通过Topology的方式进行提交
        // Topology中需要置顶Spout和Bolt的执行顺序
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("DataSourceSpout", new LocalWordCountStormTopology.DataSource());
        topologyBuilder.setBolt("SplitBolt", new LocalWordCountStormTopology.SplitBolt()).shuffleGrouping("DataSourceSpout");
        topologyBuilder.setBolt("CountBolt", new LocalWordCountStormTopology.CountBolt()).shuffleGrouping("SplitBolt");

        // 创建一个本地Storm模式的集群: 本地模式运行 不需要搭建Storm集群
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("LocalSumStormTopology", new Config(), topologyBuilder.createTopology());
    }

}
