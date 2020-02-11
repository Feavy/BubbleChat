package fr.feavy.server.network.packet.handler;

import fr.feavy.network.packet.ChatMessagePacket;
import fr.feavy.network.packet.ConnectionPacket;
import fr.feavy.network.packet.PlayerMovePacket;
import fr.feavy.server.Main;

public class ServerPacketHandlers extends PacketHandlers {
    public ServerPacketHandlers() {
        PacketHandler<ConnectionPacket> connectionPacketPacketHandler = (sender, packet) -> {
            sender.onConnection(packet.getUsername(), packet.getAdminKey());
        };
        setHandler(ConnectionPacket.class, connectionPacketPacketHandler);

        PacketHandler<PlayerMovePacket> playerMovePacketPacketHandler = (sender, packet) -> {
            if(Main.isPlayerOnline(sender.getUUID())){
                Main.getPlayer(sender.getUUID()).setX(packet.getXDestination());
                Main.getPlayer(sender.getUUID()).setY(packet.getYDestination());
            }else{
                System.err.println(sender.getIP()+"] Offline player has moved.");
            }
            Main.sendGlobalPacket(new PlayerMovePacket(sender.getPlayer().getUsername(), packet.getXDestination(), packet.getYDestination()));
        };
        setHandler(PlayerMovePacket.class, playerMovePacketPacketHandler);

        PacketHandler<ChatMessagePacket> chatMessagePacketPacketHandler = (sender, packet) -> {
            if(Main.isPlayerOnline(sender.getUUID())) {
                Main.sendGlobalPacket(new ChatMessagePacket(sender.getPlayer().getUsername(), packet.getMessage()));
            }
        };
        setHandler(ChatMessagePacket.class, chatMessagePacketPacketHandler);
    }
}
