package fr.feavy.network.packets;

import fr.feavy.network.utils.ConversionUtils;

public class PlayerJoinPacket extends Packet {

	public PlayerJoinPacket(String username, float x, float y) {
		super(PacketID.PLAYER_JOIN, username, ConversionUtils.floatToString(x), ConversionUtils.floatToString(y));
	}

	public PlayerJoinPacket(Packet p) {
		super(p);
	}

	public String getUsername() {
		return args[0];
	}

	public float getX() {
		return ConversionUtils.stringToFloat(args[1]);
	}

	public float getY() {
		return ConversionUtils.stringToFloat(args[2]);
	}

}
