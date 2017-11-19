package com.xj.scud.client;

import com.xj.scud.core.network.SerializableEnum;
import com.xj.scud.client.route.RouteEnum;

/**
 * Author: xiajun
 * Date: 2017/01/03 18:10
 */
public class ClientConfig {
    private String host;
    private int connectTimeout;
    private int timeout = 1000;//调用RPC超时
    private Class interfaze;//服务接口类
    private String version;//服务版本
    private SerializableEnum type = SerializableEnum.PROTOBUF;//序列化方式
    private int nettyBossThreadSize = 1;
    private RouteEnum route = RouteEnum.RANDOM;//路由方式
    private boolean useZk = false;//是否使用zookeeper进行集群管理
    private String zkHost;//zookeeper地址

    public String getHost() {
        return host;
    }

    /**
     * 服务端hosts
     *
     * @param host 如：192.168.1.13:5555;192.168.1.14:5555
     * @return
     */
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

    public ClientConfig setType(SerializableEnum type) {
        this.type = type;
        return this;
    }

    public Class getInterfaze() {
        return interfaze;
    }

    public ClientConfig setInterfaze(Class interfaze) {
        this.interfaze = interfaze;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getTimeout() {
        return timeout;
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

    public String getVersion() {
        return version;
    }

    public ClientConfig setVersion(String version) {
        this.version = version;
        return this;
    }

    public boolean isUseZk() {
        return useZk;
    }

    public String getZkHost() {
        return zkHost;
    }

    public ClientConfig setZkHost(String zkHost) {
        this.zkHost = zkHost;
        return this;
    }

    public ClientConfig setUseZk(boolean useZk) {
        this.useZk = useZk;
        return this;
    }
}
