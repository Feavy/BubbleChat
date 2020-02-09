package fr.feavy.client.game;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.feavy.client.MainWindow;

public class Player extends Drawable {
	
	private String username;

	private float destX, destY;
	private float dX, dY;
	private boolean isMoving;

	private float movementSpeed = 1f;
	
	private static final int MESSAGE_TIMEOUT = 60*10;
	private int currentStep = 0;
	
	public Player(String username, float x, float y, float screenRatio) {
		super(x, y, 30, 30, screenRatio);
		this.username = username;
		this.isMoving = false;
	}

	@Override
	public void draw(Graphics2D g) {
		if (isMoving) {
			move();
			if (Math.abs(x() - destX) <= movementSpeed*1.5*screenRatio && Math.abs(y() - destY) <= movementSpeed*1.5*screenRatio)
				isMoving = false;
		}
		g.setColor(Color.BLACK);
		g.setFont(MainWindow.getGameFont(15, true));
		int usernameWidth = g.getFontMetrics().stringWidth(username);
		g.drawString(username, x()-usernameWidth/2, y()-height/2-5);
		g.setColor(new Color(33, 150, 243));
		g.fillOval((int) (x()-width/2), (int) (y()-height/2), (int) width, (int) height);
		
		if(isChild("msg")){
			currentStep++;
			if(currentStep >= MESSAGE_TIMEOUT){
				currentStep = 0;
				removeChild("msg");
			}
		}
		drawChilds(g);
	}

	public String getUsername() {
		return username;
	}

	public void setMessage(String message){
		addChild("msg", new TextBubble(message, x(), y(), screenRatio));
	}
	
	@Override
	public void newScreenRatio(float ratio) {
		super.newScreenRatio(ratio);
		destX *= ratio;
		destY *= ratio;
		dX *= ratio;
		dY *= ratio;
		movementSpeed *= ratio;
		System.out.println("New speed : "+movementSpeed);
	}
	
	public void moveTo(float x, float y) {
		if(this.x() == x)
			return;
		destX = x;
		destY = y;
		double radius = Math.sqrt(Math.pow(destX-this.x(), 2)+Math.pow(destY-this.y(), 2));
		dX = 3*movementSpeed*((x > this.x()) ? 1 : -1)*(Math.abs(this.x() - destX)/(float)radius)*screenRatio;
		dY = 3*movementSpeed*((y > this.y()) ? 1 : -1)*(Math.abs(this.y() - destY)/(float)radius)*screenRatio;
		isMoving = true;
	}

	private void move() {
		setX(x() + dX);
		setY(y() + dY);
	}

}
