package com.xj.scud.scan;

import com.xj.scud.annotation.Client;
import com.xj.scud.client.ClientConfig;
import com.xj.scud.client.ScudClient;
import com.xj.scud.commons.Config;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author: xiajun
 * @date: 2019/5/24 下午8:31
 * @since 1.0.0
 */
public class ClientAnnotationProcessor {
    public void processor(ApplicationContext context) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;

        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();

        // 注册bean
        Map<String, Object> beans = context.getBeansWithAnnotation(Component.class);
        for (Object bean : beans.values()) {
            Class<? extends Object> beanClass = bean.getClass();
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                Client scudClient = field.getAnnotation(Client.class);
                if (scudClient != null) {
                    ClientConfig conf = new ClientConfig();
                    conf.setHost(scudClient.host());
                    conf.setVersion(scudClient.version());
                    conf.setTimeout(scudClient.timeout());
                    conf.setConnectTimeout(scudClient.connentTimeout());
                    conf.setInterfaze(field.getClass());
                    //conf.setNettyBossThreadSize(scudClient.);
                    conf.setType(scudClient.type());
                    conf.setRoute(scudClient.route());
                    conf.setUseZk(Boolean.parseBoolean(Config.getValue("use.zk", "false")));
                    conf.setZkHost(Config.getValue("zk.host"));

                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ClientConfig.class);
                    builder.addConstructorArgValue(conf);
                    builder.getBeanDefinition().setLazyInit(scudClient.lazy());
                    String name = scudClient.name();
                    if (name == null || "".equals(name)) {
                        name = field.getName();
                    }
                    factory.registerBeanDefinition(name, builder.getBeanDefinition());
                }
            }
        }
    }
}
