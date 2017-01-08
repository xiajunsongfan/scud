package com.xj.scud.clent;

import com.xj.scud.network.SerializableEnum;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/03 18:10
 */
public class ClientConfig<T> {
    private String ip;
    private int port;
    private int connectTimeout;
    private int timeout;
    private Class<T> serverClass;//服务接口类
    private SerializableEnum type = SerializableEnum.KRYO;
    private int workThreadSize;

    public ClientConfig setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public ClientConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public ClientConfig setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public ClientConfig setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public ClientConfig setServerClass(Class<T> serverClass) {
        this.serverClass = serverClass;
        return this;
    }

    public ClientConfig setType(SerializableEnum type) {
        this.type = type;
        return this;
    }

    public int getWorkThreadSize() {
        return workThreadSize;
    }

    public ClientConfig setWorkThreadSize(int workThreadSize) {
        this.workThreadSize = workThreadSize;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public Class getServerClass() {
        return serverClass;
    }

    public SerializableEnum getType() {
        return type;
    }
}
