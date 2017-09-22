package com.xj.scud.console.view;

import java.util.List;

/**
 * Author: baichuan - xiajun
 * Date: 2017/09/22 16:20
 */
public class AppBean {
    private String name;
    private List<ServiceInterfaceBean> interfaces;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ServiceInterfaceBean> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<ServiceInterfaceBean> interfaces) {
        this.interfaces = interfaces;
    }
}
