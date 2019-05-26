package com.xj.scud.scan;

import com.xj.scud.annotation.Client;
import com.xj.scud.spring.bean.ClientBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author: xiajun
 * @date: 2019/5/24 下午8:31
 * @since 1.0.0
 */
public class ClientAnnotationProcessor implements BeanClassLoaderAware, BeanFactoryPostProcessor, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(ClientAnnotationProcessor.class);
    private ClassLoader classLoader;
    private ApplicationContext context;
    private Map<String, BeanDefinition> beanDefinitions = new LinkedHashMap();
    private static Set<String> completeBeanName = new HashSet<>();

    public void processor(Field field) {
        Client scudClient = field.getAnnotation(Client.class);
        if (scudClient != null) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ClientBean.class);
            builder.addPropertyValue("host", scudClient.host());
            builder.addPropertyValue("version", scudClient.version());
            builder.addPropertyValue("timeout", scudClient.timeout());
            builder.addPropertyValue("connentTimeout", scudClient.connentTimeout());
            //conf.setNettyBossThreadSize(scudClient.);
            if (scudClient.type() != null) {
                builder.addPropertyValue("type", scudClient.type().getName());
            }
            if (scudClient.route() != null) {
                builder.addPropertyValue("route", scudClient.route().getValue());
            }
            builder.addPropertyValue("interfaceClass", field.getType());
            builder.getBeanDefinition().setLazyInit(scudClient.lazy());
            builder.getBeanDefinition().setScope("singleton");
            String name = field.getType().getSimpleName() + ":" + scudClient.version();
            beanDefinitions.put(name, builder.getBeanDefinition());
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition definition = beanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = definition.getBeanClassName();
            if (beanClassName != null) {
                Class<?> clazz = ClassUtils.resolveClassName(beanClassName, this.classLoader);
                ReflectionUtils.doWithFields(clazz, (field) -> processor(field));
            }
        }
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitions.entrySet()) {
            if (!completeBeanName.contains(entry.getKey()) && !context.containsBean(entry.getKey())) {
                ((BeanDefinitionRegistry) beanFactory).registerBeanDefinition(entry.getKey(), entry.getValue());
                completeBeanName.add(entry.getKey());
                logger.info("registered scud client bean '{} in spring context.", entry.getKey());
            }
        }
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
