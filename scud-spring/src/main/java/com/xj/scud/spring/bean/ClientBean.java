package com.xj.scud.spring.bean;

import com.xj.scud.client.ClientConfig;
import com.xj.scud.client.ScudClient;
import com.xj.scud.commons.Config;
import com.xj.scud.core.network.SerializableEnum;
import com.xj.scud.client.route.RouteEnum;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Author: baichuan - xiajun
 * Date: 2017/04/13 17:15
 */
public class ClientBean<T> implements FactoryBean, InitializingBean {
    private String host;
    private Class interfaceClass;
    private String version;
    private int timeout;
    private int connentTimeout;
    private int nettyBossThreadSize;
    private String type;
    private String route;
    private ScudClient<T> client;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Class getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getConnentTimeout() {
        return connentTimeout;
    }

    public void setConnentTimeout(int connentTimeout) {
        this.connentTimeout = connentTimeout;
    }

    public int getNettyBossThreadSize() {
        return nettyBossThreadSize;
    }

    public void setNettyBossThreadSize(int nettyBossThreadSize) {
        this.nettyBossThreadSize = nettyBossThreadSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    @Override
    public T getObject() throws Exception {
        return client.getClient();
    }

    private SerializableEnum determineSerializationType() {
        if (null == type)
            return null;
        if ("protobuf".equals(type)) {
            return SerializableEnum.PROTOBUF;
        } else if ("kryo".equals(type)) {
            return SerializableEnum.KRYO;
        }
        return null;
    }

    private RouteEnum determineRoute() {
        if (null == route)
            return null;
        if ("random".equals(type)) {
            return RouteEnum.RANDOM;
        } else if ("weight".equals(type)) {
            return RouteEnum.WEIGHT;
        } else if ("rotation".equals(type)) {
            return RouteEnum.ROTATION;
        }
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ClientConfig conf = new ClientConfig();
        conf.setInterfaze(interfaceClass).setHost(this.host);
        if (version != null) {
            conf.setVersion(version);
        }
        if (timeout > 0) {
            conf.setTimeout(timeout);
        }
        if (connentTimeout > 0) {
            conf.setConnectTimeout(connentTimeout);
        }
        if (nettyBossThreadSize > 0) {
            conf.setNettyBossThreadSize(nettyBossThreadSize);
        }
        if (type != null) {
            conf.setType(determineSerializationType());
        }
        if (route != null) {
            conf.setRoute(determineRoute());
        }
        conf.setUseZk(Boolean.parseBoolean(Config.getValue("use.zk", "false")));
        conf.setZkHost(Config.getValue("zk.host"));
        client = new ScudClient<>(conf);
        client.init();
    }
}
