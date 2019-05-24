package com.xj.scud.scan;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Author: baichuan - xiajun
 * Date: 2017/08/23 15:24
 */
public class ScudServiceScanner implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent applicationContextEvent) {
        ApplicationContext context = applicationContextEvent.getApplicationContext();
        ServiceAnnotationProcessor serviceAnnotationProcessor = new ServiceAnnotationProcessor();
        serviceAnnotationProcessor.processor(context);
        ClientAnnotationProcessor clientAnnotationProcessor = new ClientAnnotationProcessor();
        clientAnnotationProcessor.processor(context);
    }
}
