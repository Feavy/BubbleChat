package fr.feavy.client.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import fr.feavy.client.network.PacketListener;
import fr.feavy.client.network.PacketManager;
import fr.feavy.network.packets.ChatMessagePacket;
import fr.feavy.network.packets.Packet;
import fr.feavy.network.packets.PlayerJoinPacket;
import fr.feavy.network.packets.PlayerMovePacket;
import fr.feavy.network.packets.PlayerQuitPacket;
import fr.feavy.network.utils.ConversionUtils;

public class GameScreen extends JPanel implements PacketListener {

	private float lastWidth = 984;

	private Thread refreshThread;

	private Map<String, Player> players; // username, Drawable
	private List<TextBubble> textBubbles;

	private String playerUsername;

	public GameScreen(String playerUsername) {
		this.playerUsername = playerUsername;
		this.players = new HashMap<String, Player>();
		if (Game.isDebug) {
			players.put(playerUsername, new Player(playerUsername, 500, 500, 1));
		}
		this.textBubbles = new ArrayList<TextBubble>();
		setBackground(Color.WHITE);
		setBorder(new LineBorder(new Color(136, 136, 136)));
		this.refreshThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					repaint();
					try {
						Thread.sleep(1000l / 60l);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if (Game.isDebug)
					players.get(playerUsername).moveTo(e.getX(), e.getY());
				else
					PacketManager.sendPacket(new PlayerMovePacket(null, (float) (e.getX() / getSize().getWidth()),
							(float) (e.getY() / getSize().getHeight())));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		refreshThread.start();
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

	private boolean isPlayerOnScreen(String username) {
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
		PacketManager.sendPacket(new ChatMessagePacket(null, message));
		if(Game.isDebug)
			players.get(playerUsername).setMessage(message);
	}
	
	@Override
	public void onPacket(Packet packet) {

		String username;

		switch (packet.getID()) {
		case PLAYER_JOIN:
			PlayerJoinPacket pj = new PlayerJoinPacket(packet);
			username = pj.getUsername();
			float x = pj.getX();
			float y = pj.getY();
			players.put(username, new Player(username, (float) (x * getSize().getWidth()),
					(float) (y * getSize().getHeight()), (float) (getSize().getWidth() / lastWidth)));
			break;
		case PLAYER_QUIT:
			PlayerQuitPacket pqp = new PlayerQuitPacket(packet);
			username = pqp.getUsername();
			if (isPlayerOnScreen(username))
				players.remove(username);
			break;
		case PLAYER_MOVE:
			PlayerMovePacket pm = new PlayerMovePacket(packet);
			username = pm.getUsername();
			if (isPlayerOnScreen(username)) {
				float destX = (float) (pm.getXDestination() * getSize().getWidth());
				float destY = (float) (pm.getYDestination() * getSize().getHeight());
				players.get(pm.getUsername()).moveTo(destX, destY);
			}
			break;
		case CHAT_MESSAGE:
			ChatMessagePacket cmp = new ChatMessagePacket(packet);
			username = cmp.getSenderUsername();
			players.get(username).setMessage(cmp.getMessage());
			break;
		default:
		}

	}

}
