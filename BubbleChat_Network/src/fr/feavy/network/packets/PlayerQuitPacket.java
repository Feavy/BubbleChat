package fr.feavy.network.packets;

public class PlayerQuitPacket extends Packet{

	public PlayerQuitPacket(String username){
		super(PacketID.PLAYER_QUIT, username);
	}
	
	public PlayerQuitPacket(Packet p){
		super(p);
	}

	public String getUsername(){
		return args[0];
	}
	
}
