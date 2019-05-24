package com.xj.scud.annotation;

import com.xj.scud.client.route.RouteEnum;
import com.xj.scud.core.network.SerializableEnum;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.*;

/**
 * Author: baichuan - xiajun
 * Date: 2017/08/23 14:59
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Autowired
public @interface Client {

    /**
     * 字段名
     *
     * @return
     */
    String name() default "";

    /**
     * 服务版本号
     *
     * @return
     */
    String version() default "";

    /**
     * 单机时使用ip:port
     *
     * @return
     */
    String host() default "";

    /**
     * 连接超时 默认5000毫秒
     *
     * @return
     */
    int connentTimeout() default 5000;

    /**
     * 请求超时 默认5000
     *
     * @return
     */
    int timeout() default 5000;

    /**
     * 延迟加载
     *
     * @return
     */
    boolean lazy() default false;

    SerializableEnum type() default SerializableEnum.PROTOBUF;

    RouteEnum route() default RouteEnum.RANDOM;
}
