# netty-websocket-spring-boot-starter
make us simple and quickly build a netty websocket server，usage method same to spring-websocket

### 快速开始

1、**maven引入以下依赖在pom.xml中**

```xml
<!-- netty-websocket author Auhyuan v1.0.2 -->
<dependency>
  <groupId>io.github.auhyuan</groupId>
  <artifactId>netty-websocket-spring-boot-starter</artifactId>
  <version>1.0.2-beta</version>
</dependency>
```

2、创建一个自定义的websocket类 `{customWebSocketName}` 继承 `WebSocketHandler` 抽象类，在自定义的类上方添加`@NettyWebSocketServer()`注解使用如下

```java
@Slf4j
@NettyWebSocketServer(path = "{your netty websoket path}", port="{your netty websocket server port}")
public class MyWebSocket extends WebSocketHandler {

    /**
    * 初始连接时会执行的操作
    */
    @Override
    public void afterConnection(ChannelHandlerContext channelHandlerContext) {
        log.info("连接啦");
    }

    /**
    * 接收到消息时会执行的操作
    */
    @Override
    public void handleTextMessage(ChannelHandlerContext channelHandlerContext, String s) {
        log.info("接收到消息: {}",s);
    }

    /**
    * 断开连接时会执行的操作
    */
    @Override
    public void afterConnectionClose(ChannelHandlerContext channelHandlerContext) {
        log.info("断开连接");
    }
}
```

3、发送消息给指定用户

```java
ChannelHandlerContext channelHandlerContext; //方法参数中携带可以直接使用不需要创建
channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("your want send msg content"));
```

### 启动

启动成功后会展示出以下内容

```bash
 .--.       .-.                              .-..-.       .-.  .-.          .--.                                      .-.              .-.          .-.
: .; :      : :                              : `: :      .' `..' `.        : .--'                                    .' `.            .' `.         : :
:    :.-..-.: `-. .-..-..-..-. .--.  ,-.,-.  : .` : .--. `. .'`. .'.-..-.  `. `.  .--. .--. .-..-. .--. .--.    .--. `. .' .--.  .--. `. .' .--.  .-' :
: :: :: :; :: .. :: :; :: :; :' .; ; : ,. :  : :. :' '_.' : :  : : : :; :   _`, :' '_.': ..': `; :' '_.': ..'  `._-.' : : ' .; ; : ..' : : ' '_.'' .; :
:_;:_;`.__.':_;:_;`._. ;`.__.'`.__,_;:_;:_;  :_;:_;`.__.' :_;  :_; `._. ;  `.__.'`.__.':_;  `.__.'`.__.':_;    `.__.' :_; `.__,_;:_;   :_; `.__.'`.__.'
                   .-. :                                            .-. :
                   `._.'                                            `._.'

2026-01-07T17:58:20.186+08:00  INFO 21040 --- [natty-websocket-spring-boot-starter] [           main] c.a.config.NettyWebSocketAutoConfig      : Bean start Netty websocket: myWebSocket
2026-01-07T17:58:20.186+08:00  INFO 21040 --- [natty-websocket-spring-boot-starter] [           main] c.a.config.NettyWebSocketAutoConfig      : Netty server path: /ws
2026-01-07T17:58:20.186+08:00  INFO 21040 --- [natty-websocket-spring-boot-starter] [           main] c.a.config.NettyWebSocketAutoConfig      : Netty server port: 8081
2026-01-07T17:58:20.232+08:00  INFO 21040 --- [natty-websocket-spring-boot-starter] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat started on port 8080 (http) with context path '/'
2026-01-07T17:58:20.236+08:00  INFO 21040 --- [natty-websocket-spring-boot-starter] [           main] com.practical.PracticalApplication       : Started PracticalApplication in 1.348 seconds (process running for 1.839)
2026-01-07T17:58:20.243+08:00  INFO 21040 --- [natty-websocket-spring-boot-starter] [     virtual-51] c.a.config.NettyWebSocketAutoConfig      : Netty WebSocket server start success: /[0:0:0:0:0:0:0:0]:8081
```

