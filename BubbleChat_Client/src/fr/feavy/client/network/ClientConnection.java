package fr.feavy.client.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import fr.feavy.client.network.packet.ClientPacketSenderRunnable;
import fr.feavy.client.network.packet.PacketHandlers;
import fr.feavy.network.PacketFactory;
import fr.feavy.network.io.ConnectionErrorListener;
import fr.feavy.network.io.PacketDataListener;
import fr.feavy.network.io.PacketReceiverRunnable;
import fr.feavy.network.io.PacketSenderRunnable;
import fr.feavy.network.packet.Packet;
import fr.feavy.network.packet.PacketID;

public class ClientConnection implements ConnectionErrorListener, PacketDataListener {
	private static ClientConnection instance;

	private String host;
	private int port;

	private Socket socket;
	private OutputStream outputStream;
	private Thread listener;

	private boolean connected;

	private String uuid;

	public ClientConnection(String host, int port) {
		this.host = host;
		this.port = port;

		try {
			socket = new Socket(host, port);
			outputStream = socket.getOutputStream();
			listener = new Thread(new PacketReceiverRunnable(socket.getInputStream(), this, this));
			listener.start();
			connected = true;
		} catch (IOException e) {
			e.printStackTrace();
			connected = false;
		}
	}

	public static boolean create(String host, int port) {
		if(instance != null)
			throw new RuntimeException("A ClientConnection instance already exists.");
		instance = new ClientConnection(host, port);
		return instance.isConnected();
	}

	public static ClientConnection get() {
		if(instance == null)
			throw new RuntimeException("ClientConnection instance is null.");
		return instance;
	}

	public boolean isConnected() {
		return connected;
	}

	public void sendPacket(Packet packet) {
		// Chiffrement
		new Thread(new ClientPacketSenderRunnable(new DataOutputStream(outputStream), packet, this)).start();
	}

	@Override
	public void processData(int id, DataInputStream inputStream) throws IOException {
		PacketID packetId = PacketID.values()[id];
		Packet packet = PacketFactory.createPacket(packetId, inputStream);
		PacketHandlers.get().handle(packet);
	}

	@Override
	public void onError(Exception e) {
		System.err.println("Une errreur de connexion est survenue.-");
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	public String getUUID() {
		return uuid;
	}
}
