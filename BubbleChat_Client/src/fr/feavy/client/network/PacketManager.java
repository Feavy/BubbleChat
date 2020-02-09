package fr.feavy.client.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import fr.feavy.network.packets.Packet;
import fr.feavy.network.packets.PacketID;
import fr.feavy.network.utils.ByteArray;

public class PacketManager {

	private static String host;
	private static int port;

	private static Socket socket;
	private static OutputStream outputStream;
	private static Thread listener;

	private static List<PacketListener> packetListeners = new ArrayList<PacketListener>();
	private static List<PacketListener> packetListenersQueue = new ArrayList<PacketListener>();
	private static boolean isAccessing = false;

	private static boolean connected;

	public static boolean startConnection(String host, int port) {
		PacketManager.host = host;
		PacketManager.port = port;

		try {
			socket = new Socket(host, port);
			outputStream = socket.getOutputStream();
			listener = new Thread(new Listener(socket.getInputStream()));
			listener.start();
			connected = true;
			return connected;
		} catch (IOException e) {
			e.printStackTrace();
		}
		connected = false;
		return connected;
	}

	public static boolean isConnected() {
		return connected;
	}

	public static void sendPacket(Packet packet) {
		ByteArray data = packet.toByteArray();
		// Chiffrement
		new Thread(new Sender(outputStream, data)).start();
	}

	private static void processData(ByteArray byteArray) {
		String dataStr = byteArray.getBytesAsString();
		String packetIdEncoded = dataStr.split("\\|")[0];

		System.out.println("Packet received : " + packetIdEncoded);

		dataStr = dataStr.replaceFirst(packetIdEncoded + "\\|", "");

		String[] data = dataStr.split("\\|");

		for (int i = 0; i < data.length; i++)
			data[i] = URLDecoder.decode(data[i]);
		
		Packet p = new Packet(PacketID.fromString(URLDecoder.decode(packetIdEncoded)), data);

		isAccessing = true;
		for (PacketListener listener : packetListeners)
			listener.onPacket(p);
		isAccessing = false;
		packetListeners.addAll(packetListenersQueue);
		packetListenersQueue.clear();

	}

	public static void addPacketListener(PacketListener listener) {
		if (isAccessing)
			packetListenersQueue.add(listener);
		else
			packetListeners.add(listener);
		System.out.println("Adding end");
	}

	public static void removePacketListener(PacketListener listener) {
		packetListeners.remove(listener);
	}

	static class Listener implements Runnable {

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
					e.printStackTrace();
					connected = false;
					return;
				}

			}
		}

	}

	static class Sender implements Runnable {

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
				connected = false;
				e.printStackTrace();
			}
		}

	}

}
