package fr.feavy.network.packets;

public class ConnectionReplyPacket extends Packet{
	
	public ConnectionReplyPacket(boolean isSuccess) {
		super(PacketID.CONNECTION_REPLY, new String[]{Boolean.toString(isSuccess), " "});
	}
	
	public ConnectionReplyPacket(String uuid) {
		super(PacketID.CONNECTION_REPLY, new String[]{"true", uuid});
	}

	public ConnectionReplyPacket(Packet p){
		super(p);
	}
	
	public boolean isSuccess(){return Boolean.valueOf(args[0]);}
	
	public String getUUID(){return args[1];}

	public enum ResponseID {
		
		SUCCESS(0), USERNAME_LENGTH(1);
		
		private int id;
		
		ResponseID(int id){
			this.id = id;
		}
		
	}
	
}
