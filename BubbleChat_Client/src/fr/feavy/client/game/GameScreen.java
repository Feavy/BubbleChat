package fr.feavy.client.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import fr.feavy.client.network.ClientConnection;
import fr.feavy.network.packet.ChatMessagePacket;
import fr.feavy.network.packet.PlayerMovePacket;

public class GameScreen extends JPanel {
	private static GameScreen currentInstance;

	private float lastWidth = 984;

	private Thread refreshThread;

	private Map<String, Player> players; // username, Drawable
	private List<TextBubble> textBubbles;

	private String playerUsername;

	public GameScreen(String playerUsername) {
		currentInstance = this;
		this.playerUsername = playerUsername;
		this.players = new HashMap<>();
		if (Game.isDebug) {
			players.put(playerUsername, new Player(playerUsername, 500, 500, 1));
		}
		this.textBubbles = new ArrayList<>();
		setBackground(Color.WHITE);
		setBorder(new LineBorder(new Color(136, 136, 136)));
		this.refreshThread = new Thread(() -> {
				while (true) {
					repaint();
					try {
						Thread.sleep(1000l / 60l);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
		});
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if (Game.isDebug)
					players.get(playerUsername).moveTo(e.getX(), e.getY());
				else
					ClientConnection.get().sendPacket(new PlayerMovePacket("", (float) (e.getX() / getSize().getWidth()),
							(float) (e.getY() / getSize().getHeight())));
			}
		});
		refreshThread.start();
	}

	public void addPlayer(Player player) {
		players.put(player.getUsername(), player);
	}

	public void removePlayer(String username) {
		players.remove(username);
	}

	public Player getPlayer(String username) {
		return players.get(username);
	}

	public static GameScreen get() {
		return currentInstance;
	}

	public float getLastWidth() {
		return lastWidth;
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		for (Player p : players.values())
			p.newScreenRatio(width / lastWidth);

		for (TextBubble t : textBubbles)
			t.newScreenRatio(width / lastWidth);
		this.lastWidth = width;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHints(rh);
		drawPlayers(g2d);
		drawTextBubbles(g2d);
	}

	public boolean isPlayerOnScreen(String username) {
		return players.containsKey(username);
	}

	private void drawPlayers(Graphics2D g) {
		for (Player p : players.values())
			p.draw(g);
	}

	private void drawTextBubbles(Graphics2D g) {
		for (TextBubble t : textBubbles)
			t.draw(g);
	}

	public void sendMessage(String message){
		System.out.println("Message : "+message);
		ClientConnection.get().sendPacket(new ChatMessagePacket("", message));
		if(Game.isDebug)
			players.get(playerUsername).setMessage(message);
	}

}
