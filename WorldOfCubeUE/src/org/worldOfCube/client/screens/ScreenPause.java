package org.worldOfCube.client.screens;

import java.io.IOException;

import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.logic.chunks.SingleWorld;
import org.worldOfCube.client.logic.chunks.WorldSaveManager;
import org.worldOfCube.client.screens.gui.BoxLabel;
import org.worldOfCube.client.screens.gui.BoxLabelListener;

public class ScreenPause extends Screen implements BoxLabelListener {

	private BoxLabel buttonBack;
	private BoxLabel buttonBTM;
	private BoxLabel buttonOpt;
	private BoxLabel buttonSave;
	private SingleWorld world;
	private boolean pause;

	public ScreenPause(UniDisplay display, ClientMain mep, SingleWorld world, boolean pause) {
		super(display, mep, ClientMain.BG_R, ClientMain.BG_G, ClientMain.BG_B, 0f);;

		this.pause = pause;
		this.world = world;

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

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.screens.Screen#handleMousePosition(int, int)
	 */
	@Override
	public void handleKeyEvent(int keyCode, char keyChar, boolean down) {}

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
		if (!pause) {
			world.tick(1f, display);
		}
		buttonBack.tick(display);
		buttonBTM.tick(display);
		buttonOpt.tick(display);
		buttonSave.tick(display);
	}

	@Override
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
		if (bl.equals(buttonBTM)) {
			mep.setScreen(new ScreenLoading(display, mep, true, new Loadable() {
				private float progress = 50f;

				@Override
				public void run() {
					try {
						WorldSaveManager.saveWorld(world);
					} catch (IOException e) {
						e.printStackTrace();
					}
					world.destroy();
					progress = 100f;
				}

				@Override
				public void nextScreen(UniDisplay display, ClientMain mep) {
					mep.setScreen(new ScreenMenu(display, mep));
				}

				@Override
				public String getTitle() {
					return "Saving world.";
				}

				@Override
				public float getProgress() {
					return progress;
				}
			}));
		} else if (bl.equals(buttonBack)) {
			mep.setScreen(new ScreenGame(display, mep, world));
		} else if (bl.equals(buttonOpt)) {
			mep.setScreen(new ScreenOptions(display, mep, this));
		} else if (bl.equals(buttonSave)) {
			WorldSaveManager.saveSingleWorldThreaded(world);
		}
	}

	public void recalcButtons(int w, int h) {
		buttonBack.getBox().set((int)(0.2*w), (int)(0.2*h), (int)(0.6*w), (int)(0.1*h));
		buttonBTM.getBox().set((int)(0.2*w), (int)(0.5*h), (int)(0.6*w), (int)(0.1*h));;
		buttonOpt.getBox().set((int)(0.2*w), (int)(0.3*h), (int)(0.6*w), (int)(0.1*h));
		buttonSave.getBox().set((int)(0.2*w), (int)(0.4*h), (int)(0.6*w), (int)(0.1*h));
	}

	@Override
	public String getCaption() {
		return "Pausing Screen";
	}

}
