package org.worldOfCube.client.screens;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.universeengine.display.UniDisplay;
import org.worldOfCube.Log;
import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.res.GLFont;
import org.worldOfCube.client.util.Config;
import org.worldOfCube.client.util.debug.PerfMonitor;

public class ScreenGame extends Screen {

	private World world;
	private double lastDelta;

	public ScreenGame(UniDisplay display, ClientMain mep, String name) {
		super(display, mep, ClientMain.BG_R, ClientMain.BG_G, ClientMain.BG_B, 0f);
		world = new World(display, 32, 64, name);
	}

	public ScreenGame(UniDisplay display, ClientMain mep, World world) {
		super(display, mep, ClientMain.BG_R, ClientMain.BG_G, ClientMain.BG_B, 0f);
		this.world = world;
	}

	public void tick() {
		lastDelta = getDelta();
		world.tick(lastDelta);
	}

	public void render() {
		red = world.getClearRed();
		green = world.getClearGreen();
		blue = world.getClearBlue();
		clearScreen();
		world.render();
		if (Config.get("show_fps").equals("true")) {
			GLFont.render(10, 10, GLFont.ALIGN_LEFT, mep.getLoop().getLastFps() + " fps", 10);
		}
		if (Config.get("debug").equals("on")) {
			GLFont.render(10, 20, GLFont.ALIGN_LEFT, "Delta: " + lastDelta, 10);
			PerfMonitor.render();
		}
	}

	public void keyPressed(int key) {
		if (key == Keyboard.KEY_ESCAPE) {
			mep.setScreen(new ScreenPause(display, mep, world, true));
		}
	}

	public void keyReleased(int key) {
		if (key == Keyboard.KEY_V) {
			mep.vsync = !mep.vsync;
			Display.setVSyncEnabled(mep.vsync);
			Log.out(this, "VSync %s", mep.vsync ? "enabled" : "disabled");
		}
	}

	public void resize(int neww, int newh) {
	}

	public void screenRemove() {
	}
	
	public String getCaption() {
		return "Game";
	}

}
