package fr.feavy.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.feavy.network.packets.ConnectionPacket;
import fr.feavy.network.packets.Packet;
import fr.feavy.network.packets.PacketID;
import fr.feavy.network.packets.PlayerQuitPacket;
import fr.feavy.server.network.ClientConnection;
import fr.feavy.server.network.Player;

public class Main {

	private static List<ClientConnection> currentConnections = new ArrayList<ClientConnection>();
	private static Map<String, Player> onlinePlayers = new HashMap<String, Player>();

	private static String adminKey;

	private static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	private static boolean hasAdminKeyBeenValidated = false;

	public static void main(String[] args) {

		System.out.println("Server is listening...");

		adminKey = generateAdminKey();

		System.out.println("Clé administrateur : " + adminKey);

		try {
			ServerSocket server = new ServerSocket(12345);

			while (true) {
				ClientConnection cc = new ClientConnection(server.accept());
				System.out.println("New connection with : " + cc.getIP());
				currentConnections.add(cc);
				cc.start();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Server closed.");
	}

	public static void sendGlobalPacket(Packet p) {
		System.out.println("Connection amount : " + currentConnections.size());
		for (ClientConnection connection : currentConnections)
			connection.sendPacket(p);

	}

	public static void removeConnection(ClientConnection connection) {
		System.out.println("Disconnection : " + connection.getIP());
		currentConnections.remove(connection);
		Player p = connection.getPlayer();
		if (p != null) {
			onlinePlayers.remove(connection.getUUID());
			sendGlobalPacket(new PlayerQuitPacket(p.getUsername()));
		}
	}

	public static boolean isValidAdminKey(String key) {
		if (key == adminKey && !hasAdminKeyBeenValidated) {
			hasAdminKeyBeenValidated = true;
			return true;
		} else
			return false;
	}

	public static boolean isUsernameAvailable(String username) {
		for (Player p : onlinePlayers.values())
			if (p.getUsername().equals(username))
				return false;
		return true;

	}

	public static void addOnlinePlayer(String uuid, Player player) {
		onlinePlayers.put(uuid, player);

	}

	public static Collection<Player> getOnlinePlayers() {
		return onlinePlayers.values();

	}

	public static boolean isPlayerOnline(String uuid) {
		return onlinePlayers.containsKey(uuid);

	}

	public static Player getPlayer(String uuid) {
		return onlinePlayers.get(uuid);

	}

	private static String generateAdminKey() {
		Random r = new Random();
		StringBuilder key = new StringBuilder();
		// 0000-0000-0000-0000
		for (int i = 1; i < 17; i++) {
			key.append(alphabet.charAt(r.nextInt(alphabet.length())));
			if (i % 4 == 0 && i != 16)
				key.append('-');
		}
		return key.toString();
	}

}
