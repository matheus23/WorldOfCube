package org.worldOfCube.client.screens.gui;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.input.WrappedMouse;

public class ScrollPanel {
	
	private Rectangle rect;
	private int height;
	private int scroll;
	private List<GUIElement> elements = new ArrayList<GUIElement>();
	
	public ScrollPanel(int x, int y, int showheight) {
		this.height = showheight;
		rect = new Rectangle();
		rect.x = x;
		rect.y = y;
		rect.width = 1;
		rect.height = 1;
	}
	
	public void addElement(GUIElement element) {
		elements.add(element);
	}
	
	public void removeElement(GUIElement element) {
		elements.remove(element);
	}
	
	public void setScroll(int scroll) {
		scroll = Math.min(scroll, elements.size()-height);
		scroll = Math.max(scroll, 0);
		this.scroll = scroll;
	}
	
	public int getScroll() {
		return scroll;
	}
	
	public void tick(UniDisplay display) {
		if (rect.contains(WrappedMouse.getX(), WrappedMouse.getY())) {
			int wheel = Mouse.getDWheel();
			if (wheel > 0) {
				setScroll(scroll-1);
			} else if (wheel < 0) {
				setScroll(scroll+1);
			}
		}
		
		int max = scroll+height;
		int y = rect.y;
		for (int i = scroll; i < max && i < elements.size(); i++) {
			GUIElement e = elements.get(i);
			e.getRect().setBounds(rect.x, y, e.getRect().width, e.getRect().height);
			e.tick(display);
			y += e.getRect().height;
			
			rect.width = Math.max(rect.width, e.getRect().width);
			rect.height = Math.max(rect.height, e.getRect().height+e.getRect().y);
		}
	}
	
	public void keyPressed(int key) {
		if (key == Keyboard.KEY_UP) {
			setScroll(scroll-1);
		} else if (key == Keyboard.KEY_DOWN) {
			setScroll(scroll+1);
		}
	}
	
	public void keyReleased(int key) {
	}
	
	public void render() {
		int max = scroll+height;
		for (int i = scroll; i < max && i < elements.size(); i++) {
			elements.get(i).render();
		}
	}
	
	public void set(int x, int y) {
		rect.x = x;
		rect.y = y;
	}

}
