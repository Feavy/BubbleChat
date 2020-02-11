package fr.feavy.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Packet {
	protected PacketID id;

	public Packet(PacketID id) {
		this.id = id;
	}

	public PacketID getID(){
		return id;
	}

	public abstract void writeTo(DataOutputStream outputStream) throws IOException;
}
