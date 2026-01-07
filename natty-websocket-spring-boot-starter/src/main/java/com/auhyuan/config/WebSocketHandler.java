package com.auhyuan.config;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public abstract class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    abstract public void afterConnection(ChannelHandlerContext ctx);

    abstract public void handleTextMessage(ChannelHandlerContext ctx, String payload);

    abstract public void afterConnectionClose(ChannelHandlerContext ctx);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        afterConnection(ctx);

        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        handleTextMessage(ctx, msg.text());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        afterConnectionClose(ctx);

        super.channelInactive(ctx);
    }


}
