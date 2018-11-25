package com.bigdata.storm.drpc;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.LocalDRPC;
import org.apache.storm.drpc.LinearDRPCTopologyBuilder;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * 本地RPC
 * @author yangqian
 * @date 2018-11-25
 */
public class LocalDPRCTopology {

    public static class MyBolt extends BaseRichBolt {

        private OutputCollector outputCollector;

        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
            this.outputCollector = outputCollector;
        }

        public void execute(Tuple tuple) {
            // 请求的ID
            Object requestId = tuple.getValue(0);
            // 请求的参数
            String name = tuple.getString(1);

            // 业务逻辑的处理
            String result = "add User : " + name;
            this.outputCollector.emit(new Values(requestId, result));
        }

        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare(new Fields("id", "result"));
        }
    }

    public static void main(String[] args) {
        LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("addUser");

        builder.addBolt(new MyBolt());

        LocalCluster localCluster = new LocalCluster();
        LocalDRPC localDRPC = new LocalDRPC();
        localCluster.submitTopology("local-drpc", new Config(), builder.createLocalTopology(localDRPC));

        String result = localDRPC.execute("addUser", "zhangsan");
        System.out.println("From client ... " + result);

    }

}
