package com.auhyuan.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface NettyWebSocketServer {

    /**
     * Netty ws 服务端口
     * @return
     */
    int port() default 8081;

    /**
     * Netty Ws 路径
     * @return
     */
    String path();
}
