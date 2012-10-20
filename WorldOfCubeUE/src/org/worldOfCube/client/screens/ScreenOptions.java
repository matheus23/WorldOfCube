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

import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.screens.gui.BoxLabel;
import org.worldOfCube.client.screens.gui.BoxLabelListener;
import org.worldOfCube.client.screens.gui.BoxOptionCycle;
import org.worldOfCube.client.screens.gui.BoxOptions;
import org.worldOfCube.client.util.Config;

public class ScreenOptions extends Screen implements BoxLabelListener {

	private BoxLabel buttonBack;
	private BoxOptions buttonVSync;
	private BoxOptions buttonBlockRendering;
	private BoxOptions buttonDebug;
	private BoxOptions buttonShowFPS;
	private ScreenPause sp;

	public ScreenOptions(UniDisplay display, ClientMain mep, ScreenPause sp) {
		super(display, mep, 0.1f, 0.1f, 0.1f, 0f);
		this.sp = sp;

		buttonBack = new BoxLabel("Back", this);

		BoxOptionCycle opt = new BoxOptionCycle(mep.vsync ? 0 : 1, "on", "off");
		opt.withInfoText(
				"If set to \"on\", this will\n" +
				"cause the game to sync\n" +
				"the Frame-rate to your\n" +
				"monitor's refresh rate.");
		buttonVSync = new BoxOptions("VSync", opt, 0.6f);
		buttonVSync.withInfoText(
				"Vertical Synchronization.\n" +
				" - whether to set the Frame-\n" +
				"   rate to the monitor's\n" +
				"   refresh-rate.");

		BoxOptionCycle opt2 = new BoxOptionCycle(Config.getRestart("block_rendering").equals("imm") ? 0 : 1, "IMM", "VAO");
		opt2.withInfoText(
				"Which techneque to use for\n" +
				"the rendering of Blocks:\n" +
				" - IMM: Immediate Mode,\n" +
				"   can be slower or faster.\n" +
				" - VAO: VertexArrayObjects.\n" +
				"Options will be applied after\n" +
				"you restart the game.");
		buttonBlockRendering = new BoxOptions("Block rendering", opt2, 0.6f);
		buttonBlockRendering.withInfoText(
				"The techneque to use for\n" +
				"rendering blocks in general.\n" +
				"This will not change the way,\n" +
				"the blocks look.\n" +
				"Optins will be applied after\n" +
				"you restart the game.");

		BoxOptionCycle opt3 = new BoxOptionCycle(Config.get("debug").equals("on") ? 0 : 1, "on", "off");
		opt3.withInfoText(
				"Enable or Disable Debug\n" +
				"information.");
		buttonDebug = new BoxOptions("Show Debug", opt3, 0.6f);
		buttonDebug.withInfoText(
				"Enable or Disable Debug\n" +
				"information.");

		BoxOptionCycle opt4 = new BoxOptionCycle(Config.get("show_fps").equals("true") ? 0 : 1, "on", "off");
		opt4.withInfoText(
				"If this Option is set\n" +
				"to \"yes\", then the\n" +
				"FPS (Frames per second)\n" +
				"and Delta time will be \n" +
				"shown in the top right\n" +
				"corner ingame.");
		buttonShowFPS = new BoxOptions("Show FPS", opt4, 0.6f);
		buttonShowFPS.withInfoText(
				"If this Option is set\n" +
				"to \"yes\", then the\n" +
				"FPS (Frames per second)\n" +
				"and Delta time will be \n" +
				"shown in the top right\n" +
				"corner ingame.");

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
		buttonBack.tick(display);
		buttonVSync.tick(display);
		buttonBlockRendering.tick(display);
		buttonDebug.tick(display);
		buttonShowFPS.tick(display);
	}

	@Override
	public void render() {
		fillStandardBackground();

		buttonBack.render();
		buttonVSync.render();
		buttonBlockRendering.render();
		buttonDebug.render();
		buttonShowFPS.render();

		buttonBack.renderTwo();
		buttonVSync.renderTwo();
		buttonBlockRendering.renderTwo();
		buttonDebug.renderTwo();
		buttonShowFPS.renderTwo();
		renderCursor();
	}

	@Override
	public void resize(int neww, int newh) {
		recalcButtons(neww, newh);
	}

	@Override
	public void screenRemove() {
		boolean vsync = buttonVSync.getOptionBox().getSelectedOption().equals("on");
		mep.setVSync(vsync);
		Config.set("vsync", vsync ? "on" : "off");
		Config.setRestart("block_rendering",
				buttonBlockRendering.getOptionBox().getSelectedOption().equals("IMM") ? "imm" : "vao");
		Config.set("debug", buttonDebug.getOptionBox().getSelectedOption().equals("on") ? "on" : "off");
		Config.set("show_fps", buttonShowFPS.getOptionBox().getSelectedOption().equals("on") ? "true" : "false");
	}

	@Override
	public void boxPressed(BoxLabel bl) {
	}

	@Override
	public void boxReleased(BoxLabel bl) {
		if (bl.equals(buttonBack)) {
			mep.setScreen(sp);
		}
	}

	public void recalcButtons(int w, int h) {
		buttonBack.getBox().set((int)(0.7f*w), (int)(0.9f*h), (int)(0.3f*w), (int)(0.1f*h));
		buttonVSync.set((int)(0.1*w), (int)(0.2*h), (int)(0.8*w), (int)(0.1*h));
		buttonBlockRendering.set((int)(0.1*w), (int)(0.3*h), (int)(0.8*w), (int)(0.1*h));
		buttonDebug.set((int)(0.1*w), (int)(0.4*h), (int)(0.8*w), (int)(0.1*h));
		buttonShowFPS.set((int)(0.1*w), (int)(0.5*h), (int)(0.8*w), (int)(0.1*h));
	}

	@Override
	public String getCaption() {
		return "Options Screen";
	}

}
