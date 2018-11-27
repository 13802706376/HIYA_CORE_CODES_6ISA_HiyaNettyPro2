package com.hiya.nio.netty.common;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ServerChannelInitializer extends  ChannelInitializer<SocketChannel>
{
    // ���þ�������ݴ���ʽ
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception
    {
        socketChannel.pipeline().addLast(new ServerHandler());
    }
}
