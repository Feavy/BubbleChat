package fr.feavy.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerJoinPacket extends Packet {
	private String username;
	private float x,y;

	public PlayerJoinPacket(DataInputStream dis) throws IOException {
		this(dis.readUTF(), dis.readFloat(), dis.readFloat());
	}

	public PlayerJoinPacket(String username, float x, float y) {
		super(PacketID.PLAYER_JOIN);
		this.username = username;
		this.x = x;
		this.y = y;
	}

	public String getUsername() {
		return username;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	@Override
	public void writeTo(DataOutputStream outputStream) throws IOException {
		outputStream.writeUTF(username);
		outputStream.writeFloat(x);
		outputStream.writeFloat(y);
	}
}
