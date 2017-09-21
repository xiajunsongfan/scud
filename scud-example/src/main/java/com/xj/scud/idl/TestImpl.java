package com.xj.scud.idl;

import com.xj.scud.annotation.Scud;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author: xiajun
 * Date: 2017/01/03 14:45
 */
@Scud(version = "1.0.0")
public class TestImpl implements Test {
    private Random random = new Random();

    public String test2() {
        return "hello test";
    }

    public User test(String s) {
        User u = new User();
        u.setAge(12);
        u.setName(s);
        List<String> likes = new ArrayList<>();
        likes.add("美食");
        u.setLikes(likes);
        try {
            Thread.sleep(random.nextInt(100));
        } catch (InterruptedException e) {

        }
        System.out.println(u.toString());
        return u;
    }

    public void test(String s, int i) {
        System.out.println(s + "------" + i);
    }
}
