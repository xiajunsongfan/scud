package com.xj.scud.core;

/**
 * Author: xiajun
 * Date: 2017/01/20 10:58
 */
public interface RpcCallback<T> {
    void success(T value);
    void fail(Throwable error);
}
