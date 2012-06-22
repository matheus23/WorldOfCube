package org.worldOfCube.client.screens;

import java.io.File;

import org.lwjgl.input.Keyboard;
import org.universeengine.display.UniDisplay;
import org.worldOfCube.Log;
import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.logic.chunks.WorldSaveManager;
import org.worldOfCube.client.res.GLFont;
import org.worldOfCube.client.screens.gui.BoxInputLabel;
import org.worldOfCube.client.screens.gui.BoxInputLabelListener;
import org.worldOfCube.client.screens.gui.BoxLabel;
import org.worldOfCube.client.screens.gui.BoxLabelListener;

public class ScreenWorldCreate extends Screen implements BoxLabelListener, BoxInputLabelListener {
	
	private BoxLabel buttonCreate;
	private BoxLabel buttonBack;
	private BoxLabel labelWorldName;
	private BoxLabel labelWorldSeed;
	private BoxInputLabel inputName;
	private BoxInputLabel inputSeed;

	public ScreenWorldCreate(UniDisplay display, ClientMain mep) {
		super(display, mep, 0f, 0f, 0f, 0f);
		
		buttonCreate = new BoxLabel("Create", this);
		buttonCreate.withInfoText(
				"Create a world with the given name\n" +
				"and the given seed.");
		buttonBack = new BoxLabel("Back", this);
		labelWorldName = new BoxLabel("World name:");
		labelWorldSeed = new BoxLabel("World seed:");
		inputName = new BoxInputLabel(GLFont.CENTER, (640/20)-2, this);
		inputName.setInfoText(
				"Give the world a name.\n" +
				"If the name is already taken,\n" +
				"no world will be created.");
		inputSeed = new BoxInputLabel(GLFont.CENTER, (640/20)-2, this);
		inputSeed.setInfoText(
				"Enter a seed. If you create another\n" +
				"world with the same seed,\n" +
				"it will create the same world.\n" +
				"If you enter no seed,\n" +
				"a random seed will be used.");
		
		labelWorldName.getBox().setAlpha(0f);
		labelWorldSeed.getBox().setAlpha(0f);
		
		recalcButtons(display.getWidth(), display.getHeight());
	}

	public void keyPressed(int key) {
	}

	public void keyReleased(int key) {
		if (key == Keyboard.KEY_ESCAPE) {
			mep.setScreen(new ScreenWorlds(display, mep));
		}
	}

	public void tick() {
		buttonCreate.tick(display);
		buttonBack.tick(display);
		labelWorldName.tick(display);
		labelWorldSeed.tick(display);
		inputName.tick(display);
		inputSeed.tick(display);
	}

	public void render() {
		fillStandardBackground();
		
		buttonCreate.render();
		buttonBack.render();
		labelWorldName.render();
		labelWorldSeed.render();
		inputName.render();
		inputSeed.render();
		
		buttonCreate.renderTwo();
		buttonBack.renderTwo();
		labelWorldName.renderTwo();
		labelWorldSeed.renderTwo();
		inputName.renderTwo();
		inputSeed.renderTwo();
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
			if (inputName.getText().length() > 0
					&& checkWorldName(inputName.getText())) {
				mep.setScreen(new ScreenLoading(display, mep, true, new Loadable() {
					private World world;
					private boolean generated;
					
					public void run() {
						world = new World(display, inputSeed.getText().length() == 0 ? System.currentTimeMillis() : inputSeed.getText().toString().hashCode(), 32, 64, inputName.getText().toString(), true);
						world.generate(32, 64);
						generated = true;
					}
					
					public void nextScreen(UniDisplay display, ClientMain mep) {
						mep.setScreen(new ScreenGame(display, mep, world));
					}
					
					public String getTitle() {
						return world == null ? "Creating World." : world.getLoadingTitle();
					}
					
					public float getProgress() {
						return world == null ? 0f : (generated ? 100f : world.getLoaded());
					}
				}));
			}
		} else if (bl.equals(buttonBack)) {
			mep.setScreen(new ScreenWorlds(display, mep));
		}
	}
	
	public void enterPressed(BoxInputLabel bil) {
	}
	
	public void recalcButtons(int w, int h) {
		buttonCreate.	getBox().set((int)(0.3*w), (int)(0.6*h), (int)(0.4*w), (int)(0.1*h));
		buttonBack.		getBox().set((int)(0.6*w), (int)(0.9*h), (int)(0.4*w), (int)(0.1*h));
		labelWorldName.	getBox().set((int)(0.2*w), (int)(0.2*h), (int)(0.6*w), (int)(0.1*h));
		labelWorldSeed.	getBox().set((int)(0.2*w), (int)(0.4*h), (int)(0.6*w), (int)(0.1*h));
		inputName.		getBox().set((int)(0.2*w), (int)(0.3*h), (int)(0.6*w), (int)(0.1*h));
		inputSeed.		getBox().set((int)(0.2*w), (int)(0.5*h), (int)(0.6*w), (int)(0.1*h));
	}
	
	private boolean checkWorldName(StringBuilder strb) {
		if (new File(WorldSaveManager.worldDirStr + "/" + strb.toString()).exists()) {
			Log.out(this, "World does already exist.");
			return false;
		}
		return true;
	}
	
	public String getCaption() {
		return "World Creation Screen";
	}

}
