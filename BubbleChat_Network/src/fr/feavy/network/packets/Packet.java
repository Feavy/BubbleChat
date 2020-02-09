package fr.feavy.network.packets;

import java.net.URLEncoder;

import fr.feavy.network.utils.ByteArray;

public class Packet {

	protected PacketID id;
	protected Object[] args;
	
	public Packet(PacketID id, Object... args){
		this.id = id;
		this.args = args;
	}
	
	public Packet(Packet p){
		id = p.id;
		args = p.args;
	}
	
	public Packet(Packet p, boolean secure){
		if(secure){
			id = p.id;
			args = new String[p.args.length-1];
			args[0] = p.args[1];
			System.arraycopy(p.args, 2, args, 1, p.args.length-2);
		}
	}
	
	protected String[] toStringArray(){
		int l = (args == null)?0 : args.length;
		String[] rep = new String[l+1];
		rep[0] = id.toString();
		if(args != null)
			for(int i = 1; i < rep.length; i++)
				rep[i] = args[i-1];
		return rep;
	}
	
	public ByteArray toByteArray(){
		String[] dataStrArray = toStringArray();
		StringBuilder dataStr = new StringBuilder();
		for(String str : dataStrArray){
			if(str == null)
				dataStr.append("|");
			else
				dataStr.append(URLEncoder.encode(str)+"|");
		}
		
		ByteArray data = new ByteArray(dataStr.toString().getBytes());
		data.append((char)0);
		return data;
	}
	
	protected String[] getArgs(){return args;}
	
	public PacketID getID(){
		return id;
	}
	
}
