package com.xj.scud.idl;

import java.util.concurrent.CompletableFuture;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/03 14:55
 */
public interface Test {
    String test2();

    User test(String s);

    void test(String s, int i);

    CompletableFuture<User> testAsync(String s);
}
