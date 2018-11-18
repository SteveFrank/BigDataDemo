package com.demo3;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by qian on 2017/4/13.
 * Transactional 类上加注解，类中所有方法都具有事务
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountDao accountDao;


    /**
     * 转账方法
     * @param out
     * @param in
     * @param money
     */
    @Transactional
    public void payMoney(final String out, final String in,final double money) {
        System.out.println("Pay Begin ... ...");
        // 先扣钱
        accountDao.outMoney(out, money);
        // 模拟异常
        // int a = 10/0;
        // 再加钱
        accountDao.inMoney(in, money);
        System.out.println("Pay End ... ...");
    }
}
