package fr.feavy.server.network.packet.handler;

import fr.feavy.network.packet.Packet;
import fr.feavy.server.network.ClientConnection;

public interface PacketHandler<P extends Packet> {
    void onReceive(ClientConnection connection, P packet);
}
