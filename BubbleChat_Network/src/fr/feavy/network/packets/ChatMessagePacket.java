package fr.feavy.network.packets;

public class ChatMessagePacket extends SecurePacket{
	
	public ChatMessagePacket(String senderUsername, String message){
		super(PacketID.CHAT_MESSAGE, senderUsername, message);
	}
	
	public ChatMessagePacket(Packet p){
		super(p);
	}
	
	public String getSenderUsername(){
		return args[0];
	}
	
	public String getMessage(){
		return args[1];
	}
	
}
