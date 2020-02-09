package fr.feavy.server.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import fr.feavy.network.packets.ConnectionReplyPacket;
import fr.feavy.network.packets.Packet;
import fr.feavy.network.packets.PacketID;
import fr.feavy.network.packets.PlayerJoinPacket;
import fr.feavy.network.utils.ByteArray;
import fr.feavy.server.Main;

public class ClientConnection {

	private Socket socket;
	private OutputStream outputStream;
	private Thread listener;

	private boolean connectedToGame;

	private Player player;

	private String uuid;

	public ClientConnection(Socket s) throws IOException {
		connectedToGame = false;
		this.socket = s;
		outputStream = s.getOutputStream();
		listener = new Thread(new Listener(s.getInputStream()));
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
		ByteArray data = packet.toByteArray();
		// Chiffrement
		new Thread(new Sender(outputStream, data)).start();
	}

	private void processData(ByteArray byteArray) {
		String dataStr = byteArray.getBytesAsString();
		String packetIdEncoded = dataStr.split("\\|")[0];
		//System.out.println(getIP()+"] Packet received : " + packetIdEncoded);
		dataStr = dataStr.replaceFirst(packetIdEncoded + "\\|", "");

		String[] data = dataStr.split("\\|");

		for (int i = 0; i < data.length; i++)
			data[i] = URLDecoder.decode(data[i]);
		

		new Thread(new ClientDataProcessor(this, new Packet(PacketID.fromString(URLDecoder.decode(packetIdEncoded)), data))).start();
		;
	}

	class Listener implements Runnable {

		private InputStream inputStream;

		public Listener(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		@Override
		public void run() {
			while (true) {
				ByteArray data = new ByteArray();
				byte[] bytes = new byte[1024];
				int length;
				try {
					boolean eof = false;
					while ((length = inputStream.read(bytes)) != -1) {
						// Déchiffrement
						if (bytes[length - 1] == (byte) 0x00) {
							eof = true;
							length--;
						}
						data.append(bytes, 0, length);
						if (eof)
							break;
					}
					processData(data);
				} catch (IOException e) {
					//e.printStackTrace();
					disconnect();
					return;
				}

			}
		}

	}

	class Sender implements Runnable {

		private OutputStream outputStream;
		private ByteArray data;

		public Sender(OutputStream outputStream, ByteArray data) {
			this.outputStream = outputStream;
			this.data = data;
		}

		@Override
		public void run() {
			int length = data.getLength();
			byte[] bytes = data.getBytes();

			try {
				outputStream.write(bytes, 0, length);
				outputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
				disconnect();
			}
		}

	}

}
