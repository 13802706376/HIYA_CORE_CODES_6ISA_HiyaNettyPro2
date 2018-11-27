package com.hiya.nio.netty.heartBeat;

import java.net.InetSocketAddress;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
public class HeartBeatClient
{
    public static void main(String[] args)
    {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup).channel(NioSocketChannel.class).handler(new HeartBeatClientInitializer());
            ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8765)).sync();
            future.channel().closeFuture().sync();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            workerGroup.shutdownGracefully();
        }
    }
}
