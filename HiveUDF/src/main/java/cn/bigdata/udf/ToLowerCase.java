package cn.bigdata.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.HashMap;

/**
 * Created by qian on 2017/4/10.
 */
public class ToLowerCase extends UDF {
    public static HashMap<String, String> provinceMap =
            new HashMap<String, String>();
    static {
        provinceMap.put("136", "beijing");
        provinceMap.put("137", "shanghai");
        provinceMap.put("138", "shenzhen");
    }
    // 必须是public
    public String evaluate(String field) {
        String result = field.toLowerCase();
        return result;
    }

    public String evaluate(int phoneNum) {
        String pn = String.valueOf(phoneNum);
        return provinceMap.get(pn.substring(0,3)) == null ? "hongxing" : provinceMap.get(pn.substring(0,3));
    }
}
