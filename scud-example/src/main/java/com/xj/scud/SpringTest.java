package com.xj.scud;

import com.xj.scud.idl.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Author: baichuan - xiajun
 * Date: 2017/04/13 16:37
 */
public class SpringTest {
    private static ClassPathXmlApplicationContext context;


    public static void init() {
        //context = new ClassPathXmlApplicationContext("classpath:scud.xml");//非注解调用
        context = new ClassPathXmlApplicationContext("classpath:scud_annotation.xml");//注解使用方式
    }


    public static void main(String[] args) throws InterruptedException {
        init();
        //Test t = (Test) context.getBean("client");
        //System.out.println(t.test("34556"));
    }
}
