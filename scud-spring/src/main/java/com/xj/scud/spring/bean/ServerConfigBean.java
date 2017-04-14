package com.xj.scud.spring.bean;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Author: baichuan - xiajun
 * Date: 2017/04/13 14:06
 */
public class ServerConfigBean implements InitializingBean, ApplicationListener<ContextRefreshedEvent> {
    private int port;
    private int connentTimeout;
    private int nettyWorkPooleSize;
    private int corePoolSize;
    private String host;

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getConnentTimeout() {
        return connentTimeout;
    }

    public void setConnentTimeout(int connentTimeout) {
        this.connentTimeout = connentTimeout;
    }

    public int getNettyWorkPooleSize() {
        return nettyWorkPooleSize;
    }

    public void setNettyWorkPooleSize(int nettyWorkPooleSize) {
        this.nettyWorkPooleSize = nettyWorkPooleSize;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "ServerConfigBean{" +
                "port=" + port +
                ", connentTimeout=" + connentTimeout +
                ", nettyWorkPooleSize=" + nettyWorkPooleSize +
                ", corePoolSize=" + corePoolSize +
                ", host='" + host + '\'' +
                '}';
    }
}
