package com.hiya.nio.netty.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client
{
    public void connect(String host, int port) throws Exception {  
        EventLoopGroup workerGroup = new NioEventLoopGroup();  
  
        try {  
            Bootstrap b = new Bootstrap();  
            b.group(workerGroup);  
            b.channel(NioSocketChannel.class);  
            b.option(ChannelOption.SO_KEEPALIVE, true);  
            b.handler(new ClientChannelInitializer());  
            ChannelFuture f = b.connect(host, port).sync();  
            f.channel().closeFuture().sync();  
        } finally {  
            workerGroup.shutdownGracefully();  
        }  
    }  
    
    public static void main(String[] args) throws Exception 
    {  
        Client client=new Client();  
        client.connect("127.0.0.1", 8379);  
    } 
}
