package fr.feavy.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerQuitPacket extends Packet{
	private String username;

	public PlayerQuitPacket(DataInputStream dis) throws IOException {
		this(dis.readUTF());
	}

	public PlayerQuitPacket(String username){
		super(PacketID.PLAYER_QUIT);
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	@Override
	public void writeTo(DataOutputStream outputStream) throws IOException {
		outputStream.writeUTF(username);
	}
}
