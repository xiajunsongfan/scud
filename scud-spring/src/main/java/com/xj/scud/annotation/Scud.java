package com.xj.scud.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Author: baichuan - xiajun
 * Date: 2017/08/23 14:59
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Scud {
    /**
     * 服务版本号
     *
     * @return
     */
    String version() default "";

    /**
     * 服务实现的接口
     *
     * @return
     */
    String interfaze() default "";

    /**
     * 启动时是否立即开启服务
     *
     * @return
     */
    boolean lazy() default false;
}
