package com.xj.scud.clent;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/03 18:04
 */
public interface ScudCallback<T> {
    void invoke(T value);
}
