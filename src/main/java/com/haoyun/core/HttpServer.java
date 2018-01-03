package com.haoyun.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletException;

/**
 * Created by songcz on 2017/4/4.
 * Http请求服务
 * 【登录】
 */
public class HttpServer {

    private static final Logger log = LoggerFactory.getLogger(HttpServer.class);

    public void start() throws InterruptedException, ServletException {

        MockServletContext servletContext = new MockServletContext();
        MockServletConfig servletConfig = new MockServletConfig(servletContext);

        AnnotationConfigWebApplicationContext wac = new AnnotationConfigWebApplicationContext();
        wac.setServletContext(servletContext);
        wac.setServletConfig(servletConfig);
        wac.register(AppConfig.class);
        wac.refresh();

        int portNum = Integer.parseInt((String) AppInit.config.get("http_server_port"));

        log.info("登录服务器【HTTP】启动中...端口号： " + portNum + " ...");
        //开启两个事件循环组，事件循环组会自动构建EventLoop，服务器一般开启两个，提高效率
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //Netty的引导类，用于简化开发
            ServerBootstrap b = new ServerBootstrap();
            //把事件循环组加入引导程序
            b.group(bossGroup, workerGroup);
            //开启socket
            b.channel(NioServerSocketChannel.class);
            //加入业务控制器，这里是加入一个初始化类，其中包含了很多业务控制器
            b.childHandler(new HttpServerInitializer(wac, servletConfig));

            // 服务器绑定端口监听&监听服务器关闭监听
            b.bind(portNum).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
