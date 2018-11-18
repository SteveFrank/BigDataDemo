package com.demo2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by qian on 2017/4/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Demo2 {

    @Resource(name="jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Test
    public void run1() {
        // Spring提供了自己的内置连接池，不想使用内置的连接池，可以自己进行创建
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql:///spring_jdbc");
//        dataSource.setUsername("root");
//        dataSource.setPassword("admin");

//        // 创建模板类
//        JdbcTemplate template = new JdbcTemplate();
//        // 设置连接池
//        template.setDataSource(dataSource);
        // 完成操作
//        template.update("insert into t_account values(?,?,?)",1,"steve",10000);
    }

    @Test
    public void run2() {
        jdbcTemplate.update("insert into t_account values(?,?,?)",3,"steve frank",6000000);
    }

    public void run3() {

    }
}
