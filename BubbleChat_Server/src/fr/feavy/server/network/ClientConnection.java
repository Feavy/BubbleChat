package fr.feavy.server.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import fr.feavy.network.PacketFactory;
import fr.feavy.network.io.ConnectionErrorListener;
import fr.feavy.network.io.PacketDataListener;
import fr.feavy.network.io.PacketReceiverRunnable;
import fr.feavy.network.io.PacketSenderRunnable;
import fr.feavy.network.packet.*;
import fr.feavy.server.Main;
import fr.feavy.server.network.packet.handler.PacketHandlers;

import javax.xml.crypto.Data;

public class ClientConnection implements ConnectionErrorListener, PacketDataListener {

	private Socket socket;
	private DataOutputStream outputStream;
	private Thread listener;

	private boolean connectedToGame;

	private Player player;

	private String uuid;

	public ClientConnection(Socket s) throws IOException {
		connectedToGame = false;
		this.socket = s;
		outputStream = new DataOutputStream(s.getOutputStream());
		listener = new Thread(new PacketReceiverRunnable(s.getInputStream(), this, this));
	}

	public void start() {
		listener.start();
	}

	public void disconnect() {
		Main.removeConnection(this);
	}

	public Player getPlayer() {
		return player;
	}

	public String getIP() {
		return socket.getInetAddress().getHostAddress();
	}

	public String getUUID() {
		return uuid;
	}

	public void onConnection(String username, String adminKey) {
		if (!Main.isUsernameAvailable(username)) {
			sendPacket(new ConnectionReplyPacket(false));
			return;
		}
		connectedToGame = true;
		uuid = UUID.randomUUID().toString();
		player = new Player(this, username, 0.5f, 0.5f, Main.isValidAdminKey(adminKey));
		Main.addOnlinePlayer(uuid, player);
		sendPacket(new ConnectionReplyPacket(uuid));
		
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				Main.sendGlobalPacket(new PlayerJoinPacket(username, player.getX(), player.getY()));
				Collection<Player> onlinePlayers = Main.getOnlinePlayers();
				for(Player p : onlinePlayers) {
					if(!p.getUsername().equals(username)){
						sendPacket(new PlayerJoinPacket(p.getUsername(), p.getX(), p.getY()));
					}
				}
			}
		}, 100l);
	}

	public void sendPacket(Packet packet) {
		//System.out.println(getIP() + "] Send packet " + packet.getID());
		// Chiffrement
		new Thread(new PacketSenderRunnable(outputStream, packet, this)).start();
	}

	@Override
	public void processData(int id, DataInputStream inputStream) throws IOException {
		PacketID packetId = PacketID.values()[id];
		if(packetId.isSecurePacket()) {
			String senderUUID = inputStream.readUTF();
			if(!senderUUID.equals(getUUID())){
				System.err.println(getIP()+"] Warning : fake packet received !");
				System.err.println("  expected UUID : "+getUUID());
				System.err.println("  received UUID : "+senderUUID);
				return;
			}
		}
		Packet packet = PacketFactory.createPacket(packetId, inputStream);
		PacketHandlers.get().handle(this, packet);
	}

	@Override
	public void onError(Exception e) {
		disconnect();
	}

}
