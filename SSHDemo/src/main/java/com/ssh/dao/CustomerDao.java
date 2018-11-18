package com.ssh.dao;

import com.ssh.domain.Customer;

/**
 * Created by qian on 2017/4/13.
 */
public interface CustomerDao {
    public void save(Customer customer);
    public void update(Customer customer);
}
