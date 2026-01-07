package com.auhyuan.config;

import com.auhyuan.annotation.NettyWebSocketServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(NettyWebscoketProperties.class)
@ConditionalOnClass(ServerBootstrap.class)
public class NettyWebSocketAutoConfig {

    /**
     * 构建连接线程组与工作线程组
     */
    private static final EventLoopGroup bossGroup = new MultiThreadIoEventLoopGroup(1, NioIoHandler.newFactory());
    private static final EventLoopGroup workerGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
    private static final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    @Resource
    private ApplicationContext context;

    @PostConstruct
    void init(){

        IO.println(" .--.       .-.                              .-..-.       .-.  .-.          .--.                                      .-.              .-.          .-.\n" +
                ": .; :      : :                              : `: :      .' `..' `.        : .--'                                    .' `.            .' `.         : :\n" +
                ":    :.-..-.: `-. .-..-..-..-. .--.  ,-.,-.  : .` : .--. `. .'`. .'.-..-.  `. `.  .--. .--. .-..-. .--. .--.    .--. `. .' .--.  .--. `. .' .--.  .-' :\n" +
                ": :: :: :; :: .. :: :; :: :; :' .; ; : ,. :  : :. :' '_.' : :  : : : :; :   _`, :' '_.': ..': `; :' '_.': ..'  `._-.' : : ' .; ; : ..' : : ' '_.'' .; :\n" +
                ":_;:_;`.__.':_;:_;`._. ;`.__.'`.__,_;:_;:_;  :_;:_;`.__.' :_;  :_; `._. ;  `.__.'`.__.':_;  `.__.'`.__.':_;    `.__.' :_; `.__,_;:_;   :_; `.__.'`.__.'\n" +
                "                   .-. :                                            .-. :\n" +
                "                   `._.'                                            `._.'\n");

        Map<String, Object> beans = context.getBeansWithAnnotation(NettyWebSocketServer.class);

        beans.forEach((s,bean)->{

            log.info("Bean start Netty websocket: {}", s);

            Class<?> targetClass = AopUtils.getTargetClass(bean);
            NettyWebSocketServer nettyWebSocketServer = AnnotationUtils.findAnnotation(targetClass, NettyWebSocketServer.class);

            String path = nettyWebSocketServer.path();
            int port = nettyWebSocketServer.port();
            log.info("Netty server path: {}", path);
            log.info("Netty server port: {}", port);
            start(port,path, (WebSocketHandler) bean);
        });

    }

    void start(int port, String path,WebSocketHandler webSocketHandler) {

        executor.submit(() -> {
            try {
                ChannelFuture channelFuture = new ServerBootstrap().group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new WebSocketPipeline(path,webSocketHandler))
                        .bind("0.0.0.0", port)
                        .sync();

                log.info("Netty WebSocket server start success: {}", channelFuture.channel().localAddress());
                //开始阻塞监听，直至服务关闭
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("Netty WebSocket server start error", e);
            } finally {
                stop();
            }
        });
    }

    void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
