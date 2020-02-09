package fr.feavy.server.network;

public class Player {

	private String username;
	private boolean isAdmin;
	
	private float x, y;
	
	private ClientConnection parent;
	
	public Player(ClientConnection parent, String username, float x, float y, boolean isAdmin){
		this.parent = parent;
		this.username = username;
		this.isAdmin = isAdmin;
		this.x = x;
		this.y = y;
	}
	
	public ClientConnection getConnection(){return parent;}
	
	public String getUsername(){return username;}
	public boolean isAdmin(){return isAdmin;}
	
	public void setX(float x){
		this.x = x;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
}
