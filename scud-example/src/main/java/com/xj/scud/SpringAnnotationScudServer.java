package com.xj.scud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: xiajun
 * @date: 2019/5/21 下午9:34
 * @since 1.0.0
 * spring注解服务端
 */
@SpringBootApplication(scanBasePackages = "com.xj.scud")
public class SpringAnnotationScudServer {

    public static void main(String[] args) {
        SpringApplication.run(SpringAnnotationScudServer.class);
    }
}
