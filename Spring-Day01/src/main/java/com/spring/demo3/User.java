package com.spring.demo3;

import java.util.Map;

/**
 * Created by qian on 2017/4/6.
 * 集合注入方式
 */
public class User {
    private String[] arrs;

    public String[] getArrs() {
        return arrs;
    }
    public void setArrs(String[] arrs) {
        this.arrs = arrs;
    }

    private Map map;

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
