package com.hiya.nio.netty.heartBeat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import io.netty.channel.ChannelHandlerContext;

public class HeartBeatClientTask implements Runnable
{
    private final ChannelHandlerContext ctx;
    InetAddress address = null;
    public HeartBeatClientTask(ChannelHandlerContext ctx) throws UnknownHostException 
    {
        this.ctx = ctx;
        address = InetAddress.getLocalHost();
    }

    @Override
    public void run()
    {
        try
        {
            RequestInfo requestInfo = new RequestInfo();
            requestInfo.setIp(address.getHostAddress());
            
            Sigar sigar = new Sigar();
            CpuPerc cpuPerc = sigar.getCpuPerc();
            Map<String, Object> cpuPercMap = new HashMap<>();
            cpuPercMap.put("combined", cpuPerc.getCombined());
            cpuPercMap.put("user", cpuPerc.getUser());
            cpuPercMap.put("sys", cpuPerc.getSys());
            cpuPercMap.put("wait", cpuPerc.getWait());
            cpuPercMap.put("idle", cpuPerc.getIdle());

            Mem mem = sigar.getMem();

            Map<String, Object> memoryMap = new HashMap<>();
            memoryMap.put("total", mem.getTotal() / (1024 * 1024));
            memoryMap.put("used", mem.getUsed() / (1024 * 1024));
            memoryMap.put("free", mem.getFree() / (1024 * 1024));
            requestInfo.setCpuPercMap(cpuPercMap);
            requestInfo.setMemoryMap(memoryMap);
            ctx.writeAndFlush(requestInfo);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     *  
        JAVA web项目报错no sigar-x86-winnt.dll in java.library.path
        
        no sigar-amd64-winnt.dll in java.library.path
org.hyperic.sigar.SigarException: no sigar-amd64-winnt.dll in java.library.path
    at org.hyperic.sigar.Sigar.loadLibrary(Sigar.java:172)
    at org.hyperic.sigar.Sigar.<clinit>(Sigar.java:100)
    
    解决此报错：
   一、需要下载hyperic-sigar-1.6.2.zip，提前hyperic-sigar-1.6.2\sigar-bin\lib下的sigar-x86-winnt.dll，然后放到JAVA JDK安装目录下的bin目录下即可。
   二、需要注意的是：本次调试使用的KDE是myeclipse 8.5，其自带有JDK 1.6，默认情况下Myeclipse 8.5运行不使用外部安装的JDK，需要将sigar-x86-winnt.dll放到Myeclipse 8.5自带JDK环境下的bin目录下，如果项目编译使用手动安装的外部JDK则将sigar-x86-winnt.dll放入外部JDK的bin目录下。
   
     * @param args
     * @throws UnknownHostException
     * @throws SigarException
     */
    public static void main(String[] args) throws UnknownHostException, SigarException
    {
        
        InetAddress address = InetAddress.getLocalHost();
        
        
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setIp(address.getHostAddress());
        
        Sigar sigar = new Sigar();
        CpuPerc cpuPerc = sigar.getCpuPerc();
        Map<String, Object> cpuPercMap = new HashMap<>();
        cpuPercMap.put("combined", cpuPerc.getCombined());
        cpuPercMap.put("user", cpuPerc.getUser());
        cpuPercMap.put("sys", cpuPerc.getSys());
        cpuPercMap.put("wait", cpuPerc.getWait());
        cpuPercMap.put("idle", cpuPerc.getIdle());

        Mem mem = sigar.getMem();

        Map<String, Object> memoryMap = new HashMap<>();
        memoryMap.put("total", mem.getTotal() / (1024 * 1024));
        memoryMap.put("used", mem.getUsed() / (1024 * 1024));
        memoryMap.put("free", mem.getFree() / (1024 * 1024));
        requestInfo.setCpuPercMap(cpuPercMap);
        requestInfo.setMemoryMap(memoryMap);
        
        System.out.println(address.getHostAddress());
        System.out.println(cpuPercMap);
        System.out.println(memoryMap);
    }
}