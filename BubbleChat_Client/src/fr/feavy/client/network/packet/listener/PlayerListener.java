package fr.feavy.client.network.packet.listener;

import fr.feavy.client.game.GameScreen;
import fr.feavy.client.game.Player;
import fr.feavy.network.packet.ChatMessagePacket;
import fr.feavy.network.packet.PlayerJoinPacket;
import fr.feavy.network.packet.PlayerMovePacket;
import fr.feavy.network.packet.PlayerQuitPacket;

import java.awt.*;

public class PlayerListener {
    public static void onPlayerJoin(PlayerJoinPacket packet) {
        String username = packet.getUsername();
        float x = packet.getX();
        float y = packet.getY();
        Dimension size = GameScreen.get().getSize();
        float lastWidth = GameScreen.get().getLastWidth();
        GameScreen.get().addPlayer(new Player(username, (float) (x * size.getWidth()),
                (float) (y * size.getHeight()), (float) (size.getWidth() / lastWidth)));
    }

    public static void onPlayerQuit(PlayerQuitPacket packet) {
        String username = packet.getUsername();
        GameScreen.get().removePlayer(username);
    }

    public static void onPlayerMove(PlayerMovePacket packet) {
        String username = packet.getUsername();
        if(GameScreen.get().isPlayerOnScreen(username)) {
            Dimension size = GameScreen.get().getSize();
            float destX = (float) (packet.getXDestination() * size.getWidth());
            float destY = (float) (packet.getYDestination() * size.getHeight());
            GameScreen.get().getPlayer(username).moveTo(destX, destY);
        }
    }

    public static void onChatMessage(ChatMessagePacket packet) {
        String username = packet.getSenderUsername();
        GameScreen.get().getPlayer(username).setMessage(packet.getMessage());
    }
}