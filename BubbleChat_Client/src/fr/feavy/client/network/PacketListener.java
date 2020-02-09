package fr.feavy.client.network;

import fr.feavy.network.packets.Packet;

public interface PacketListener {

	void onPacket(Packet packet);
	
}
