package org.worldOfCube.client.screens;

import java.io.IOException;

import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.input.WrappedMouse;
import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.logic.chunks.WorldSaveManager;
import org.worldOfCube.client.screens.gui.BoxLabel;
import org.worldOfCube.client.screens.gui.BoxLabelListener;

public class ScreenPause extends Screen implements BoxLabelListener {
	
	private BoxLabel buttonBack;
	private BoxLabel buttonBTM;
	private BoxLabel buttonOpt;
	private BoxLabel buttonSave;
	private World world;
	private boolean pause;
	
	public ScreenPause(UniDisplay display, ClientMain mep, World world, boolean pause) {
		super(display, mep, ClientMain.BG_R, ClientMain.BG_G, ClientMain.BG_B, 0f);;
		
		this.pause = pause;
		this.world = world;
		
		world.setKeyboardLock(true);
		
		buttonBTM = new BoxLabel("Back to Main Menu", this);
		buttonBTM.withInfoText("Go to the Main Menu.\nThe world will be saved.");
		buttonBack = new BoxLabel("Back to Game", this);
		buttonBack.withInfoText("Continue playing.");
		buttonOpt = new BoxLabel("Options", this);
		buttonOpt.withInfoText("Go to Options.");
		buttonSave = new BoxLabel("Save World", this);
		buttonSave.withInfoText("Start world save.\nWill run in another Thread.");
		
		recalcButtons(display.getWidth(), display.getHeight());
	}

	public void keyPressed(int key) {
	}

	public void keyReleased(int key) {
	}
	
	public void tick() {
		if (!pause) {
			WrappedMouse.eventHappened();
			world.tick(1f);
			WrappedMouse.update();
		}
		buttonBack.tick(display);
		buttonBTM.tick(display);
		buttonOpt.tick(display);
		buttonSave.tick(display);
	}
	
	public void render() {
		clearScreen();
		world.render();
		buttonBack.render();
		buttonBTM.render();
		buttonOpt.render();
		buttonSave.render();
		buttonBack.renderTwo();
		buttonBTM.renderTwo();
		buttonOpt.renderTwo();
		buttonSave.renderTwo();
	}
	
	public void resize(int neww, int newh) {
		recalcButtons(neww, newh);
	}

	public void screenRemove() {
		world.setKeyboardLock(false);
	}

	public void boxPressed(BoxLabel bl) {
	}

	public void boxReleased(BoxLabel bl) {
		if (bl.equals(buttonBTM)) {
			mep.setScreen(new ScreenLoading(display, mep, true, new Loadable() {
				private float progress = 50f;
				
				public void run() {
					try {
						WorldSaveManager.saveWorld(world);
					} catch (IOException e) {
						e.printStackTrace();
					}
					world.destroy();
					progress = 100f;
				}
				
				public void nextScreen(UniDisplay display, ClientMain mep) {
					mep.setScreen(new ScreenMenu(display, mep));
				}
				
				public String getTitle() {
					return "Saving world.";
				}
				
				public float getProgress() {
					return progress;
				}
			}));
		} else if (bl.equals(buttonBack)) {
			mep.setScreen(new ScreenGame(display, mep, world));
		} else if (bl.equals(buttonOpt)) {
			mep.setScreen(new ScreenOptions(display, mep, this));
		} else if (bl.equals(buttonSave)) {
			WorldSaveManager.saveWorldThread(world);
		}
	}
	
	public void recalcButtons(int w, int h) {
		buttonBack.getBox().set((int)(0.2*w), (int)(0.2*h), (int)(0.6*w), (int)(0.1*h));
		buttonBTM.getBox().set((int)(0.2*w), (int)(0.5*h), (int)(0.6*w), (int)(0.1*h));;
		buttonOpt.getBox().set((int)(0.2*w), (int)(0.3*h), (int)(0.6*w), (int)(0.1*h));
		buttonSave.getBox().set((int)(0.2*w), (int)(0.4*h), (int)(0.6*w), (int)(0.1*h));
	}
	
	public String getCaption() {
		return "Pausing Screen";
	}
	
}
