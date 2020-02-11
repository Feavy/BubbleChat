package fr.feavy.network.packet;

public enum PacketID {
	CONNECTION(ConnectionPacket.class),
	CONNECTION_REPLY(ConnectionReplyPacket.class),
	PLAYER_JOIN(PlayerJoinPacket.class),
	PLAYER_QUIT(PlayerQuitPacket.class),
	PLAYER_MOVE(PlayerMovePacket.class, true),
	CHAT_MESSAGE(ChatMessagePacket.class, true);

	private Class<? extends Packet> packetClass;
	private boolean secure;

	PacketID(Class<? extends Packet> packetClass, boolean secure) {
		this.packetClass = packetClass;
		this.secure = secure;
	}
	
	PacketID(Class<? extends Packet> packetClass) {
		this.packetClass = packetClass;
		this.secure = false;
	}

	public boolean isSecurePacket() {return secure;}

	public Class<? extends Packet> getPacketClass() {
		return packetClass;
	}
}
