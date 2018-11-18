package com.ssh.dao;

import com.ssh.domain.Customer;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by qian on 2017/4/13.
 */
public class CustomerDaoImpl extends HibernateDaoSupport implements CustomerDao {

//    @Resource
//    public void setSF(SessionFactory sessionFactory) {
//        /**
//         * 利用Resource的方式完成注解
//         */
//        super.setSessionFactory(sessionFactory);
//    }

    public void save(Customer customer) {
        System.out.println("Dao Begin Save Customer ... ...");
        this.getHibernateTemplate().save(customer);
        System.out.println("Dao End Save Customer ... ...");
        List<Customer> cs = (List<Customer>) this.getHibernateTemplate().find("from Customer");
        for (Customer c : cs) {
            System.out.println(c);
        }
    }

    public void update(Customer customer) {
        this.getHibernateTemplate().update(customer);
    }
}
