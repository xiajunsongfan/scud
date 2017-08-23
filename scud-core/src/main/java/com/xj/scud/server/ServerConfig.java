package com.xj.scud.server;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/04 10:56
 */
public class ServerConfig {
    private String ip = "0.0.0.0";
    private int port;//服务端口
    private int corePoolSize = 8;//服务work线程数
    private int nettyWorkPooleSize = 2;//netty work线程数
    private int connectTimeout;//连接超时时间 毫秒

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

    public int getNettyWorkPooleSize() {
        return nettyWorkPooleSize;
    }

    public ServerConfig setNettyWorkPooleSize(int nettyWorkPooleSize) {
        this.nettyWorkPooleSize = nettyWorkPooleSize;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public ServerConfig setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    @Override
    public String toString() {
        return "ServerConfig{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", corePoolSize=" + corePoolSize +
                ", nettyWorkPooleSize=" + nettyWorkPooleSize +
                ", connectTimeout=" + connectTimeout +
                '}';
    }
}
