package com.hiya.nio.netty.common;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ClientChannelInitializer extends  ChannelInitializer<SocketChannel>
{
    // ���þ�������ݴ���ʽ
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception
    {
        //socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
        //socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
        
        socketChannel.pipeline().addLast(new ClientHandler());  
    }
}
