package fr.feavy.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChatMessagePacket extends Packet{
	private String senderUsername, message;

	public ChatMessagePacket(DataInputStream dis) throws IOException {
		this(dis.readUTF(), dis.readUTF());
	}

	public ChatMessagePacket(String senderUsername, String message){
		super(PacketID.CHAT_MESSAGE);
		this.senderUsername = senderUsername;
		this.message = message;
	}

	public String getSenderUsername(){
		return senderUsername;
	}
	
	public String getMessage() {
		return message;
	}

	@Override
	public void writeTo(DataOutputStream outputStream) throws IOException {
		outputStream.writeUTF(senderUsername);
		outputStream.writeUTF(message);
	}
}
