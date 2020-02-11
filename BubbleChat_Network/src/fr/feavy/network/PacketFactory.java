package fr.feavy.network;

import fr.feavy.network.packet.Packet;
import fr.feavy.network.packet.PacketID;

import java.io.DataInputStream;

public class PacketFactory {
    public static Packet createPacket(PacketID id, DataInputStream dis) {
        try {
            return id.getPacketClass().getConstructor(DataInputStream.class).newInstance(dis);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
