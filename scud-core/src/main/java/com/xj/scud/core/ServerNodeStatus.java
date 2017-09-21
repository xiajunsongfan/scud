package com.xj.scud.core;

/**
 * Author: baichuan - xiajun
 * Date: 2017/09/21 16:48
 */
public class ServerNodeStatus {
    private int status;//1上线状态， -1下线状态

    public ServerNodeStatus() {
    }

    public ServerNodeStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
