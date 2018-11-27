package com.hiya.nio.netty.heartBeat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class HeartBeatServerInitializer extends  ChannelInitializer<SocketChannel>
{
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception
    {
        //socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
        //socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
        socketChannel.pipeline().addLast(new HeartBeatServerHandler());
    }
}
