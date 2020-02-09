package fr.feavy.client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import fr.feavy.client.game.Game;
import fr.feavy.client.game.GameInterface;
import fr.feavy.client.network.PacketManager;
import fr.feavy.network.packets.SecurePacket;

public class MainWindow extends JFrame {

	private static MainWindow instance;
	private JPanel contentPane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		
		instance = this;
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String ip = JOptionPane.showInputDialog("IP :");
		
		if(!Game.isDebug && !PacketManager.startConnection(ip, 12345))
				JOptionPane.showMessageDialog(this, "Erreur : le serveur est actuellement indisponible.", "Erreur", JOptionPane.ERROR_MESSAGE);
		
		setResizable(true);
		setTitle("Bubble Chat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 750);
		setLocationRelativeTo(null);
		if(!Game.isDebug)
			contentPane = new ConnectionPanel(this);
		else
			contentPane = new GameInterface("undefined");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
	}
	
	public static Font getGameFont(int size, boolean bold){
		Font f = new Font("Arial", (bold)?Font.BOLD:0, size);
		return f;
	}
	
	public static MainWindow getInstance(){
		return instance;
	}
	
	public static void connected(String username, String uniqueID){
		System.out.println("Connecté avec succès");
		System.out.println("  Username : "+username);
		System.out.println("  UUID     : "+uniqueID);
		instance.setTitle("Bubble Chat - "+username);
		SecurePacket.setUUID(uniqueID);
		instance.setupGameInterface(username);
	}
	
	private void setupGameInterface(String username){
		remove(contentPane);
		GameInterface gameInterface = new GameInterface(username);
		setContentPane(gameInterface);
		revalidate();
		repaint();
	}
	
}
