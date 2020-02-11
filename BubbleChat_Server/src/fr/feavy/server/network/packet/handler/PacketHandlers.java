package fr.feavy.server.network.packet.handler;

import fr.feavy.network.packet.Packet;
import fr.feavy.server.network.ClientConnection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PacketHandlers {
    private static PacketHandlers instance;

    public static void setInstance(PacketHandlers handlers) {
        instance = handlers;
    }

    public static PacketHandlers get() {
        return instance;
    }



    private Map<Class<? extends Packet>, PacketHandler> handlers = new HashMap<>();

    public void setHandler(Class<? extends Packet> packetClass, PacketHandler handler) {
        handlers.put(packetClass, handler);
    }

    public void handle(ClientConnection sender, Packet p) {
        Iterator<Map.Entry<Class<? extends Packet>, PacketHandler>> iterator = handlers.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Class<? extends Packet>, PacketHandler> next = iterator.next();
            if(next.getKey().equals(p.getClass())) {
                next.getValue().onReceive(sender, p);
                return;
            }
        }
    }
}
