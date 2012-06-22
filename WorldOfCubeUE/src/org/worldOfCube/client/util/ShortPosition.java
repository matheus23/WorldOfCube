package org.worldOfCube.client.util;

public class ShortPosition {
	
	public static final short X = (short) 0xFF00;
	public static final short Y = 0x00FF;
	public static final int X_OFF = 8;
	
	public static short set(byte x, byte y) {
		return (short) (((short)x << X_OFF) | ((short)y));
	}
	
	public static byte getX(short pos) {
		return (byte)((pos & X) >> X_OFF);
	}
	
	public static byte getY(short pos) {
		return (byte)(pos & Y);
	}
	
}
