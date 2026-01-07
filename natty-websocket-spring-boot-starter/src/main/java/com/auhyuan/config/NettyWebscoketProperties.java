package com.auhyuan.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auhyuan.websocket")
public record NettyWebscoketProperties(String path, Integer port) {
}
