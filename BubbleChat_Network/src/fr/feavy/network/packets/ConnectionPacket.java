package fr.feavy.network.packets;

public class ConnectionPacket extends Packet{
	
	public ConnectionPacket(String username, String adminKey) {
		super(PacketID.CONNECTION, new String[]{username, adminKey});
	}
	
	public ConnectionPacket(String username) {
		super(PacketID.CONNECTION, new String[]{username, " "});
	}
	
	public ConnectionPacket(Packet p) { // Il doit y avoir un moyen plus simple de faire ça
		super(p);
	}
	
	public String getUsername() {return args[0];}
	public String getAdminKey() {return args[1];}
}
