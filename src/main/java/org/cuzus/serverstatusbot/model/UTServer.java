package org.cuzus.serverstatusbot.model;

import java.net.InetSocketAddress;

public class UTServer
{
    private String servername;

    private InetSocketAddress inetAddress;

    public UTServer(String servername, String ip, int port)
    {
        this.servername = servername;
        this.inetAddress = new InetSocketAddress(ip, port);
    }

    public String getServername()
    {
        return this.servername;
    }

    public String getIp()
    {
        return this.inetAddress.getAddress().getHostAddress();
    }

    public int getPort()
    {
        return this.inetAddress.getPort();
    }

    public InetSocketAddress getSocketAddress()
    {
        return this.inetAddress;
    }
}