package com.haoyun.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

/**
 * Created by songcz on 2017/3/30.
 */
public class HttpClientInitializer extends ChannelInitializer<SocketChannel> {
    
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //开启传输通道，这个通道的作用就是管理控制器，形成一个责任链式管理
        ChannelPipeline pipeline = ch.pipeline();
        
        // 字符串解码&编码
        pipeline.addLast("http-decoder", new HttpResponseDecoder());
        // 将多个消息转换为单一的FullHttpRequest或者FullHttpResponse
        pipeline.addLast("http-aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("http-encoder", new HttpRequestEncoder());
        
        pipeline.addLast("handler", new HttpClientHandler());
    }
}
