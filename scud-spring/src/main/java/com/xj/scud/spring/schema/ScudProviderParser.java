package com.xj.scud.spring.schema;

import com.xj.scud.spring.bean.ProviderBean;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Author: baichuan - xiajun
 * Date: 2017/04/13 15:33
 */
public class ScudProviderParser extends AbstractSingleBeanDefinitionParser {
    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String interfaceName = element.getAttribute("interface");
        String version = element.getAttribute("version");
        String ref = element.getAttribute("ref");

        builder.addPropertyValue("interfaze", interfaceName);
        if (version != null) {
            builder.addPropertyValue("version", version);
        }
        builder.addPropertyValue("ref", new RuntimeBeanReference(ref));
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ProviderBean.class;
    }
}
