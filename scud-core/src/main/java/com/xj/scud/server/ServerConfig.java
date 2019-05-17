package com.xj.scud.server;

/**
 * Author: xiajun
 * Date: 2017/01/04 10:56
 */
public class ServerConfig {
    private String ip = "0.0.0.0";
    private int port;//服务端口
    private int corePoolSize = 8;//服务work线程数
    private int nettyWorkPooleSize = 2;//netty work线程数
    private int connectTimeout;//连接超时时间 毫秒
    private boolean useZk = false;//是否使用zookeeper进行集群管理
    private String zkHost;//zookeeper地址

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

    public boolean isUseZk() {
        return useZk;
    }

    public void setUseZk(boolean useZk) {
        this.useZk = useZk;
    }

    public String getZkHost() {
        return zkHost;
    }

    public void setZkHost(String zkHost) {
        this.zkHost = zkHost;
    }

    @Override
    public String toString() {
        return "ServerConfig{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", corePoolSize=" + corePoolSize +
                ", nettyWorkPooleSize=" + nettyWorkPooleSize +
                ", connectTimeout=" + connectTimeout +
                ", useZk=" + useZk +
                '}';
    }
}
