/*
 * Copyright (c) 2012 matheusdev
 *
 *import java.io.File;
import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.universeengine.display.UniDisplay;
import org.worldOfCube.Log;
import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.logic.chunks.SingleWorld;
import org.worldOfCube.client.logic.chunks.WorldSaveManager;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.screens.gui.BoxLabel;
import org.worldOfCube.client.screens.gui.BoxLabelListener;
import org.worldOfCube.client.screens.gui.ScrollPanel;
n
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
import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.universeengine.display.UniDisplay;
import org.worldOfCube.Log;
import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.logic.chunks.SingleWorld;
import org.worldOfCube.client.logic.chunks.WorldSaver;
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

		File[] worlds = WorldSaver.listWorlds();
		Log.out("Found " + worlds.length + " worlds.");
		buttonWorlds = new BoxLabel[worlds.length];
		for (int i = 0; i < worlds.length; i++) {
			buttonWorlds[i] = new BoxLabel(worlds[i].getName(), ResLoader.Sheets.GUI_BORDER_NORMAL.ordinal(), this);
			scrollPanel.addElement(buttonWorlds[i]);
		}

		recalcButtons(display.getWidth(), display.getHeight());
	}

	@Override
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

	@Override
	public void handleMouseEvent(int mousex, int mousey, int button, boolean down) {}

	@Override
	public void handleMousePosition(int mousex, int mousey) {}

	@Override
	public void tick() {
		buttonCreate.tick(display);
		buttonBack.tick(display);
		scrollPanel.tick(display);
	}

	@Override
	public void render() {
		fillStandardBackground();

		buttonCreate.render();
		buttonBack.render();
		scrollPanel.render();

		buttonCreate.renderTwo();
		buttonBack.renderTwo();
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
			mep.setScreen(new ScreenWorldCreate(display, mep));
		} else if (bl.equals(buttonBack)) {
			mep.setScreen(new ScreenMenu(display, mep));
		} else {
			for (int i = 0; i < buttonWorlds.length; i++) {
				if (bl.equals(buttonWorlds[i])) {
					final String worldName = buttonWorlds[i].getText().toString();
					mep.setScreen(new ScreenLoading(display, mep, true, new Loadable() {

						private SingleWorld world;

						@Override
						public void run() {
							try {
								world = WorldSaver.loadSingleWorld(worldName, display);
								world.getChunkManager().updateAll();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void nextScreen(UniDisplay display, ClientMain mep) {
							mep.setScreen(new ScreenGame(display, mep, world));
						}

						@Override
						public String getTitle() {
							return "Loading world...";
						}

						@Override
						public float getProgress() {
							return  world == null ? Math.min(99f, WorldSaver.getLoaded()) : 100f;
						}
					}));
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

	@Override
	public String getCaption() {
		return "World List";
	}

}
