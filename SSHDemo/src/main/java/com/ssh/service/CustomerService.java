package com.ssh.service;

import com.ssh.domain.Customer;

/**
 * Created by qian on 2017/4/13.
 */
public interface CustomerService {
    public void save(Customer customer);
    public void update(Customer customer);
}
