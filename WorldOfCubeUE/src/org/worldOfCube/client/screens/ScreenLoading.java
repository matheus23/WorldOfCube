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
import org.worldOfCube.client.screens.gui.Loadbar;

public class ScreenLoading extends Screen {

	private Loadable todo;
	private BoxLabel label;
	private Loadbar loadbar;
	private boolean drawBackground;

	public ScreenLoading(UniDisplay display, ClientMain mep, boolean drawBackground, Loadable todo) {
		super(display, mep, 0f, 0f, 0f, 0f);
		this.todo = todo;
		this.drawBackground = drawBackground;

		label = new BoxLabel(todo.getTitle());
		label.getBox().setAlpha(0f);

		loadbar = new Loadbar(0, 0, 1);

		recalcButtons(display.getWidth(), display.getHeight());

		new Thread(todo).start();
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

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.screens.Screen#handleMousePosition(int, int)
	 */
	@Override
	public void tick() {
		float progress = todo.getProgress();
		if (progress >= 100f) {
			todo.nextScreen(display, mep);
		}
		label.setText(todo.getTitle());
		label.tick(display);
		loadbar.tick(display);
		loadbar.setProgress(progress);
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.screens.Screen#handleMousePosition(int, int)
	 */
	@Override
	public void render() {
		if (drawBackground) {
			fillStandardBackground();
		}
		label.render();
		loadbar.render();
		renderCursor();
	}

	@Override
	public void resize(int neww, int newh) {
		recalcButtons(neww, newh);
	}

	@Override
	public void screenRemove() {
	}

	public void recalcButtons(int w, int h) {
		label.getBox().set((int)(0.3*w), (int)(0.45*h), (int)(0.4*w), (int)(0.1*h));
		loadbar.set((int)(0.5*w), (int)(0.55*h), (int)(0.3*w));
	}

	@Override
	public String getCaption() {
		return "Loading Screen";
	}

}
