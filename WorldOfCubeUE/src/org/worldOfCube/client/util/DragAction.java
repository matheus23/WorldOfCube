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
package org.worldOfCube.client.util;

import org.lwjgl.input.Mouse;
import org.worldOfCube.client.input.WrappedMouse;

public class DragAction {

	private int xvel;
	private int yvel;
	private int lastx;
	private int lasty;

	public DragAction() {
	}

	public int getVX() {
		return xvel;
	}

	public int getVY() {
		return yvel;
	}

	public void tick() {
		int mx = WrappedMouse.getX();
		int my = WrappedMouse.getY();
		int dx = lastx-mx;
		int dy = lasty-my;
		lastx = mx;
		lasty = my;
		if (Mouse.isButtonDown(0)) {
			xvel = dx;
			yvel = dy;
		} else {
			xvel = 0;
			yvel = 0;
		}
	}

}
