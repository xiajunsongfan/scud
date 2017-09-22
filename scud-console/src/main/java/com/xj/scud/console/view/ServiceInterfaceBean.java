package com.xj.scud.console.view;

import java.util.List;

/**
 * Author: baichuan - xiajun
 * Date: 2017/09/22 16:58
 */
public class ServiceInterfaceBean {
    private String name;
    private String version;
    private List<ServerInfoBean> servers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<ServerInfoBean> getServers() {
        return servers;
    }

    public void setServers(List<ServerInfoBean> servers) {
        this.servers = servers;
    }
}
