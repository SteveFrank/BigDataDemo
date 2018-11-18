package com.ssh;

import com.ssh.domain.Customer;
import com.ssh.service.CustomerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by qian on 2017/4/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Demo1 {

    @Resource(name="customerService")
    private CustomerService customerService;

    @Test
    public void run1() {
        Customer customer = new Customer();
        customer.setCust_id(2L);
        customer.setCust_name("YQ");
        customerService.update(customer);
    }
}
