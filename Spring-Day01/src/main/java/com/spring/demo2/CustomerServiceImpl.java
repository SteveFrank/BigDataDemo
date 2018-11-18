package com.spring.demo2;

/**
 * Created by qian on 2017/4/6.
 */
public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao;

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public void save() {
        System.out.println("save customer ... ... Service");
        customerDao.save();
    }
}
