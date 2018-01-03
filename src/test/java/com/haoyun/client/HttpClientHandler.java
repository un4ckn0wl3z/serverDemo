package com.haoyun.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.net.URI;

/**
 * Created by songcz on 2017/3/30.
 */
public class HttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client active ");
        
        URI uri = new URI(ctx.channel().remoteAddress().toString());
        String msg="来自客户端的问候！";
    
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));
        
        // 构建http请求
//        request.headers().set(HttpHeaderNames.HOST, "/manager/test");
//        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        ctx.writeAndFlush(request);
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse response) throws Exception {
    
        ByteBuf buf = response.content();
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String result = new String(bytes,"utf-8");
        System.out.println("Server say : " + result);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client close ");
        super.channelInactive(ctx);
    }
}
