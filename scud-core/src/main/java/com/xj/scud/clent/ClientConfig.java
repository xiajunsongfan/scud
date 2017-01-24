package com.xj.scud.clent;

import com.xj.scud.network.SerializableEnum;
import com.xj.scud.route.RouteEnum;

/**
 * Author: xiajun
 * Date: 2017/01/03 18:10
 */
public class ClientConfig<T> {
    private String host;
    private int connectTimeout;
    private int timeout;
    private Class<T> serverClass;//服务接口类
    private SerializableEnum type = SerializableEnum.KRYO;
    private int workThreadSize;
    private int nettyBossThreadSize = 1;
    private RouteEnum route = RouteEnum.RANDOM;

    public String getHost() {
        return host;
    }

    public ClientConfig setHost(String host) {
        this.host = host;
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

    public int getNettyBossThreadSize() {
        return nettyBossThreadSize;
    }

    public ClientConfig setNettyBossThreadSize(int nettyBossThreadSize) {
        this.nettyBossThreadSize = nettyBossThreadSize;
        return this;
    }

    public RouteEnum getRoute() {
        return route;
    }

    public ClientConfig setRoute(RouteEnum route) {
        this.route = route;
        return this;
    }
}
