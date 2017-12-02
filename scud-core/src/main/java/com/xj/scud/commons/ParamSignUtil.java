package com.xj.scud.commons;


import io.netty.handler.codec.base64.Base64Encoder;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: baichuan - xiajun
 * Date: 2017/12/2 12:47
 */
public class ParamSignUtil {
    private static Map<String, String> MD5_MAP = new ConcurrentHashMap<>();
    private static Map<String, String> CLASS_MAP = new HashMap<>();

    static {
        CLASS_MAP.put(int.class.getName(), Integer.class.getName());
        CLASS_MAP.put(byte.class.getName(), Byte.class.getName());
        CLASS_MAP.put(short.class.getName(), Short.class.getName());
        CLASS_MAP.put(float.class.getName(), Float.class.getName());
        CLASS_MAP.put(double.class.getName(), Double.class.getName());
        CLASS_MAP.put(long.class.getName(), Long.class.getName());
        CLASS_MAP.put(boolean.class.getName(), Boolean.class.getName());
    }

    public static String sign(Object[] params) {
        if (params != null) {
            StringBuilder builder = new StringBuilder("");
            for (Object param : params) {
                String key;
                if (param instanceof Class) {
                    key = ((Class) param).getName();
                } else {
                    key = param.getClass().getName();
                }
                if (CLASS_MAP.containsKey(key)) {
                    builder.append(CLASS_MAP.get(key)).append(":");
                } else {
                    builder.append(key).append(":");
                }
            }
            try {
                return ParamSignUtil.md5(builder.toString());
            } catch (Exception e) {
            }
        }
        return "0";
    }


    private static String md5(String str) throws Exception {
        String md5Str = MD5_MAP.get(str);
        if (md5Str == null) {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            String newstr = Base64.getEncoder().encodeToString(md5.digest(str.getBytes()));
            MD5_MAP.put(str, newstr);
            return newstr;
        }
        return md5Str;
    }
}
