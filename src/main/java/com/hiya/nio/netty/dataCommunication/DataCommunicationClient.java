package com.hiya.nio.netty.dataCommunication;

import com.hiya.nio.netty.common.Message;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 数据通信
 * 我们需要了解在真正项目中如何使用Netty，大体上对于一些参数设置都是根据服务器性能决定的。我们需要考虑的问题是两台机器（甚至多台）使用Netty怎样进行通信。
 * 
 * 大体上分为三种：
 * ①使用长连接通道不断开的形式进行通信，也就是服务器和客户端的通道一直处于开启状态，如果服务器性能足够好，并且客户端数量也比较上的情况下，推荐这种方式。
 * ②一次性批量提交数据，采用短连接方式。也就是说先把数据保存到本地临时缓存区或者临时表，当达到界值时进行一次性批量提交，又或者根据定时任务轮询提交，
 * 这种情况的弊端是做不到实时性传输，对实时性要求不高的应用程序中推荐使用。
 * ③使用一种特殊的长连接，在某一指定时间段内，服务器与某台客户端没有任何通信，则断开连接。下次连接则是客户端向服务器发送请求的时候，再次建立连接。
 * 
 * 使用Netty实现第三种方式的连接，但是我们需要考虑两个因素： ①如何在超时（即服务器和客户端没有任何通信）后关闭通道？关闭通道后又如何再次建立连接？
 * ②客户端宕机时，我们无需考虑，下次重启客户端之后就可以与服务器建立连接，但服务器宕机时，客户端如何与服务器端通信？
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
            Message message = new Message(i, "pro" + i, "数据信息" + i);
            future.channel().writeAndFlush(message);
            Thread.sleep(4000); // 休眠4秒后再发送数据
        }
        future.channel().closeFuture().sync();
        new Thread(() ->
        {
            try
            {
                System.out.println("子线程开始....");
                ChannelFuture f = dataCommunicationClient.getFuture();
                Message message = new Message(4, "pro" + 4, "数据信息" + 4);
                f.channel().writeAndFlush(message);
                f.channel().closeFuture().sync();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }).start();
        System.out.println("主线程退出......");
    }
}
