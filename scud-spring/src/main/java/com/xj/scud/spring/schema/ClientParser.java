package com.xj.scud.spring.schema;

import com.xj.scud.spring.bean.ClientBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Author: baichuan - xiajun
 * Date: 2017/04/13 15:39
 */
public class ClientParser extends AbstractSingleBeanDefinitionParser {
    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String host = element.getAttribute("host");
        String interfaceClass = element.getAttribute("interface");
        String version = element.getAttribute("version");
        String timeout = element.getAttribute("timeout");
        String connentTimeout = element.getAttribute("connentTimeout");
        String nettyBossThreadSize = element.getAttribute("nettyBossThreadSize");
        String type = element.getAttribute("type");
        String route = element.getAttribute("route");
        String lazy = element.getAttribute("lazy-init");

        builder.addPropertyValue("host", host);
        builder.addPropertyValue("interfaceClass", interfaceClass);
        if (version != null && !"".equals(version)) {
            builder.addPropertyValue("version", version);
        }
        if (timeout != null && !"".equals(timeout)) {
            builder.addPropertyValue("timeout", timeout);
        }
        if (connentTimeout != null && !"".equals(connentTimeout)) {
            builder.addPropertyValue("connentTimeout", connentTimeout);
        }
        if (nettyBossThreadSize != null && !"".equals(nettyBossThreadSize)) {
            builder.addPropertyValue("nettyBossThreadSize", nettyBossThreadSize);
        }
        if (type != null && !"".equals(type)) {
            builder.addPropertyValue("type", type);
        }
        if (route != null && !"".equals(route)) {
            builder.addPropertyValue("route", route);
        }
        if (lazy != null && !"".equals(lazy)) {
            builder.getBeanDefinition().setLazyInit(Boolean.parseBoolean(lazy));
        } else {
            builder.getBeanDefinition().setLazyInit(false);
        }
        builder.getBeanDefinition().setScope("singleton");
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ClientBean.class;
    }
}
