package org.worldOfCube.client.screens;

import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.logic.collision.Rectangle;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.screens.gui.BoxLabel;
import org.worldOfCube.client.screens.gui.BoxLabelListener;
import org.worldOfCube.client.screens.gui.ZoomEffect;

public class ScreenMenu extends Screen implements BoxLabelListener {

	private BoxLabel buttonWorlds;
	private BoxLabel buttonExit;

	private ZoomEffect zoomBG;

	public ScreenMenu(UniDisplay display, ClientMain mep) {
		super(display, mep, 0f, 0f, 0f, 0f);

		ResLoader.loadTitle();
		zoomBG = new ZoomEffect(ResLoader.tsBackground, 3f,
				new Rectangle(1326, 976, 610, 2148),
				new Rectangle(382, 814, 200, 200),
				new Rectangle(0, 3440, 388, 360),
				new Rectangle(330, 2700, 358, 272));

		buttonWorlds = new BoxLabel("Worlds", this);
		buttonWorlds.withInfoText("See available Worlds.");
		buttonExit = new BoxLabel("Exit", this);
		buttonExit.withInfoText("Exit game.");

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
		zoomBG.tick(getDelta());
		buttonWorlds.tick(display);
		buttonExit.tick(display);
	}

	@Override
	public void render() {
		zoomBG.render(display);
		buttonWorlds.render();
		buttonExit.render();
		buttonWorlds.renderTwo();
		buttonExit.renderTwo();
		renderCursor();
	}

	@Override
	public void resize(int neww, int newh) {
		recalcButtons(neww, newh);
	}

	@Override
	public void screenRemove() {
		ResLoader.unloadTitle();
	}

	@Override
	public void boxPressed(BoxLabel bl) {
	}

	@Override
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

	@Override
	public String getCaption() {
		return "Menu Screen";
	}

}
