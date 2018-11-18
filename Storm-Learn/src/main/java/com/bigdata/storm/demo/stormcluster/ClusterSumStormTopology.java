package com.bigdata.storm.demo.stormcluster;

import java.util.Map;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
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
import org.apache.storm.utils.Utils;

/**
 * @author yangqian
 * @date 2018/11/3
 */
public class ClusterSumStormTopology {

    /**
     * spout 需要继承BaseRichSpout
     *
     * 数据源需要产生数据并且发射
     *
     * 定义数据源
     */
    public static class DataSourceSpout extends BaseRichSpout {

        private SpoutOutputCollector spoutOutputCollector;

        /**
         * 初始化方法，只会被调用一次
         * @param conf 配置参数
         * @param topologyContext 配置拓扑上下文
         * @param spoutOutputCollector 数据发射器
         */
        public void open(
                Map conf, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
            this.spoutOutputCollector = spoutOutputCollector;
        }

        int number = 0;

        /**
         * 会产生数据，在生产上从消息系统获取数据
         * 该方法是一个死循环
         * 用于不停的获取数据
         */
        public void nextTuple() {
            this.spoutOutputCollector.emit(new Values(number ++));
            System.out.println("Spout : " + number);
            // 防止数据产生太快
            Utils.sleep(1000);
        }

        /**
         * 声明输出字段的名称
         * @param outputFieldsDeclarer 输出声明
         */
        public void declareOutputFields(
                OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare(new Fields("num"));
        }
    }


    /**
     * 数据的累积求和 Bolt : 接受数据并且处理
     */
    public static class SumBolt extends BaseRichBolt {
        /**
         * 初始化方法 会被执行一次
         * @param stormConf
         * @param context
         * @param collector
         */
        public void prepare(
                Map stormConf, TopologyContext context, OutputCollector collector) {

        }

        int sum = 0;

        /**
         * 其实也是一个死循环 职责:获取Spout发送过来的数据
         * @param input
         */
        public void execute(
                Tuple input) {
            // Bolt 中获取值可以根据index获取 也可以根据名称获取 建议使用名称获取的方式
            Integer value = input.getIntegerByField("num");
            sum += value;

            System.out.println("Bolt: sum = [" + sum + "]");
        }

        public void declareOutputFields(
                OutputFieldsDeclarer declarer) {

        }
    }

    /**
     * 利用本地模式进行测试
     * @param args
     */
    public static void main(String[] args) {
        // TopologyBuilder 根据Spout和Bolt来构建Topology
        // Storm中任何一个作业都是通过Topology的方式进行提交
        // Topology中需要置顶Spout和Bolt的执行顺序
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("DataSourceSpout", new DataSourceSpout());
        // 设置executor数量
        topologyBuilder.setBolt("SumBolt", new SumBolt(), 2)
                .shuffleGrouping("DataSourceSpout");

        // 将代码提交到Storm集群上运行
        String topoName = ClusterSumStormTopology.class.getSimpleName();
        try {
            Config config = new Config();
            config.setNumWorkers(2);
            config.setNumAckers(0);
            StormSubmitter.submitTopology(topoName, config, topologyBuilder.createTopology());
        } catch (AlreadyAliveException e) {
            e.printStackTrace();
        } catch (InvalidTopologyException e) {
            e.printStackTrace();
        } catch (AuthorizationException e) {
            e.printStackTrace();
        }

    }

}
