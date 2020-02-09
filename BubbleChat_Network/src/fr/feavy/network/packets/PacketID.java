package fr.feavy.network.packets;

public enum PacketID {
	CONNECTION("connection"),
	CONNECTION_REPLY("connectionReply"),
	PLAYER_JOIN("playerJoin"),
	PLAYER_QUIT("playerQuit"),
	PLAYER_MOVE("playerMove", true),
	CHAT_MESSAGE("chatMessage", true);

	private String id;
	private boolean secure;

	PacketID(String id, boolean secure) {
		this.id = id;
		this.secure = secure;
	}
	
	PacketID(String id) {
		this.id = id;
		this.secure = false;
	}

	public String toString() {
		return (secure ? "s_" : "") + id;
	}

	public boolean isSecurePacket() {return secure;}
	
	public static PacketID fromString(String idStr) {
		for (PacketID p : PacketID.values()) {
			if (p.toString().equalsIgnoreCase(idStr)) {
				return p;
			}
		}
		throw new IllegalArgumentException("No packet with id " + idStr + " found.");
	}

}
