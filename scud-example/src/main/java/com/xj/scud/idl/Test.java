package com.xj.scud.idl;

import com.xj.scud.annotation.Async;

import java.util.concurrent.CompletableFuture;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/03 14:55
 */
public interface Test {
    String test2();

    @Async
    User test(String s);

    void test(String s, int i);

    CompletableFuture<User> testAsync(String s);
}
