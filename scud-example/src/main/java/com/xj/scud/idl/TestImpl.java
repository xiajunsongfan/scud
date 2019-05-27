package com.xj.scud.idl;

import com.xj.scud.annotation.Async;
import com.xj.scud.annotation.Scud;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * Author: xiajun
 * Date: 2017/01/03 14:45
 */
@Scud(version = "1.0.1")
public class TestImpl implements Test {
    private Random random = new Random();

    public String test() {
        return "hello test";
    }
    @Async
    public User test(String name) {
        User u = new User();
        u.setAge(12);
        u.setName(name);
        List<String> likes = new ArrayList<>();
        likes.add("美食");
        u.setLikes(likes);
        try {
            Thread.sleep(random.nextInt(1500));
        } catch (InterruptedException e) {

        }
        System.out.println(u.toString());
        return u;
    }
}
