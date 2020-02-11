package fr.feavy.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerMovePacket extends Packet {
	private String username;
	private float xDest, yDest;

	public PlayerMovePacket(DataInputStream dis) throws IOException {
		this(dis.readUTF(), dis.readFloat(), dis.readFloat());
	}

	public PlayerMovePacket(String username, float xDest, float yDest) {
		super(PacketID.PLAYER_MOVE);
		this.username = username;
		this.xDest = xDest;
		this.yDest = yDest;
	}

	public String getUsername(){
		return username;
	}
	
	public float getXDestination() {
		return xDest;
	}

	public float getYDestination() {
		return yDest;
	}

	@Override
	public void writeTo(DataOutputStream outputStream) throws IOException {
		outputStream.writeUTF(username);
		outputStream.writeFloat(xDest);
		outputStream.writeFloat(yDest);
	}
}
