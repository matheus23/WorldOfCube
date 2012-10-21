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

import org.worldOfCube.client.res.ResLoader;

public class BoxOnOff extends BoxLabel {

	private class OnOffListener implements BoxLabelListener {

		private BoxOnOff box;

		OnOffListener(BoxOnOff box) {
			this.box = box;
		}

		@Override
		public void boxPressed(BoxLabel bl) {
		}

		@Override
		public void boxReleased(BoxLabel bl) {
			box.on = !box.on;
			box.setText(box.on ? box.onString : box.offString);
		}
	}

	private String onString;
	private String offString;
	private boolean on;

	public BoxOnOff(String on, String off, boolean isOn) {
		super(0, 0, 1, 1, isOn ? on : off, ResLoader.Sheets.GUI_BORDER_NORMAL.ordinal());
		this.onString = on;
		this.offString = off;
		this.on = isOn;
		super.addBoxLabelListener(new OnOffListener(this));
	}

	public boolean isOn() {
		return on;
	}

}
