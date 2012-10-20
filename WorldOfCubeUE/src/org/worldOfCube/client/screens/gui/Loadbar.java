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
package org.worldOfCube.client.screens.gui;

import java.awt.Rectangle;

import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.res.Sprite;

public class Loadbar implements GUIElement {

	private Box box;
	private float width;
	private float progress;
	private Rectangle rect;

	public Loadbar(int x, int y, int width) {
		rect = new Rectangle();
		box = new Box(0, 0, 1, 1, ResLoader.GUI_BORDER_NORMAL);
		set(x, y, width);
	}

	public void set(int x, int y, int width) {
		int realx = x-(width/2);
		int realy = y-14;
		rect.x = realx;
		rect.y = realy;
		rect.width = width;
		box.set(realx-10, realy-10, width+20, 28);
	}

	public void setProgress(float progress) {
		while (progress > 100f) {
			progress -= 100f;
		}
		this.progress = progress;
		this.width = (progress/100f)*rect.width;
		width = Math.max(8f, width);
	}

	@Override
	public Rectangle getRect() {
		return box.getRect();
	}

	public float getProgress() {
		return progress;
	}

	@Override
	public void tick(UniDisplay display) {
	}

	@Override
	public void render() {
		box.render();

		Sprite left = ResLoader.get(ResLoader.GUI_LOADBAR, ResLoader.GUI_LOADBAR_LEFT);
		Sprite mid = ResLoader.get(ResLoader.GUI_LOADBAR, ResLoader.GUI_LOADBAR_MID);
		Sprite right = ResLoader.get(ResLoader.GUI_LOADBAR, ResLoader.GUI_LOADBAR_RIGHT);

		left.bindAndRender(rect.x, rect.y, 4, 8);
		right.bindAndRender(rect.x+width-4, rect.y, 4, 8);
		mid.bindAndRender(rect.x+4, rect.y, width-8, 8);
	}

}
