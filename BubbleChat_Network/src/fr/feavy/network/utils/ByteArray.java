package fr.feavy.network.utils;

public class ByteArray {

	private StringBuffer data;
	
	public ByteArray(){
		data = new StringBuffer();
	}
	
	public ByteArray(byte[] bytes){
		data = new StringBuffer();
		data.append(new String(bytes));
	}
	
	public void append(byte[] bytes, int offset, int length){
		data.append(new String(bytes, offset, length));
	}
	
	public void append(char b){
		data.append(b);
	}
	
	public byte[] getBytes(){
		return data.toString().getBytes();
	}
	
	public int getLength(){
		return data.length();
	}
	
	public String getBytesAsString(){
		return data.toString();
	}
	
}
