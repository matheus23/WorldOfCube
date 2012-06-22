package org.worldOfCube.client.screens;

import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.res.Sprite;
import org.worldOfCube.client.screens.gui.BoxLabel;
import org.worldOfCube.client.screens.gui.BoxLabelListener;

public class ScreenMenu extends Screen implements BoxLabelListener {
	
	private Sprite titlescreen;
	
	private BoxLabel buttonWorlds;
	private BoxLabel buttonExit;
	
	public ScreenMenu(UniDisplay display, ClientMain mep) {
		super(display, mep, 0f, 0f, 0f, 0f);
		
		ResLoader.loadTitle();
		titlescreen = ResLoader.get(ResLoader.GUI_BIG_TITLE_SCREEN, 0);
		
		buttonWorlds = new BoxLabel("Worlds", this);
		buttonWorlds.withInfoText("See available Worlds.");
		buttonExit = new BoxLabel("Exit", this);
		buttonExit.withInfoText("Exit game.");
		
		recalcButtons(display.getWidth(), display.getHeight());
	}

	public void keyPressed(int key) {
	}

	public void keyReleased(int key) {
	}
	
	public void tick() {
		buttonWorlds.tick(display);
		buttonExit.tick(display);
	}
	
	public void render() {
		titlescreen.bindAndRender(0, 0, display.getWidth(), display.getHeight());
		buttonWorlds.render();
		buttonExit.render();
		buttonWorlds.renderTwo();
		buttonExit.renderTwo();
	}
	
	public void resize(int neww, int newh) {
		recalcButtons(neww, newh);
	}

	public void screenRemove() {
		ResLoader.unloadTitle();
	}

	public void boxPressed(BoxLabel bl) {
	}

	public void boxReleased(BoxLabel bl) {
		if (bl.equals(buttonWorlds)) {
			mep.setScreen(new ScreenWorlds(display, mep));
		} else if (bl.equals(buttonExit)) {
			mep.getLoop().stop();
		}
	}
	
	public void recalcButtons(int w, int h) {
		buttonWorlds.getBox().set((int)(0.2f*w), (int)(0.3f*h), (int)(0.6f*w), (int)(0.1f*h));
		buttonExit.getBox().set((int)(0.2f*w), (int)(0.5f*h), (int)(0.6f*w), (int)(0.1f*h));
	}
	
	public String getCaption() {
		return "Menu Screen";
	}
	
}
