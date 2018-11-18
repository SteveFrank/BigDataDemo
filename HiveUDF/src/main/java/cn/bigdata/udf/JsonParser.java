package cn.bigdata.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by qian on 2017/4/11.
 */
public class JsonParser extends UDF {
    public String evaluate(String line_json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            MovieRateBean movieRateBean
                    = objectMapper.readValue(line_json, MovieRateBean.class);
            return movieRateBean.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
