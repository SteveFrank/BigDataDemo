package com.demo2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by qian on 2017/4/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-demo2.xml")
public class Demo2 {

    @Resource(name="accountService")
    private AccountService accountService;

    @Test
    public void run1() {
        accountService.payMoney("steve frank", "steve", 40000);
    }

}
