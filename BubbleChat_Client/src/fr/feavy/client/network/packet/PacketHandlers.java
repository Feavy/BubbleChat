package fr.feavy.client.network.packet;

import fr.feavy.network.packet.Packet;

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

    public <T extends Packet> void setHandler(Class<? extends Packet> packetClass, PacketHandler<T> handler) {
        handlers.put(packetClass, handler);
    }

    public void handle(Packet p) {
        Iterator<Map.Entry<Class<? extends Packet>, PacketHandler>> iterator = handlers.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Class<? extends Packet>, PacketHandler> next = iterator.next();
            if(next.getKey().equals(p.getClass())) {
                next.getValue().onReceive(p);
                return;
            }
        }
    }
}
