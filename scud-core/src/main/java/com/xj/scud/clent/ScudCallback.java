package com.xj.scud.clent;

/**
 * Author: xiajun
 * Date: 2017/01/03 18:04
 */
public interface ScudCallback<T> {
    void invoke(T value);
}
