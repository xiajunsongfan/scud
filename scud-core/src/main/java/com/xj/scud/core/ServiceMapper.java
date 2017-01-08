package com.xj.scud.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/03 19:03
 * 服务接口方法映射
 */
public class ServiceMapper {
    private static Map<String, Method> methodMap;

    /**
     * 将方法进行映射
     * @param clazz 接口class
     */
    public static void init(Class clazz) {
        Method[] methods = clazz.getMethods();
        methodMap = new HashMap<>(methods.length);
        for (Method method : methods) {
            methodMap.put(ProtocolProcesser.buildMethodName(method), method);
        }
    }

    /**
     * 通过key获取Method对象
     * @param key 方法标识
     * @return Method
     */
    public static Method getMethod(String key) {
        return methodMap.get(key);
    }
}
