package fr.feavy.client.game;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.feavy.client.MainWindow;

public class TextBubble extends Drawable{

	private String text;
	
	public TextBubble(String text, float x, float y, float screenRatio) {
		super(x, y, 100, 30, screenRatio);
		this.text = text;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setFont(MainWindow.getGameFont(15, false));
		float width = g.getFontMetrics().stringWidth(text);
		g.drawString(text, x()-width/2, y()-50*screenRatio);
		g.drawLine((int)x()+4, (int)(y()-40*screenRatio), (int)(x()+width/2+5), (int)(y()-40*screenRatio));
		g.drawLine((int)(x()-width/2-5), (int)(y()-40*screenRatio), (int)x()-4, (int)(y()-40*screenRatio));
		g.drawLine((int)(x()+4), (int)(y()-40*screenRatio), (int)x(), (int)(y()-35*screenRatio));
		g.drawLine((int)(x()-4), (int)(y()-40*screenRatio), (int)x(), (int)(y()-35*screenRatio));
	}

}
