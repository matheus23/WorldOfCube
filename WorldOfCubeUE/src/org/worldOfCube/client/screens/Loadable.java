package org.worldOfCube.client.screens;

import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.ClientMain;

public interface Loadable extends Runnable {
	
	public String getTitle();
	public float getProgress();
	public void nextScreen(UniDisplay display, ClientMain mep);
	
}
