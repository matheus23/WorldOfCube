package org.worldOfCube.client.screens;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.universeengine.display.UniDisplay;
import org.worldOfCube.Log;
import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.logic.chunks.SingleWorld;
import org.worldOfCube.client.logic.entity.EntityPlayer;
import org.worldOfCube.client.res.GLFont;
import org.worldOfCube.client.util.Config;
import org.worldOfCube.client.util.debug.PerfMonitor;

public class ScreenGame extends Screen {

	private SingleWorld world;
	private double lastDelta;

	public ScreenGame(UniDisplay display, ClientMain mep, String name) {
		super(display, mep, ClientMain.BG_R, ClientMain.BG_G, ClientMain.BG_B, 0f);
		world = new SingleWorld(new EntityPlayer(32768/2, 0, "Player"), 32, 64, name, display);
	}

	public ScreenGame(UniDisplay display, ClientMain mep, SingleWorld world) {
		super(display, mep, ClientMain.BG_R, ClientMain.BG_G, ClientMain.BG_B, 0f);
		this.world = world;
	}

	@Override
	public void tick() {
		lastDelta = getDelta();
		world.tick(lastDelta, display);
	}

	@Override
	public void render() {
		red = world.getClearColorRed();// Will be implemented in SingleplayerWorld ONLY!
		green = world.getClearColorGreen();
		blue = world.getClearColorBlue();
		clearScreen();
		world.render();
		if (Config.get("show_fps").equals("true")) {
			GLFont.render(10, 10, GLFont.ALIGN_LEFT, mep.getLoop().getLastFps() + " fps", 10);
		}
		if (Config.get("debug").equals("on")) {
			GLFont.render(10, 20, GLFont.ALIGN_LEFT, "Delta: " + lastDelta, 10);
			PerfMonitor.render();
		}
		renderCursor();
	}

	@Override
	public void handleKeyEvent(int keyCode, char keyChar, boolean down) {
		// First use the key events yourself:
		if (down) {
			// If you press the Escape key, the screen will be set to a Pausing screen:
			if (keyCode == Keyboard.KEY_ESCAPE) {
				mep.setScreen(new ScreenPause(display, mep, world, true));
			}
		} else {
			// If you release the "V" key, vertical sync is enabled, or disabled:
			if (keyCode == Keyboard.KEY_V) {
				mep.vsync = !mep.vsync;
				Display.setVSyncEnabled(mep.vsync);
				Log.out("VSync %s", mep.vsync ? "enabled" : "disabled");
			}
		}
		// Now give it to world:
		world.handleKeyEvent(keyCode, keyChar, down);
	}

	@Override
	public void handleMouseEvent(int mousex, int mousey, int button, boolean down) {
		// Give the event to world:
		world.handleMouseEvent(mousex, mousey, button, down);
	}

	@Override
	public void handleMousePosition(int mousex, int mousey) {
		// Give the event to world:
		world.handleMousePosition(mousex, mousey);
	}

	@Override
	public void resize(int neww, int newh) {
		world.getViewport().w = neww;
		world.getViewport().h = newh;
	}

	@Override
	public void screenRemove() {
	}

	@Override
	public String getCaption() {
		return "Game";
	}

}
