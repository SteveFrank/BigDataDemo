package com.demo1;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by qian on 2017/4/13.
 */
@Repository("accountDao")
public class AccountDaoImpl extends JdbcDaoSupport implements AccountDao {

    @Resource
    public void setJb(JdbcTemplate jb) {
        super.setJdbcTemplate(jb);
    }

    /**
     * 扣钱
     * @param out
     * @param money
     */
    public void outMoney(String out, double money) {
        String sql = "update t_account set money = money - ? where name = ?";
        this.getJdbcTemplate().update(sql, money, out);
    }

    /**
     * 存款
     * @param in
     * @param money
     */
    public void inMoney(String in, double money) {
        String sql = "update t_account set money = money + ? where name = ?";
        this.getJdbcTemplate().update(sql, money, in);
    }
}
