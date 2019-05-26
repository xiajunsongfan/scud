package com.xj.scud.scan;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: xiajun
 * @date: 2019/5/22 下午7:27
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(
        name = {"scud.enabled"},
        matchIfMissing = true
)
public class ScudAutoConfiguration {

    @Bean("serviceScanner")
    public ScudServiceScanner scanner() {
        return new ScudServiceScanner();
    }

    @Bean
    public ClientAnnotationProcessor clientAnnotationProcessor() {
        return new ClientAnnotationProcessor();
    }
}
