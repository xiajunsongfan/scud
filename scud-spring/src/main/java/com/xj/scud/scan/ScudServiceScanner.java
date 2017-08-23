package com.xj.scud.scan;

import com.xj.scud.annotation.Scud;
import com.xj.scud.server.Provider;
import com.xj.scud.server.ScudServer;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Author: baichuan - xiajun
 * Date: 2017/08/23 15:24
 */
public class ScudServiceScanner implements ApplicationListener<ApplicationContextEvent> {
    @Override
    public void onApplicationEvent(ApplicationContextEvent applicationContextEvent) {
        ApplicationContext context = applicationContextEvent.getApplicationContext();
        Map<String, Object> beans = context.getBeansWithAnnotation(Scud.class);
        Provider providers[] = new Provider[beans.size()];
        int i = 0;
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object bean = entry.getValue();
            Class<?> clazz = AopUtils.getTargetClass(bean);
            Scud scud = clazz.getAnnotation(Scud.class);
            if (scud != null) {
                Class<?>[] interfaces = clazz.getInterfaces();
                if (interfaces == null || interfaces.length < 1) {
                    throw new IllegalArgumentException("Service[" + clazz + "] do not find Interfaces");
                }
                Class interfacz;
                if (StringUtils.hasText(scud.interfaze())) {
                    if (!isInterface(scud.interfaze(), interfaces)) {
                        throw new IllegalArgumentException(scud.interfaze() + "  is not " + clazz.getName() + " interface");
                    }
                    try {
                        interfacz = Class.forName(scud.interfaze());
                    } catch (ClassNotFoundException e) {
                        throw new IllegalArgumentException("interface: " + scud.interfaze() + " can not be found.");
                    }
                } else {
                    interfacz = interfaces[0];
                }
                providers[i++] = new Provider(interfacz, bean, scud.version());
            }
        }
        if (providers.length > 0) {
            ScudServer server = new ScudServer(providers);
            server.start();
        }
    }

    private boolean isInterface(String interfaceStr, Class<?>[] interfaces) {
        for (Class<?> clazz : interfaces) {
            if (interfaceStr.equals(clazz.getName()))
                return true;
        }
        return false;
    }
}
