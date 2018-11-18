package com.demo1;

import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * Created by qian on 2017/4/13.
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Resource(name="accountDao")
    private AccountDao accountDao;

    @Resource(name="transactionTemplate")
    private TransactionTemplate transactionTemplate;

    /**
     * 转账方法
     * @param out
     * @param in
     * @param money
     */
    public void payMoney(final String out, final String in,final double money) {
        System.out.println("Pay Begin ... ...");
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                // 先扣钱
                accountDao.outMoney(out, money);

                // 模拟异常
                int a = 10/0;

                // 再加钱
                accountDao.inMoney(in, money);
                System.out.println("Pay End ... ...");
            }
        });

    }
}
