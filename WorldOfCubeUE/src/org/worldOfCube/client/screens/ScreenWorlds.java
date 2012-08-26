package org.worldOfCube.client.screens;

import java.io.File;
import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.universeengine.display.UniDisplay;
import org.worldOfCube.Log;
import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.logic.chunks.WorldSaveManager;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.screens.gui.BoxLabel;
import org.worldOfCube.client.screens.gui.BoxLabelListener;
import org.worldOfCube.client.screens.gui.ScrollPanel;

public class ScreenWorlds extends Screen implements BoxLabelListener {
	
	private BoxLabel buttonCreate;
	private BoxLabel buttonBack;
	private ScrollPanel scrollPanel;
	private BoxLabel[] buttonWorlds;
	
	public ScreenWorlds(UniDisplay display, ClientMain mep) {
		super(display, mep, 0f, 0f, 0f, 0f);
		
		buttonCreate = new BoxLabel("Create World", this);
		buttonCreate.withInfoText("Create a world.");
		buttonBack = new BoxLabel("Back", this);
		
		scrollPanel = new ScrollPanel(100, 100, 6);
		
		File[] worlds = WorldSaveManager.listWorlds();
		Log.out(this, "Found " + worlds.length + " worlds.");
		buttonWorlds = new BoxLabel[worlds.length];
		for (int i = 0; i < worlds.length; i++) {
			buttonWorlds[i] = new BoxLabel(worlds[i].getName(), ResLoader.GUI_BORDER_NORMAL, this);
			scrollPanel.addElement(buttonWorlds[i]);
		}
		
		recalcButtons(display.getWidth(), display.getHeight());
	}
	
	public void handleKeyEvent(int keyCode, char keyChar, boolean down) {
		if (down) {
			scrollPanel.keyPressed(keyCode);
		} else {
			if (keyCode == Keyboard.KEY_ESCAPE) {
				mep.setScreen(new ScreenMenu(display, mep));
			}
			scrollPanel.keyReleased(keyCode);
		}
	}
	
	public void handleMouseEvent(int mousex, int mousey, int button, boolean down) {}
	
	public void handleMousePosition(int mousex, int mousey) {}

	public void tick() {
		buttonCreate.tick(display);
		buttonBack.tick(display);
		scrollPanel.tick(display);
	}

	public void render() {
		fillStandardBackground();
		
		buttonCreate.render();
		buttonBack.render();
		scrollPanel.render();
		
		buttonCreate.renderTwo();
		buttonBack.renderTwo();
		renderCursor();
	}

	public void resize(int neww, int newh) {
		recalcButtons(neww, newh);
	}

	public void screenRemove() {
	}

	public void boxPressed(BoxLabel bl) {
	}

	public void boxReleased(BoxLabel bl) {
		if (bl.equals(buttonCreate)) {
			mep.setScreen(new ScreenWorldCreate(display, mep));
		} else if (bl.equals(buttonBack)) {
			mep.setScreen(new ScreenMenu(display, mep));
		} else {
			for (int i = 0; i < buttonWorlds.length; i++) {
				if (bl.equals(buttonWorlds[i])) {
					final int index = i;
//					try {
//						mep.setScreen(new ScreenGame(display, mep, WorldSaveManager.loadWorld(buttonWorlds[i].getText().toString(), display)));
					mep.setScreen(new ScreenLoading(display, mep, true, new Loadable() {
						
						private World world;
						
						public void run() {
							try {
								world = WorldSaveManager.loadWorld(buttonWorlds[index].getText().toString(), display);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
						public void nextScreen(UniDisplay display, ClientMain mep) {
							mep.setScreen(new ScreenGame(display, mep, world));
						}
						
						public String getTitle() {
							return "Loading world...";
						}
						
						public float getProgress() {
							return  world == null ? Math.min(99f, WorldSaveManager.getLoaded()) : 100f;
						}
					}));
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
					return;
				}
			}
		}
	}
	
	public void recalcButtons(int w, int h) {
		buttonCreate.getBox().set((int)(0.0f*w), (int)(0.9f*h), (int)(0.4f*w), (int)(0.1f*h));
		buttonBack.getBox().set((int)(0.6f*w), (int)(0.9f*h), (int)(0.4f*w), (int)(0.1f*h));
		for (int i = 0; i < buttonWorlds.length; i++) {
			buttonWorlds[i].getBox().set(0, 0, (int)(0.6*w), (int)(0.1*h));
		}
		scrollPanel.set((int)(0.2*w), (int)(0.2*h));
	}

	public String getCaption() {
		return "World List";
	}

}
