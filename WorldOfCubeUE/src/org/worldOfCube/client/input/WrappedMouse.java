package org.worldOfCube.client.input;

import org.lwjgl.input.Mouse;
import org.universeengine.display.UniDisplay;

public class WrappedMouse {
	
	private static UniDisplay display;
	
	public static void giveDisplay(UniDisplay d) {
		display = d;
	}
	
	public static int getX() {
		return Mouse.getX();
	}
	
	public static int getY() {
		return (display.getHeight()-1)-Mouse.getY();
	}

}
