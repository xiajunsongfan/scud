package com.xj.scud.idl;

import com.xj.scud.annotation.Async;
import com.xj.scud.annotation.Scud;

import java.util.concurrent.CompletableFuture;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/03 14:55
 */
public interface Test {
    String test();

    @Async
    User test(String s);

    //CompletableFuture<User> testAsync(String s);
}
