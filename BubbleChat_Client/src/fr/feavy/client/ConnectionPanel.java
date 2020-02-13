package fr.feavy.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.feavy.client.network.ClientConnection;
import fr.feavy.client.network.packet.PacketHandlers;
import fr.feavy.network.packet.ConnectionPacket;
import fr.feavy.network.packet.ConnectionReplyPacket;

public class ConnectionPanel extends JPanel{

	private boolean isInitialized;
	
	private MainWindow parent;
	
	private JLabel title;
	private JLabel connexionTitle;
	
	private JLabel usernameLabel;
	private JTextField usernameField;
	
	private JButton loginButton;
	
	private JLabel adminKeyLabel;
	private JTextField adminKeyField;
	
	private JLabel infoLabel;
	
	private String username;
	private String adminKey;
	
	public ConnectionPanel(MainWindow parent){
		PacketHandlers.get().setHandler(ConnectionReplyPacket.class, this::onConnectionReply);
		this.parent = parent;
		isInitialized = false;
		
		title = new JLabel("Bubble Chat", SwingConstants.CENTER);
		title.setFont(new Font("Impact", 70, 70));
		title.setForeground(new Color(33, 150, 243));
		
		connexionTitle = new JLabel("Connexion", SwingConstants.CENTER);
		connexionTitle.setFont(new Font("Arial", 30, 30));
		
		usernameLabel = new JLabel("Pseudonyme : ", SwingConstants.CENTER);
		usernameField = new JTextField();
		loginButton = new JButton("Connexion");
		loginButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				username = usernameField.getText();
				adminKey = adminKeyField.getText();
				
				if(username.length() > 0 && adminKey.length() <= 19){
					loginButton.setEnabled(false);
					infoLabel.setText("Connexion en cours...");
					if(!adminKey.equals("0000-0000-0000-0000"))
						ClientConnection.get().sendPacket(new ConnectionPacket(username, adminKey));
					else
						ClientConnection.get().sendPacket(new ConnectionPacket(username));
				}else{
					infoLabel.setText("Erreur : Données entrées invalides.");
				}
				
			}
		});
		adminKeyLabel = new JLabel("Clé admin : ", SwingConstants.CENTER);
		adminKeyField = new JTextField("0000-0000-0000-0000");
		adminKeyField.setHorizontalAlignment(JTextField.CENTER);
		adminKeyField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(adminKeyField.getText().length() > 19)
					adminKeyField.setText(adminKeyField.getText().substring(0,19));
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		
		infoLabel = new JLabel(ClientConnection.get().isConnected() ? "..." : "Erreur : le serveur est actuellement indisponible.", SwingConstants.CENTER);
		loginButton.setEnabled(ClientConnection.get().isConnected());
		
		add(title);
		add(connexionTitle);
		add(usernameLabel);
		add(usernameField);
		add(loginButton);
		add(adminKeyLabel);
		add(adminKeyField);
		add(infoLabel);
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		title.setBounds((width-400)/2, (height-200)/2-200, 400, 200);
		connexionTitle.setBounds((width-400)/2, (height-200)/2-100, 400, 200);
		
		usernameLabel.setBounds((width-140)/2-75, (height-20)/2-50, 140, 20);
		usernameField.setBounds((width-140)/2+40, (height-20)/2-50, 140, 20);
		
		loginButton.setBounds((width-110)/2, (height-30)/2, 110, 30);
		
		adminKeyLabel.setBounds((width-60)/2-85, (height-20)/2+50, 60, 20);
		adminKeyField.setBounds((width-170)/2+30, (height-20)/2+50, 170, 20);
		
		infoLabel.setBounds((width-300)/2, (height-20)/2+90, 300, 20);
	}

	public void onConnectionReply(ConnectionReplyPacket p) {
		if(p.isSuccess()){
			infoLabel.setText("Connecté avec succès.");
			MainWindow.connected(username, p.getUUID());
		}else{
			loginButton.setEnabled(true);
			infoLabel.setText("La connexion a echouée : ce pseudonyme est déjà utilisé.");
		}
	}
	
}
