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

import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.res.ResLoader;

public class BoxOptions extends BoxLabel {

	private BoxLabel label;
	private BoxOptionCycle option;
	private float division;

	public BoxOptions(String label, BoxOptionCycle option, float division) {
		super(0, 0, 1, 1, "", ResLoader.Sheets.GUI_BORDER_BLUE.ordinal());
		this.label = new BoxLabel(label);
		this.label.getBox().setAlpha(0f);
		this.option = option;
		this.division = division;
	}

	public void set(int x, int y, int w, int h) {
		getBox().set(x-5, y-5, w+10, h+10);
		int leftw = (int)(w*division);
		int rightx = x+leftw;
		int rightw = w-leftw;
		label.getBox().set(x, y, leftw, h);
		option.getBox().set(rightx, y, rightw, h);
	}

	public BoxOptionCycle getOptionBox() {
		return option;
	}

	@Override
	public void render() {
		super.render();
		option.render();
		label.render();
	}

	@Override
	public void tick(UniDisplay display) {
		super.tick(display);
		if (getBox().getState() == Box.STATE_CLICKED) {
			getBox().setState(Box.STATE_HOVER);
		}
		option.tick(display);
		label.tick(display);
		if (option.getBox().getState() == Box.STATE_HOVER
				|| option.getBox().getState() == Box.STATE_CLICKED) {
			getBox().setState(Box.STATE_NONE);
		}
		getBox().recalcColor();
	}

	@Override
	public void renderTwo() {
		if (!option.showsInfoText()) {
			super.renderTwo();
		}
		// This actually doesn't matter, cause label does never have an info text.
		label.renderTwo();
		option.renderTwo();
	}

}
