package fr.feavy.server.network;

import fr.feavy.network.packets.ChatMessagePacket;
import fr.feavy.network.packets.ConnectionPacket;
import fr.feavy.network.packets.Packet;
import fr.feavy.network.packets.PlayerJoinPacket;
import fr.feavy.network.packets.PlayerMovePacket;
import fr.feavy.network.packets.SecurePacket;
import fr.feavy.server.Main;

public class ClientDataProcessor implements Runnable{

	private ClientConnection sender;
	private Packet packet;
	
	public ClientDataProcessor(ClientConnection sender, Packet p){
		this.sender = sender;
		this.packet = p;
	}

	@Override
	public void run() {
		if(packet.getID().isSecurePacket()){
			SecurePacket p = new SecurePacket(packet);
			if(!p.getSenderUUID().equals(sender.getUUID())){
				System.err.println(sender.getIP()+"] Warning : fake packet received !");
				System.err.println("  UUID expected : "+sender.getUUID());
				System.err.println("  UUID received : "+p.getSenderUUID());
				return;
			}
		}
		
		switch(packet.getID()){
		case CONNECTION:
			ConnectionPacket cp = new ConnectionPacket(packet);
			sender.onConnection(cp.getUsername(), cp.getAdminKey());
			break;
		case PLAYER_MOVE:
			PlayerMovePacket pm = new PlayerMovePacket(packet);
			if(Main.isPlayerOnline(pm.getSenderUUID())){
				Main.getPlayer(pm.getSenderUUID()).setX(pm.getXDestination());
				Main.getPlayer(pm.getSenderUUID()).setY(pm.getYDestination());
			}else{
				System.err.println(sender.getIP()+"] Player move strange behavior.");
			}
			Main.sendGlobalPacket(new PlayerMovePacket(sender.getPlayer().getUsername(), pm.getXDestination(), pm.getYDestination()));
			break;
		case CHAT_MESSAGE: 
			ChatMessagePacket cmp = new ChatMessagePacket(packet);
			if(Main.isPlayerOnline(cmp.getSenderUUID())) {
				Main.sendGlobalPacket(new ChatMessagePacket(sender.getPlayer().getUsername(), cmp.getMessage()));
			}
			break;
		default:
		
		}
	}
	
}
