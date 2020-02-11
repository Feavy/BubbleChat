package fr.feavy.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ConnectionPacket extends Packet{
	private String username, adminKey;

	public ConnectionPacket(DataInputStream dis) throws IOException {
		this(dis.readUTF(), dis.readUTF());
	}

	public ConnectionPacket(String username, String adminKey) {
		super(PacketID.CONNECTION);
		this.username = username;
		this.adminKey = adminKey;
	}
	
	public ConnectionPacket(String username) {
		this(username, " ");
	}
	
	public String getUsername() {return username;}
	public String getAdminKey() {return adminKey;}

	@Override
	public void writeTo(DataOutputStream outputStream) throws IOException {
		outputStream.writeUTF(username);
		outputStream.writeUTF(adminKey);
	}
}
