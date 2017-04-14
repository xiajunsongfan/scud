package com.xj.scud.spring.schema;

import com.xj.scud.spring.bean.ServerConfigBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Author: baichuan - xiajun
 * Date: 2017/04/13 14:02
 */
public class ScudServerConfigParser extends AbstractSingleBeanDefinitionParser {
    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String port = element.getAttribute("port");
        String connentTimeout = element.getAttribute("connentTimeout");
        String nettyWorkPooleSize = element.getAttribute("nettyWorkPooleSize");
        String corePoolSize = element.getAttribute("corePoolSize");
        String host = element.getAttribute("host");

        builder.addPropertyValue("port", port);
        if (connentTimeout != null && !"".equals(connentTimeout)) {
            builder.addPropertyValue("connentTimeout", connentTimeout);
        }
        if (nettyWorkPooleSize != null && !"".equals(nettyWorkPooleSize)) {
            builder.addPropertyValue("nettyWorkPooleSize", nettyWorkPooleSize);
        }
        if (corePoolSize != null && !"".equals(corePoolSize)) {
            builder.addPropertyValue("corePoolSize", corePoolSize);
        }
        if (host != null && !"".equals(host)) {
            builder.addPropertyValue("host", host);
        }
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ServerConfigBean.class;
    }
}
