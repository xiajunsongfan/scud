package com.xj.scud.spring.schema;

import com.xj.scud.spring.bean.ServerBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Author: baichuan - xiajun
 * Date: 2017/04/13 15:39
 */
public class ServerParser extends AbstractSingleBeanDefinitionParser {
    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String config = element.getAttribute("config");
        builder.addPropertyValue("config", new RuntimeBeanReference(config));
        List<BeanDefinition> interceptorsBeans = parseInterceptorElements(element, parserContext, builder);
        if (interceptorsBeans != null && interceptorsBeans.size() > 0) {
            builder.addPropertyValue("providers", interceptorsBeans);
        }
    }

    protected List<BeanDefinition> parseInterceptorElements(Element refElement, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String interceptorsElementName = "providers";
        Element interceptorsElement = DomUtils.getChildElementByTagName(refElement, interceptorsElementName);
        if (null != interceptorsElement) {
            List<Element> interceptorElements = DomUtils.getChildElementsByTagName(interceptorsElement, "provider");
            ManagedList<BeanDefinition> list = new ManagedList<>();
            list.setMergeEnabled(true);
            list.setSource(parserContext.getReaderContext().extractSource(interceptorsElement));
            for (Element element : interceptorElements) {
                list.add(parserContext.getDelegate().parseCustomElement(element, builder.getRawBeanDefinition()));
            }
            return list;
        }
        return null;
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ServerBean.class;
    }

    @Override
    protected boolean shouldGenerateId() {
        return true;
    }
}
