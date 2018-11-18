package com.demo1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by qian on 2017/4/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Demo1 {

    @Resource(name = "customerDao")
    private CustomerDao customerDao;

    @Test
    public void run1() {
        System.out.println("============");
        customerDao.save();
        customerDao.update();
        System.out.println("============");
    }

}
