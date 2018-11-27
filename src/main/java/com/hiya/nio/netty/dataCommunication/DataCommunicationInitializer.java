package com.hiya.nio.netty.dataCommunication;

import com.hiya.nio.netty.common.ClientHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class DataCommunicationInitializer extends  ChannelInitializer<SocketChannel>
{
    // 配置具体的数据处理方式
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception
    {
        //socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
        //socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
        socketChannel.pipeline().addLast(new ReadTimeoutHandler(5)); //5秒后未与服务器通信，则断开连接。
        socketChannel.pipeline().addLast(new ClientHandler());  
    }
}
