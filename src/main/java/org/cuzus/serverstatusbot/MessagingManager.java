package org.cuzus.serverstatusbot;

import java.util.HashMap;

// import org.telegram.telegrambots.*;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;

public class MessagingManager implements ServerListener
{
    @Override
    public void receivedServerInfo()
    {
        System.out.println("MessagingManager receivedServerInfo");
    }

    @Override
    public void receivedPlayers()
    {
        System.out.println("MessagingManager receivedPlayers");
    }

    @Override
    public void receivedRules(HashMap<String, String> rulesKeyValue)
    {
        System.out.println("MessagingManager receivedRules");
        System.out.println("Rules list:");

        for (String key : rulesKeyValue.keySet())
        {
            System.out.println(key + " = " + rulesKeyValue.get(key));
        }
    }

    public void sendMessage(String message)
    {
        // sendTelegram(message);
        sendDiscord(message);
    }

    // public static void sendTelegram(String message)
    // {
    //     ApiContextInitializer.init();

    //     TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

    //     try
    //     {
    //         TelegramBot telegramBot = new TelegramBot();

    //         telegramBotsApi.registerBot(telegramBot);

    //         System.out.println("Bot registred successfully");

    //         SendMessage sm = new SendMessage() // Create a message object object
    //             .setChatId("@cuzus")
    //             .setText(message);

    //         try
    //         {
    //             telegramBot.execute(sm);
    //             System.out.println("Message sent");
    //         }
    //         catch (Exception e) 
    //         {
    //             System.out.println("Error");
    //             e.printStackTrace();
    //         }
    //     }
    //     catch (TelegramApiException e)
    //     {
    //         System.out.println("Error");
    //         e.printStackTrace();
    //     }
    // }
    
    public static void sendDiscord(String message)
    {
        System.out.println("Send to discord");

        try
        {
            String token = "your discord token";
            DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
    
            String channelId = "your discord channel id";
            TextChannel channel = api.getTextChannelById(channelId).get();

            channel.sendMessage(message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}