package org.cuzus.serverstatusbot;

import org.cuzus.serverstatusbot.model.Request;
import org.cuzus.serverstatusbot.model.UTServer;

public class App 
{
    public static void main(String[] args)
    {
        UTServer s1 = new UTServer("Friend", "95.31.33.59", 8507 + 1);
        UTServer s2 = new UTServer("Enemy", "95.31.20.232", 7707 + 1);
        
        UnrealEngine2Server server = new UnrealEngine2Server(s1.getSocketAddress());
        MessagingManager mm = new MessagingManager();
        server.addListener(mm);
        
        UnrealEngine2Server server2 = new UnrealEngine2Server(s2.getSocketAddress());
        MessagingManager mm2 = new MessagingManager();
        server.addListener(mm2);
        
        try
        {
            server.load(Request.RULES, Request.PLAYERS);
            Thread.sleep(5000);

            server2.load(Request.RULES);
            Thread.sleep(5000);
        }
        catch (Exception e) {
			e.printStackTrace();
		}
        finally
        {
			server.close();
		}
    }
}
