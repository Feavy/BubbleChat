package fr.feavy.client.game;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Drawable {

	private float x, y;
	protected float width, height;
	protected float screenRatio;

	private Map<String, Drawable> childs;

	public Drawable(float x, float y, float width, float height, float screenRatio) {
		this.childs = new HashMap<>();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.screenRatio = screenRatio;
	}

	public abstract void draw(Graphics2D g);
	
	protected void addChild(String id, Drawable d){
		childs.put(id, d);
	}
	
	protected void removeChild(String id){
		childs.remove(id);
	}
	
	protected boolean isChild(String id){return childs.containsKey(id);}
	
	protected void drawChilds(Graphics2D g){
		for(Drawable c : childs.values())
			c.draw(g);
	}

	public void newScreenRatio(float ratio) {
		this.screenRatio = ratio;
		System.out.println("New ratio : " + ratio);
		x *= ratio;
		y *= ratio;
		width *= ratio;
		height *= ratio;
		for(Drawable c : childs.values()){
			c.x *= ratio;
			c.y *= ratio;
			c.width *= ratio;
			c.height *= height;
		}
	}

	public float x() {return x;}

	public float y() {return y;}
	
	public void setX(float x) {
		this.x = x;
		for(Drawable c : childs.values())
			c.setX(x);
	}

	public void setY(float y) {
		this.y = y;
		for(Drawable c : childs.values())
			c.setY(y);
	}

}
