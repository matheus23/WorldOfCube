package org.worldOfCube.client.input;

import org.lwjgl.input.Mouse;
import org.universeengine.display.UniDisplay;

public class WrappedMouse {
	
	private static UniDisplay display;
	private static boolean eventHappened;
	
	public static void giveDisplay(UniDisplay d) {
		display = d;
	}
	
	public static void update() {
		eventHappened = false;
	}
	
	public static void eventHappened() {
		eventHappened = true;
	}
	
	public static boolean isEventHappened() {
		return eventHappened;
	}
	
	public static int getX() {
		return Mouse.getX();
	}
	
	public static int getY() {
		return (display.getHeight()-1)-Mouse.getY();
	}

}
