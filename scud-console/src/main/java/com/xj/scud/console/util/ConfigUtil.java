package com.xj.scud.console.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Author: baichuan - xiajun
 * Date: 2017/09/22 16:15
 */
public class ConfigUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(ConfigUtil.class);
    private static Properties prop;
    static {
        init();
    }

    private static void init() {
        InputStream is = null;
        String confName = "console.properties";
        prop = new Properties();
        try {
            Enumeration<URL> confs = ConfigUtil.class.getClassLoader().getResources(confName);
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
}
