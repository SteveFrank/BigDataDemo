package com.demo1;

/**
 * Created by qian on 2017/4/13.
 */
public interface AccountDao {

    public void outMoney(String out, double money);

    public void inMoney(String in, double money);

}
