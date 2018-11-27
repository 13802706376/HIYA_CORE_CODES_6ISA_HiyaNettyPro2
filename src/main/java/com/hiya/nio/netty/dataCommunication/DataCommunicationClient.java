package com.hiya.nio.netty.dataCommunication;

import com.hiya.nio.netty.common.Message;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * ����ͨ��
 * ������Ҫ�˽���������Ŀ�����ʹ��Netty�������϶���һЩ�������ö��Ǹ��ݷ��������ܾ����ġ�������Ҫ���ǵ���������̨������������̨��ʹ��Netty��������ͨ�š�
 * 
 * �����Ϸ�Ϊ���֣�
 * ��ʹ�ó�����ͨ�����Ͽ�����ʽ����ͨ�ţ�Ҳ���Ƿ������Ϳͻ��˵�ͨ��һֱ���ڿ���״̬����������������㹻�ã����ҿͻ�������Ҳ�Ƚ��ϵ�����£��Ƽ����ַ�ʽ��
 * ��һ���������ύ���ݣ����ö����ӷ�ʽ��Ҳ����˵�Ȱ����ݱ��浽������ʱ������������ʱ�����ﵽ��ֵʱ����һ���������ύ���ֻ��߸��ݶ�ʱ������ѯ�ύ��
 * ��������ı׶���������ʵʱ�Դ��䣬��ʵʱ��Ҫ�󲻸ߵ�Ӧ�ó������Ƽ�ʹ�á�
 * ��ʹ��һ������ĳ����ӣ���ĳһָ��ʱ����ڣ���������ĳ̨�ͻ���û���κ�ͨ�ţ���Ͽ����ӡ��´��������ǿͻ�������������������ʱ���ٴν������ӡ�
 * 
 * ʹ��Nettyʵ�ֵ����ַ�ʽ�����ӣ�����������Ҫ�����������أ� ������ڳ�ʱ�����������Ϳͻ���û���κ�ͨ�ţ���ر�ͨ�����ر�ͨ����������ٴν������ӣ�
 * �ڿͻ���崻�ʱ���������迼�ǣ��´������ͻ���֮��Ϳ�����������������ӣ���������崻�ʱ���ͻ���������������ͨ�ţ�
 * 
 * @author czj
 *
 */
public class DataCommunicationClient
{
    private static class SingleHodler
    {
        static final DataCommunicationClient DataCommunicationClient = new DataCommunicationClient();
    }

    public static DataCommunicationClient getInstance()
    {
        return SingleHodler.DataCommunicationClient;
    }

    private EventLoopGroup workerGroup;
    private Bootstrap bootstrap;
    private ChannelFuture future;

    private DataCommunicationClient()
    {
        workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup) .channel(NioSocketChannel.class).handler(new DataCommunicationInitializer());
    }

    public void connect()
    {
        try
        {
            future = bootstrap.connect("127.0.0.1", 8765).sync();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public ChannelFuture getFuture()
    {
        if (future == null || !future.channel().isActive())
        {
            this.connect();
        }
        return future;
    }

    public static void main(String[] args) throws InterruptedException
    {
        DataCommunicationClient dataCommunicationClient = getInstance();
        ChannelFuture future = dataCommunicationClient.getFuture();
        for (int i = 1; i <= 3; i++)
        {
            Message message = new Message(i, "pro" + i, "������Ϣ" + i);
            future.channel().writeAndFlush(message);
            Thread.sleep(4000); // ����4����ٷ�������
        }
        future.channel().closeFuture().sync();
        new Thread(() ->
        {
            try
            {
                System.out.println("���߳̿�ʼ....");
                ChannelFuture f = dataCommunicationClient.getFuture();
                Message message = new Message(4, "pro" + 4, "������Ϣ" + 4);
                f.channel().writeAndFlush(message);
                f.channel().closeFuture().sync();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }).start();
        System.out.println("���߳��˳�......");
    }
}
