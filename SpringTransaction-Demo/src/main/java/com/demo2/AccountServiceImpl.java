package com.demo2;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by qian on 2017/4/13.
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
