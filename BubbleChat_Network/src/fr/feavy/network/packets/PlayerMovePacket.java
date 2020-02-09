package fr.feavy.network.packets;

import fr.feavy.network.utils.ConversionUtils;

public class PlayerMovePacket extends SecurePacket {
	
	public PlayerMovePacket(String username, float x, float y) {
		super(PacketID.PLAYER_MOVE, username, ConversionUtils.floatToString(x), ConversionUtils.floatToString(y));
	}

	public PlayerMovePacket(Packet p) {
		super(p);
	}

	public String getUsername(){
		return args[0];
	}
	
	public float getXDestination() {
		return ConversionUtils.stringToFloat(args[1]);
	}

	public float getYDestination() {
		return ConversionUtils.stringToFloat(args[2]);
	}

}
