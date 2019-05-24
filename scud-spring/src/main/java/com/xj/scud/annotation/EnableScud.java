package com.xj.scud.annotation;

/**
 * @author: xiajun
 * @date: 2019/5/24 上午11:44
 * @since 1.0.0
 */

import com.xj.scud.processor.EnableScudConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Import(EnableScudConfiguration.class)
@Documented
public @interface EnableScud {
}
