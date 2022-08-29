package com.yuuko.SimpleServer.init;

import com.yuuko.SimpleServer.anno.ScannerPath;
import com.yuuko.SimpleServer.common.BeanScanner;
import com.yuuko.SimpleServer.config.AnnoConfig;
import com.yuuko.SimpleServer.config.RouterConfig;
import com.yuuko.test.App;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class SimpleServerBootstrap {
    public static void run(Class<App> appClass, int port){
        //初始化注解相关
        ScannerPath scannerPath = appClass.getAnnotation(ScannerPath.class);
        try {
            List<String> beanList = BeanScanner.getInstance().getBeanList(scannerPath.value());
            AnnoConfig.getInstance().initAnno(beanList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //初始化网络服务器
        DefaultEventLoop defaultEventLoop = new DefaultEventLoop();
        ChannelFuture channelFuture = new ServerBootstrap()
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 99999)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        channel.pipeline()
                                .addLast(new HttpRequestDecoder())
                                .addLast(new HttpObjectAggregator(Integer.MAX_VALUE))
                                .addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                        if(ctx.channel().isOpen()){
                                            FullHttpRequest request = (FullHttpRequest) msg;
                                            DefaultFullHttpResponse response = RouterConfig.getInstance().routerSelector(request);
                                            ctx.channel().writeAndFlush(response);
                                            ctx.channel().close();
                                        }

                                    }
                                }).addLast( new HttpResponseEncoder());
                    }
                }).bind(port);


        //命令行输出
        String msg = "<=================>Server Start<=================>";
        log.info(msg);
    }
}
