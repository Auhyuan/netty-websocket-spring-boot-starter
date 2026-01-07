package com.auhyuan.config;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * WebSocket管道
 */
public class WebSocketPipeline extends ChannelInitializer<SocketChannel> {

    private static final Integer MAX_CONTENT_LENGTH = 1024 * 8;

    private final String path;
    private final WebSocketHandler webSocketHandler;

    public WebSocketPipeline(String path,WebSocketHandler webSocketHandler) {
        this.path = path;
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {


        ChannelPipeline pipeline = ch.pipeline();

        //Http编码解码
        pipeline.addLast(new HttpServerCodec());

        //分块请求
        pipeline.addLast(new ChunkedWriteHandler());

        //请求分片
        pipeline.addLast(new HttpObjectAggregator(MAX_CONTENT_LENGTH));

        //转义为Websocket protocol
        pipeline.addLast(new WebSocketServerProtocolHandler(path));

        pipeline.addLast(webSocketHandler);

    }
}
