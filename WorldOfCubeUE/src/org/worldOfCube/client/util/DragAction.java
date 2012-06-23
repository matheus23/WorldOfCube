package org.worldOfCube.client.util;

import org.lwjgl.input.Mouse;
import org.worldOfCube.client.input.WrappedMouse;

public class DragAction {
	
	private int xvel;
	private int yvel;
	private int lastx;
	private int lasty;
	
	public DragAction() {
	}
	
	public int getVX() {
		return xvel;
	}
	
	public int getVY() {
		return yvel;
	}
	
	public void tick() {
		int mx = WrappedMouse.getX();
		int my = WrappedMouse.getY();
		int dx = lastx-mx;
		int dy = lasty-my;
		lastx = mx;
		lasty = my;
		if (Mouse.isButtonDown(0)) {
			xvel = dx;
			yvel = dy;
		} else {
			xvel = 0;
			yvel = 0;
		}
	}

}
