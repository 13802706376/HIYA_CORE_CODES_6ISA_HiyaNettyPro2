package com.hiya.nio.netty.heartBeat;

import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.ScheduledFuture;
public class HeartBeatClientHandler extends ChannelHandlerAdapter
{
    private ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> heartBeat;
    private InetAddress address;
    private static final String SUCCESS_KEY = "auth_success_key";

    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        address = InetAddress.getLocalHost();
        String ip = address.getHostAddress();
        String key = "1234";
        String auth = ip + "," + key;
        ctx.writeAndFlush(auth);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        if (heartBeat != null)
        {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        try
        {
            if (msg instanceof String)
            {
                String data = (String) msg;
                if (SUCCESS_KEY.equals(data))
                {
                    heartBeat = (ScheduledFuture<?>) scheduled.scheduleWithFixedDelay(new HeartBeatClientTask(ctx), 0, 5, TimeUnit.SECONDS);
                    System.out.println(msg);
                } else
                {
                    System.out.println(msg);
                }
            }
        } finally
        {
            ReferenceCountUtil.release(msg);
        }
    }
}