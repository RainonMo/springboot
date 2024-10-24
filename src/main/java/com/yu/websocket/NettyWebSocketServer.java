package com.yu.websocket;

import com.yu.websocket.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class NettyWebSocketServer {

    public static void main(String[] args) throws InterruptedException {
        //创建两个线程组 bossGroup、workerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务端的启动对象，设置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    //设置服务端通道实现类型
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //初始化通道对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //添加http消息的转换，将socket数据流 转换为 HttpRequest， websocket协议添加这个是为了
                            //握手时候使用。 HttpServerCodec 与 HttpObjectAggregator会在第一次http请求后，被移除掉，握手结束了
                            //协议升级就会只用websocket协议了。
                            pipeline.addLast(new HttpServerCodec());
                            //未知
                            pipeline.addLast(new ChunkedWriteHandler());
                            //将拆分的http消息聚合成一个消息。
                            pipeline.addLast(new HttpObjectAggregator(8096));

                            //用户websocket协议的 握手，ping pang处理， close处理，对于二进制或者文件数据，直接交付给下层
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

                            pipeline.addLast(new WebSocketHandler());

                        }
                    });
            //绑定端口号
            ChannelFuture channelFuture = serverBootstrap.bind(9999).sync();
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }


    }

}
