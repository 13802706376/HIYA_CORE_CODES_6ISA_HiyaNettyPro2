package com.hiya.nio.netty.heartBeat;

import java.net.InetSocketAddress;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
public class HeartBeatServer
{
    public HeartBeatServer(int port)
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap();
             bootstrap.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(new HeartBeatServerInitializer())
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .option(ChannelOption.SO_BACKLOG, 1024);
            ChannelFuture future = bootstrap.bind(new InetSocketAddress("127.0.0.1", port)).sync();
            future.channel().closeFuture().sync();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args)
    {
        new HeartBeatServer(8765);
    }
}
