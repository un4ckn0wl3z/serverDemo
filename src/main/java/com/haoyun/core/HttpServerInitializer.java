package com.haoyun.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;

/**
 * Created by songcz on 2017/3/30.
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final DispatcherServlet dispatcherServlet;


    public HttpServerInitializer(AnnotationConfigWebApplicationContext wac, MockServletConfig servletConfig) throws ServletException {

        this.dispatcherServlet = new DispatcherServlet(wac);
        this.dispatcherServlet.init(servletConfig);
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //开启传输通道，这个通道的作用就是管理控制器，形成一个责任链式管理
        ChannelPipeline pipeline = ch.pipeline();
        
        /* tcp需要解决粘包问题
        // 以("\n")为结尾分割的 解码器
        // 客户端请求时，需以“\r\n”作为结尾；maxFrameLength：8192字节（约定）
        // 如果连续读取到最大长度后没有发现换行符，就会抛出异常，同时忽略掉之前读到的异常码流
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));*/

        // 字符串解码&编码
        pipeline.addLast("http-decoder", new HttpRequestDecoder());
        // 将多个消息转换为单一的FullHttpRequest或者FullHttpResponse
        pipeline.addLast("http-aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("http-encoder", new HttpResponseEncoder());
        // pipeline.addLast("http-chunked", new ChunkedWriteHandler());// 异步传输大文件

        // 加入自定义的Handler
        pipeline.addLast("handler", new HttpServerHandler(this.dispatcherServlet));
        //初始化类一般都是先加入编码解码器来解读传输来的消息，然后加入自定义类来处理业务逻辑
    }
}
