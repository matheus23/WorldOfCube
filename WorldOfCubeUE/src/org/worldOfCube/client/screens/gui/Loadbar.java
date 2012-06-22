package org.worldOfCube.client.screens.gui;

import java.awt.Rectangle;

import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.res.Sprite;

public class Loadbar implements GUIElement {
	
	private Box box;
	private float width;
	private float progress;
	private Rectangle rect;
	
	public Loadbar(int x, int y, int width) {
		rect = new Rectangle();
		box = new Box(0, 0, 1, 1, ResLoader.GUI_BORDER_NORMAL);
		set(x, y, width);
	}
	
	public void set(int x, int y, int width) {
		int realx = x-(width/2);
		int realy = y-14;
		rect.x = realx;
		rect.y = realy;
		rect.width = width;
		box.set(realx-10, realy-10, width+20, 28);
	}
	
	public void setProgress(float progress) {
		while (progress > 100f) {
			progress -= 100f;
		}
		this.progress = progress;
		this.width = (progress/100f)*(float)rect.width;
		width = Math.max(8f, width);
	}
	
	public Rectangle getRect() {
		return box.getRect();
	}
	
	public float getProgress() {
		return progress;
	}
	
	public void tick(UniDisplay display) {
	}
	
	public void render() {
		box.render();
		
		Sprite left = ResLoader.get(ResLoader.GUI_LOADBAR, ResLoader.GUI_LOADBAR_LEFT);
		Sprite mid = ResLoader.get(ResLoader.GUI_LOADBAR, ResLoader.GUI_LOADBAR_MID);
		Sprite right = ResLoader.get(ResLoader.GUI_LOADBAR, ResLoader.GUI_LOADBAR_RIGHT);
		
		left.bindAndRender(rect.x, rect.y, 4, 8);
		right.bindAndRender(rect.x+width-4, rect.y, 4, 8);
		mid.bindAndRender(rect.x+4, rect.y, width-8, 8);
	}

}
