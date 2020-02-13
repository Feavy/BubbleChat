package fr.feavy.client.game;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.feavy.client.network.ClientConnection;

public class GameInterface extends JPanel{

	private GameScreen screen;
	private JTextField messageField;
	private JButton sendButton;
	
	private float widthRatio = 984/681f;
	private float heightRatio = 681/984f;
	
	private int screenWidth, screenHeight;
	
	public GameInterface(String playerUsername) {
		setLayout(null);
		setBackground(Color.BLACK);
		this.screen = new GameScreen(playerUsername);
		this.messageField = new JTextField();
		this.messageField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {

			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 10)
					sendMessage(messageField.getText());
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		this.sendButton = new JButton("Envoyer");
		this.sendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage(messageField.getText());
			}
		});
		
		add(screen);
		add(messageField);
		add(sendButton);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		
		screenWidth = ((int)((height-30)*widthRatio) <= width) ? (int)((height-30)*widthRatio) : width;
		screenHeight = (screenWidth == width) ? (int)(width*heightRatio) : height-30;
		
		screen.setBounds((width-screenWidth)/2, (height-(screenHeight+30))/2, screenWidth, screenHeight);
		//TODO : Rajouter une image en fond pour cacher les bords
		messageField.setBounds(0, height-30, width-100, 30);
		sendButton.setBounds(width-100, height-30, 100, 30);
	}

	public float getGameScreenHeight(){return (float)screenHeight;}
	public float getGameScreenWidth(){return (float)screenWidth;}
	
	private void sendMessage(String message){
		if(message.length() > 0){
			//Envoyer packet
			screen.sendMessage(message);
			messageField.setText("");
		}
	}
	
}
