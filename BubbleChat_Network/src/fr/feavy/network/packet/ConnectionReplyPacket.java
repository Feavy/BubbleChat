package fr.feavy.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ConnectionReplyPacket extends Packet{
	private boolean isSuccess;
	private String uuid = null;

	public ConnectionReplyPacket(DataInputStream dis) throws IOException {
		this(dis.readBoolean(), dis.readUTF());
	}

	public ConnectionReplyPacket(boolean isSuccess) {
		this(isSuccess, " ");
		this.isSuccess = isSuccess;
	}
	
	public ConnectionReplyPacket(String uuid) {
		this(true, uuid);
	}

	public ConnectionReplyPacket(boolean isSuccess, String uuid) {
		super(PacketID.CONNECTION_REPLY);
		this.isSuccess = isSuccess;
		this.uuid = uuid;
	}

	public boolean isSuccess(){return isSuccess;}
	
	public String getUUID(){return uuid;}

	@Override
	public void writeTo(DataOutputStream outputStream) throws IOException {
		outputStream.writeBoolean(isSuccess);
		outputStream.writeUTF(uuid);
	}

	public enum ResponseID {
		SUCCESS(0), USERNAME_LENGTH(1);
		
		private int id;
		
		ResponseID(int id){
			this.id = id;
		}
	}
	
}
