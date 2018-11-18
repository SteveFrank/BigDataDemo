package com.demo.logging;

import org.apache.log4j.Logger;
import org.junit.Test;


/**
 * Created by qian on 2017/4/6.
 */
public class Demo1 {

    private Logger logger = Logger.getLogger(Demo1.class);

    @Test
    public void run1() {
        System.out.println("... By Logger");
        logger.info("... By Logger");
    }

}
