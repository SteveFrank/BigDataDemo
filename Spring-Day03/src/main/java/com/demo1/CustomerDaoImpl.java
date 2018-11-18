package com.demo1;

import org.springframework.stereotype.Repository;

/**
 * Created by qian on 2017/4/13.
 */
@Repository("customerDao")
public class CustomerDaoImpl implements CustomerDao {

    public void save() {
        System.out.println("save ... ...");
    }

    public void update() {
        System.out.println("update ... ...");
    }
}
