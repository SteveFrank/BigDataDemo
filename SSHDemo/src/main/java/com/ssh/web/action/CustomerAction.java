package com.ssh.web.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.ssh.domain.Customer;
import com.ssh.service.CustomerService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by qian on 2017/4/13.
 * 强调：以后配置Action必须是多例的
 */
@Controller("customerAction")
@Scope("prototype")
public class CustomerAction extends ActionSupport implements ModelDriven<Customer> {

    @Resource(name="customerService")
    private CustomerService customerService;

    // 模型驱动
    private Customer customer = new Customer();

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getModel() {
        return customer;
    }

    /**
     * 保存客户的方法
     * @return
     */
    public String add() {
        System.out.println("Web action : save customer ... ...");
        customerService.save(customer);
        return NONE;
    }

}
