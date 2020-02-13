package fr.feavy.client.network.packet;

import fr.feavy.client.network.packet.listener.PlayerListener;
import fr.feavy.network.packet.ChatMessagePacket;
import fr.feavy.network.packet.PlayerJoinPacket;
import fr.feavy.network.packet.PlayerMovePacket;
import fr.feavy.network.packet.PlayerQuitPacket;

public class ClientPacketHandlers extends PacketHandlers {
    public ClientPacketHandlers() {
        setHandler(PlayerJoinPacket.class, PlayerListener::onPlayerJoin);
        setHandler(PlayerMovePacket.class, PlayerListener::onPlayerMove);
        setHandler(PlayerQuitPacket.class, PlayerListener::onPlayerQuit);
        setHandler(ChatMessagePacket.class, PlayerListener::onChatMessage);
    }
}
