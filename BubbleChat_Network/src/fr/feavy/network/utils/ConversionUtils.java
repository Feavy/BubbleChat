package fr.feavy.network.utils;

public class ConversionUtils {

	public static String floatToString(float f){
		int bits = Float.floatToIntBits(f);
		char[] bytes = new char[4];
		bytes[0] = (char) (bits >> 24);
		bytes[1] = (char) ((bits >> 16) & 0xFF);
		bytes[2] = (char) ((bits >> 8) & 0xFF);
		bytes[3] = (char) (bits & 0xFF);
		return new String(bytes, 0, 4);
	}
	
	public static float stringToFloat(String str){
		char[] bytes = str.toCharArray();
		int bits = ((bytes[0] << 24) | (bytes[1] << 16) | (bytes[2] << 8) | (bytes[3]));
		return Float.intBitsToFloat(bits);
	}
	
}
