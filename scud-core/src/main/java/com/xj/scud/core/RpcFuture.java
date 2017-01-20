package com.xj.scud.core;

import java.util.concurrent.Future;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/20 10:46
 */
public interface RpcFuture<T> extends Future<T> {
    void responseReceived(T response);

    void coypFuture(RpcFuture<T> future);
}
