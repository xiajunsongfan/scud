package com.xj.scud.spring.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Author: baichuan - xiajun
 * Date: 2017/04/13 12:21
 */
public class ScudNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("serverConfig", new ScudServerConfigParser());
        registerBeanDefinitionParser("provider", new ScudProviderParser());
        registerBeanDefinitionParser("server", new ServerParser());
        registerBeanDefinitionParser("client", new ClientParser());
    }
}
