package fr.feavy.client.network.packet;

import fr.feavy.network.packet.Packet;

public interface PacketHandler<P extends Packet> {
    void onReceive(P packet);
}
