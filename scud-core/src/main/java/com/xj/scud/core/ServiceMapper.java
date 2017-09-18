package com.xj.scud.core;

import com.xj.scud.commons.Config;
import com.xj.scud.commons.NetworkUtil;
import com.xj.scud.core.exception.ScudExecption;
import com.xj.scud.server.Provider;
import com.xj.scud.server.ServerConfig;
import com.xj.zk.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: xiajun
 * Date: 2017/01/03 19:03
 * 服务接口方法映射
 */
public class ServiceMapper {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceMapper.class);
    private static Map<String, Map<String, Method>> serviceMethodMap;
    private static Map<String, Object> servicesMap;

    /**
     * 将方法进行映射
     *
     * @param providers 服务提供者
     */
    public static void init(Provider[] providers) {
        if (providers == null) {
            throw new IllegalArgumentException("Service provider can't null.");
        }
        serviceMethodMap = new HashMap<>(providers.length);
        servicesMap = new HashMap<>(providers.length);
        for (Provider provider : providers) {
            Method[] methods = provider.getInterfaze().getMethods();
            Map<String, Method> methodMap = new HashMap<>(methods.length);
            String serviceName = buildServiceName(provider.getInterfaze().getName(), provider.getVersion());
            serviceMethodMap.put(serviceName, methodMap);
            for (Method method : methods) {
                methodMap.put(ProtocolProcesser.buildMethodName(method), method);
            }
            servicesMap.put(serviceName, provider.getService());
            LOGGER.info("Provider info interfaze:{}.", serviceName);
        }
    }

    public static void createZkNode(Provider[] providers, ZkClient zkClient, int serverPort) {
        for (Provider provider : providers) {
            if (zkClient != null && zkClient.isConnection()) {
                String path = Config.DNS_PREFIX + provider.getInterfaze().getName() + "/" + provider.getVersion();
                if (!zkClient.exists(path)) {
                    zkClient.create(path, CreateMode.PERSISTENT);
                }
                try {
                    zkClient.create(path + "/" + NetworkUtil.getAddress() + ":" + serverPort, new byte[1], true);
                } catch (SocketException e) {
                    LOGGER.error("Get local ip fail.", e);
                }
            }
        }
    }

    /**
     * 通过key获取Method对象
     *
     * @param service 服务接口名称
     * @param method  方法标识
     * @return Method
     */
    public static Method getMethod(String service, String version, String method) {
        Method m = serviceMethodMap.get(buildServiceName(service, version)).get(method);
        if (m == null) {
            throw new NullPointerException("The " + method + " method was not found in the " + service + " service, version:" + version);
        }
        return m;
    }

    /**
     * 通过接口名称获取接口实现对象
     *
     * @param service 服务名称
     * @return Object
     */
    public static Object getSerivce(String service, String version) {
        Object obj = servicesMap.get(buildServiceName(service, version));
        if (obj == null) {
            throw new ScudExecption("Not foud service:" + service + " version:" + version);
        }
        return obj;
    }

    private static String buildServiceName(String service, String version) {
        if (version == null || "".equals(version.trim())) {
            return service;
        }
        return service + ":" + version;
    }
}
