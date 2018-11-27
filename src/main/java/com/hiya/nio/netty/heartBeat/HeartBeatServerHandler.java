package com.hiya.nio.netty.heartBeat;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class HeartBeatServerHandler extends ChannelHandlerAdapter
{
    private static Map<String, String> AUTH_IP_MAP = new HashMap<>();
    private static final String SUCCESS_KEY = "auth_success_key";

    static
    {
        AUTH_IP_MAP.put("192.168.3.176", "1234");
    }

    private boolean auth(ChannelHandlerContext ctx, Object msg)
    {
        String[] rets = ((String) msg).split(",");
        String auth = AUTH_IP_MAP.get(rets[0]);
        if (auth != null && auth.equals(rets[1]))
        {
            ctx.writeAndFlush(SUCCESS_KEY);
            return true;
        } else
        {
            ctx.writeAndFlush("authfailure!").addListener(ChannelFutureListener.CLOSE);
            return false;
        }
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        if (msg instanceof String)
        {
            auth(ctx, msg);
        } else if (msg instanceof RequestInfo)
        {
            RequestInfo info = (RequestInfo) msg;
            System.out.println("----------------------------------------------");
            System.out.println("��ǰ����ip��" + info.getIp());
            System.out.println("��ǰ����cpu�����");
            Map<String, Object> cpuMap = info.getCpuPercMap();
            System.out.println("��ʹ���ʣ�" + cpuMap.get("combined"));
            System.out.println("�û�ʹ���ʣ�" + cpuMap.get("user"));
            System.out.println("ϵͳʹ���ʣ�" + cpuMap.get("sys"));
            System.out.println("�ȴ��ʣ�" + cpuMap.get("wait"));
            System.out.println("�����ʣ�" + cpuMap.get("idle"));
            System.out.println("��ǰ����memory�����");
            
            Map<String, Object> memMap = info.getMemoryMap();
            System.out.println("�ڴ�������" + memMap.get("total"));
            System.out.println("��ǰ�ڴ�ʹ������" + memMap.get("used"));
            System.out.println("��ǰ�ڴ�ʣ������" + memMap.get("free"));
            System.out.println("-----------------------------------------------");
            ctx.writeAndFlush("info received!");
        } 
        else
        {
            ctx.writeAndFlush("connectfailure").addListener(ChannelFutureListener.CLOSE);
        }
    }
}