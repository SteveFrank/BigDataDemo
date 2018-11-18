package com.ssh.service;

import com.ssh.dao.CustomerDao;
import com.ssh.domain.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by qian on 2017/4/13.
 * 客户的业务
 */
@Service("customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerDao customerDao;

    // 保存客户
    public void save(Customer customer) {
        customerDao.save(customer);
    }

    public void update(Customer customer) {
        customerDao.update(customer);
    }
}
