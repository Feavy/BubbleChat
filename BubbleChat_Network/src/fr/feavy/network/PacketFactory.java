package fr.feavy.network;

import fr.feavy.network.packets.ConnectionReplyPacket;
import fr.feavy.network.packets.Packet;
import fr.feavy.network.packets.PacketID;

public class PacketFactory {
    public Packet createPacket(PacketID id, Object[] args) {
        ConnectionReplyPacket.class.getConstructors()[0].newInstance(args);
    }
}
