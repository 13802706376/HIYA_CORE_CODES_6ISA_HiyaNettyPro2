package com.hiya.nio.netty.dataCommunication;

import com.hiya.nio.netty.common.ClientHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class DataCommunicationInitializer extends  ChannelInitializer<SocketChannel>
{
    // ���þ�������ݴ���ʽ
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception
    {
        //socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
        //socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
        socketChannel.pipeline().addLast(new ReadTimeoutHandler(5)); //5���δ�������ͨ�ţ���Ͽ����ӡ�
        socketChannel.pipeline().addLast(new ClientHandler());  
    }
}
