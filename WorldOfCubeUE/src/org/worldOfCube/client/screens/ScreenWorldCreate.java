/*
 * Copyright (c) 2012 matheusdev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.worldOfCube.client.screens;

import java.io.File;

import org.lwjgl.input.Keyboard;
import org.universeengine.display.UniDisplay;
import org.worldOfCube.Log;
import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.logic.chunks.SingleWorld;
import org.worldOfCube.client.logic.chunks.WorldSaver;
import org.worldOfCube.client.logic.entity.EntityPlayer;
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

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.screens.Screen#handleMousePosition(int, int)
	 */
	@Override
	public void handleKeyEvent(int keyCode, char keyChar, boolean down) {
		if (keyCode == Keyboard.KEY_ESCAPE && down) {
			mep.setScreen(new ScreenWorlds(display, mep));
		} else if (keyCode == Keyboard.KEY_TAB) {
			if (inputName.isSelected()) {
				inputName.setSelected(false);
				inputSeed.setSelected(true);
			}
		} else {
			inputName.handleKeyEvent(keyCode, keyChar, down);
			inputSeed.handleKeyEvent(keyCode, keyChar, down);
		}
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.screens.Screen#handleMousePosition(int, int)
	 */
	@Override
	public void handleMouseEvent(int mousex, int mousey, int button, boolean down) {}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.screens.Screen#handleMousePosition(int, int)
	 */
	@Override
	public void handleMousePosition(int mousex, int mousey) {}

	@Override
	public void tick() {
		buttonCreate.tick(display);
		buttonBack.tick(display);
		labelWorldName.tick(display);
		labelWorldSeed.tick(display);
		inputName.tick(display);
		inputSeed.tick(display);
	}

	@Override
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
		renderCursor();
	}

	@Override
	public void resize(int neww, int newh) {
		recalcButtons(neww, newh);
	}

	@Override
	public void screenRemove() {
	}

	@Override
	public void boxPressed(BoxLabel bl) {
	}

	@Override
	public void boxReleased(BoxLabel bl) {
		if (bl.equals(buttonCreate)) {
			if (inputName.getText().length() > 0
					&& checkWorldName(inputName.getText())) {
				mep.setScreen(new ScreenLoading(display, mep, true, new Loadable() {
					private SingleWorld world;
					private boolean generated;

					@Override
					public void run() {
						if (inputSeed.getText().length() == 0) {
							world = new SingleWorld(new EntityPlayer(0, 0, "Player"), 32, 64, inputName.getText().toString(), display);
						} else {
							world = new SingleWorld(new EntityPlayer(0, 0, "Player"), 32, 64, inputSeed.getText().hashCode(), inputName.getText().toString(), display);
						}
						generated = true;
					}

					@Override
					public void nextScreen(UniDisplay display, ClientMain mep) {
						mep.setScreen(new ScreenGame(display, mep, world));
					}

					@Override
					public String getTitle() {
						return "Loading"; //TODO: Re-Implement world == null ? "Creating World." : world.getLoadingTitle();
					}

					@Override
					public float getProgress() {
						return world == null ? 0f : (generated ? 100f : /* TODO: Re-implement: world.getLoaded()*/ 50f);
					}
				}));
			}
		} else if (bl.equals(buttonBack)) {
			mep.setScreen(new ScreenWorlds(display, mep));
		}
	}

	@Override
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
		if (new File(WorldSaver.worldDirStr + "/" + strb.toString()).exists()) {
			Log.out("World does already exist.");
			return false;
		}
		return true;
	}

	@Override
	public String getCaption() {
		return "World Creation Screen";
	}

}
