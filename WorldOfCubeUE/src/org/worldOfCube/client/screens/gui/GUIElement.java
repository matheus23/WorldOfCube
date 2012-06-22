package org.worldOfCube.client.screens.gui;

import java.awt.Rectangle;

import org.universeengine.display.UniDisplay;

public interface GUIElement {
	
	public void tick(UniDisplay display);
	public void render();
	public Rectangle getRect();

}
