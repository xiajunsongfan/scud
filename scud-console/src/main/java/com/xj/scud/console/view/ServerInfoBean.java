package com.xj.scud.console.view;

/**
 * Author: baichuan - xiajun
 * Date: 2017/09/22 16:59
 */
public class ServerInfoBean {
    private String ip;
    private String port;
    private int status;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
