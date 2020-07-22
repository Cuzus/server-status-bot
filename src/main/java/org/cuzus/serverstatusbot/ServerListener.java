package org.cuzus.serverstatusbot;

import java.util.HashMap;

interface ServerListener 
{
    void receivedServerInfo();
    void receivedPlayers();
    void receivedRules(HashMap<String, String> rulesKeyValue);
};