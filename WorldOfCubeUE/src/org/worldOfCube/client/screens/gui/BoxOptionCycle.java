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

public class BoxOptionCycle extends BoxLabel {

	private class CycleListener implements BoxLabelListener {
		private BoxOptionCycle box;

		public CycleListener(BoxOptionCycle box) {
			this.box = box;
		}

		@Override
		public void boxPressed(BoxLabel bl) {
		}

		@Override
		public void boxReleased(BoxLabel bl) {
			box.current = (box.current+1)%box.options.length;
		}

	}

	private String[] options;
	private int current;

	public BoxOptionCycle(int selected, String... options) {
		super(0, 0, 1, 1, options[selected], ResLoader.Sheets.GUI_BORDER_NORMAL.ordinal());
		this.current = selected;
		this.options = options;
		addBoxLabelListener(new CycleListener(this));
	}

	public String getSelectedOption() {
		return options[current];
	}

	public int getCurrentSelected() {
		return current;
	}

	public String[] getPossibleOptions() {
		return options;
	}

	@Override
	public void tick(UniDisplay display) {
		super.tick(display);
		if (!getText().equals(options[current])) {
			setText(options[current]);
		}
	}

}
