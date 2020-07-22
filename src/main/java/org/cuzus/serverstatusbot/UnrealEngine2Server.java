package org.cuzus.serverstatusbot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.cuzus.serverstatusbot.model.*;

public class UnrealEngine2Server extends UdpServer {
	private int gamePort = -1;
	private String gameType;
	private String skill;

	public String name;
	public String map;
	public int numberOfPlayers = -1;
	public int maximumPlayers;

	protected Players players;
	protected HashMap<String, String> rules;

	private List<ServerListener> listeners = new ArrayList<>();

	public UnrealEngine2Server(InetSocketAddress address) {
		super(address);
	}

	// public static UnrealEngine2Server getInstance(InetSocketAddress address) {
	// 	UnrealEngine2Server server;

	// 	UdpServer udp = servers.get(address);

	// 	if (udp instanceof UnrealEngine2Server) {
	// 		server = (UnrealEngine2Server) udp;
	// 	} else {
	// 		server = new UnrealEngine2Server(address);
	// 	}

	// 	return server;
	// }

	public void addListener(ServerListener toAdd)
	{
        listeners.add(toAdd);
    }

	@Override
	protected void parseData(byte[] data) throws IOException {
		System.out.println("parseData");

		ReplyStream stream = new ReplyStream(data);

		stream.readInt(); // Protocol version

		switch (stream.readByte()) {
			case 0:
				handleInformation(stream);
				break;
			case 1:
				handleRules(stream);
				break;
			case 2:
				handlePlayers(stream);
				break;
		}
	}

	private void handleInformation(ReplyStream stream) {
		System.out.println("Severinfo:");

		stream.readInt(); // Server ID
		stream.readByte(); // IP
		gamePort = stream.readInt(); // Game Port
		System.out.println("gamePort " + gamePort);
		int queryPort = stream.readInt(); // Query Port
		System.out.println("queryPort " + queryPort);
		int serverNameLen = stream.readByte(); // Server Name Length
		System.out.println("serverNameLen " + serverNameLen);
		name = stream.readString(); // Server Name
		System.out.println("name " + name);
		int mapLen = stream.readByte(); // Map Length
		System.out.println("mapLen " + mapLen);
		map = stream.readString(); // Map
		System.out.println("map " + map);
		int gameTypeLen = stream.readByte(); // Game Type Length
		System.out.println("gameTypeLen " + gameTypeLen);
		gameType = stream.readString(); // Game Type
		System.out.println("gameType " + gameType);
		numberOfPlayers = stream.readInt(); // Player Count
		System.out.println("numberOfPlayers " + numberOfPlayers);
		maximumPlayers = stream.readInt(); // Player Max
		System.out.println("maximumPlayers " + maximumPlayers);

		stream.readInt(); // Ping
		stream.readInt(); // Server Flags
		stream.readByte(); // Skill Length

		skill = stream.readString(); // Skill

		for (ServerListener sl : listeners)
		{
			sl.receivedServerInfo();
		}
	}

	private void handleRules(ReplyStream stream) {
		rules = new HashMap<String, String>();

		while (stream.readByte() > 1) 
		{
			String option = stream.readString();
			String value;

			if (stream.readByte() > 0) {
				value = stream.readString();
			} else {
				value = "";
			}

			rules.put(option, value);
		}

		for (ServerListener sl : listeners)
		{
			sl.receivedRules(rules);
		}
	}

	private void handlePlayers(ReplyStream stream) 
	{
		players = new Players();

		System.out.println("Players list:");

		while (true) 
		{
			int id = stream.readInt();

			if (id == 0) {
				break;
			}

			Player player = players.get(id);

			stream.readByte(); // Name Length
			player.name = stream.readString();
			player.ping = stream.readInt();
			player.score = stream.readInt();
			stream.readInt(); // TODO Stats ID

			System.out.println(player.name + "(ping " + player.ping + " score " + player.score + ")");
		}

		for (ServerListener sl : listeners)
		{
			sl.receivedPlayers();
		}
	}

	public void load(Request... requests) {
		List<Request> requestList = Arrays.asList(requests);

		try {
			if (requestList.contains(Request.INFORMATION)) {
				sendData(new byte[] { 0, 0, 0, 0, 0 });
			}

			if (requestList.contains(Request.PLAYERS)) {
				players = null;
				sendData(new byte[] { 0, 0, 0, 0, 2 });
			}

			if (requestList.contains(Request.RULES)) {
				rules = null;
				sendData(new byte[] { 0, 0, 0, 0, 1 });
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getGamePort() {
		return gamePort;
	}

	public String getGameType() {
		return gameType;
	}
}