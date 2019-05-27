package com.xj.scud;

import com.xj.scud.idl.Test;
import com.xj.scud.idl.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

/**
 * Author: baichuan - xiajun
 * Date: 2017/04/13 16:37
 */

public class SpringTest {
    private static ClassPathXmlApplicationContext context;


    public static void init() {
        context = new ClassPathXmlApplicationContext("classpath:scud.xml");
    }


    public static void main(String[] args) throws InterruptedException {
        init();
        Test t = (Test) context.getBean("client");
        User user = t.test("hello");
        Assert.isTrue("hello".equals(user.getName()), "结果错误");
    }
}
