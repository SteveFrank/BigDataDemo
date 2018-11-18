package com.demo3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by qian on 2017/4/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Demo3 {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Test
    public void run1() {

        // 单个数据查询
        Account account = jdbcTemplate.queryForObject("select * from t_account where id = ?", new BeanMapper(), 1);
        System.out.println(account);

        // 集合数据查询
        List<Account> accountList = jdbcTemplate.query("select * from t_account", new BeanMapper());
        System.out.println(accountList);

    }

}

/**
 * 自己手动封装数据（一行一行地进行封装）
 */
class BeanMapper implements RowMapper<Account> {

    public Account mapRow(ResultSet resultSet, int i)
            throws SQLException {

        Account account = new Account();
        account.setId(resultSet.getInt("id"));
        account.setName(resultSet.getString("name"));
        account.setMoney(resultSet.getDouble("money"));

        return account;
    }

}
