package fr.feavy.network.packets;

/***
 * 
 * This type of packet keep an indentifier of the sender
 * 
 * @author Feavy
 *
 */

public class SecurePacket extends Packet{

	private static String uuid = "";
	
	// Format : s_packetId|uuid|arg0|arg1|...|
	
	private static String senderUUID;
	
	public SecurePacket(PacketID id, String... args) {
		super(id, args);
	}
	
	public SecurePacket(Packet p) {
		super(p, true);
		senderUUID = p.args[0];
	}
	
	public static void setUUID(String uuid) {SecurePacket.uuid = uuid;}
	
	public String getSenderUUID(){return senderUUID;}

	@Override
	protected String[] toStringArray() { //For client
		int l = (args == null) ? 1 : args.length+1;
		String[] rep = new String[l+1];
		rep[0] = id.toString();
		rep[1] = uuid;
		if(args != null)
			for(int i = 2; i < rep.length; i++)
				rep[i] = args[i-2];
		return rep;
	}
	
}
