package com.xj.scud.commons;

import com.xj.scud.monitor.MonitorReport;
import com.xj.scud.server.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Author: xiajun
 * Date: 2017/01/22 10:13
 */
public class Config {
    private final static Logger LOGGER = LoggerFactory.getLogger(Config.class);
    private static Properties prop;
    public static boolean METHOD_MONITOR = false;//是否对方法进行性能监控
    public final static String DNS_PREFIX = "/jdns/";

    static {
        init();
    }

    private static void init() {
        InputStream is = null;
        String confName = "scud.properties";
        prop = new Properties();
        try {
            Enumeration<URL> confs = Config.class.getClassLoader().getResources(confName);
            if (confs == null || !confs.hasMoreElements()) {
                confs = Thread.currentThread().getContextClassLoader().getResources(confName);
            }
            if (confs == null || !confs.hasMoreElements()) {
                throw new IllegalStateException("Not find scud config file scud.properties.");
            }
            List<URL> urls = new ArrayList<>(4);
            while (confs.hasMoreElements()) {
                urls.add(confs.nextElement());
            }
            if (urls.size() > 1) {
                LOGGER.warn("there are multiple tesla.properties files in classpath: {}", Arrays.toString(urls.toArray()));
            }
            LOGGER.info("Use scud.properties files in classpath:{}", urls.get(0).toString());
            is = urls.get(0).openStream();
            prop.load(is);
            METHOD_MONITOR = "true".equalsIgnoreCase(prop.getProperty("method.performance.monitor", "false"));
            LOGGER.info("Method performance monitor status {}.", METHOD_MONITOR ? "on" : "off");
            MonitorReport.init();
        } catch (Exception var4) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException var3) {
                    var3.printStackTrace();
                }
            }
        }

    }

    public static String getValue(String key, String defaultValue) {
        return prop.getProperty(key, defaultValue);
    }

    public static String getValue(String key) {
        return prop.getProperty(key);
    }

    public static int getValue(String key, int defaultValue) {
        String v = prop.getProperty(key);
        if (v == null) {
            return defaultValue;
        } else {
            return Integer.parseInt(v);
        }
    }

    public static ServerConfig buildServerConfig() {
        ServerConfig conf = new ServerConfig();
        conf.setConnectTimeout(Integer.parseInt(prop.getProperty("connect.timeout", "1000")));
        conf.setCorePoolSize(Integer.parseInt(prop.getProperty("work.core.pool.size", "8")));
        conf.setIp(prop.getProperty("provider.ip", "0.0.0.0"));
        conf.setNettyWorkPooleSize(Integer.parseInt(prop.getProperty("netty.work.poole.size", "2")));
        conf.setPort(Integer.parseInt(prop.getProperty("provider.port", "6155")));
        conf.setZkHost(prop.getProperty("zk.host"));
        conf.setUseZk(Boolean.valueOf(prop.getProperty("use.zk", "false")));
        return conf;
    }

/*    public static ClientConfig buildClientConfig() {
        ClientConfig conf = new ClientConfig();
        conf.setZkHost(prop.getProperty("zk.host"));
        conf.setUseZk(Boolean.valueOf(prop.getProperty("use.zk", "false")));//
        conf.setHost(prop.getProperty("client.service.host"));
        conf.setConnectTimeout(Integer.parseInt(prop.getProperty("client.connentTimeout", "4000")));
        conf.setTimeout(Integer.parseInt(prop.getProperty("clinet.timeout", "1000")));
        conf.setType(SerializableEnum.valueOf(prop.getProperty("serializable.type", "PROTOBUF")));
        return conf;
    }*/
}
