package com.xj.scud.server;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/04 10:56
 */
public class ServerConfig<T> {
    private String ip = "0.0.0.0";
    private int port;//服务端口
    private int corePoolSize;//服务work线程数
    private int nettyWorkPooleSize = 2;//netty work线程数
    private Class serviceClasses[];//服务接口class
    private T services[];//服务实现类对象
    private int connentTimeout;//连接超时时间 毫秒

    public String getIp() {
        return ip;
    }

    public ServerConfig setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ServerConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public ServerConfig setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    public Class[] getServiceClasses() {
        return serviceClasses;
    }

    /**
     * 服务接口class,需要和服务
     */
    public ServerConfig setServiceClasses(Class... serviceClasses) {
        this.serviceClasses = serviceClasses;
        return this;
    }

    public T[] getServices() {
        return services;
    }

    /**
     * 服务实现类对象
     * 该实现类为可变参数，需要和接口class一一对应
     *
     * @param services
     * @return
     */
    public ServerConfig setServices(T... services) {
        this.services = services;
        return this;
    }

    public int getNettyWorkPooleSize() {
        return nettyWorkPooleSize;
    }

    public ServerConfig setNettyWorkPooleSize(int nettyWorkPooleSize) {
        this.nettyWorkPooleSize = nettyWorkPooleSize;
        return this;
    }

    public int getConnentTimeout() {
        return connentTimeout;
    }

    public ServerConfig setConnentTimeout(int connentTimeout) {
        this.connentTimeout = connentTimeout;
        return this;
    }
}
