package com.xj.scud.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: xiajun
 * Date: 2017/01/03 19:03
 * 服务接口方法映射
 */
public class ServiceMapper {
    private static Map<String, Map<String, Method>> serviceMethodMap;
    private static Map<String, Object> servicesMap;

    /**
     * 将方法进行映射
     *
     * @param classes 接口class
     */
    public static void init(Class[] classes, Object[] services) {
        if (classes == null) {
            throw new IllegalArgumentException("Service interface class can't null.");
        }
        if (services == null) {
            throw new IllegalArgumentException("Service interface implement object can't null.");
        }
        if (classes.length != services.length) {
            throw new IllegalArgumentException("Service interface and implementation classes can't corresponding.");
        }
        serviceMethodMap = new HashMap<>(classes.length);
        for (Class clazz : classes) {
            Method[] methods = clazz.getMethods();
            Map<String, Method> methodMap = new HashMap<>(methods.length);
            serviceMethodMap.put(clazz.getName(), methodMap);
            for (Method method : methods) {
                methodMap.put(ProtocolProcesser.buildMethodName(method), method);
            }
        }
        servicesMap = new HashMap<>();
        for (int i = 0; i < services.length; i++) {
            Object service = services[i];
            servicesMap.put(classes[i].getName(), service);
        }
    }

    /**
     * 通过key获取Method对象
     *
     * @param service 服务接口名称
     * @param method  方法标识
     * @return Method
     */
    public static Method getMethod(String service, String method) {
        return serviceMethodMap.get(service).get(method);
    }

    /**
     * 通过接口名称获取接口实现对象
     *
     * @param service 服务名称
     * @return Object
     */
    public static Object getSerivce(String service) {
        return servicesMap.get(service);
    }
}
