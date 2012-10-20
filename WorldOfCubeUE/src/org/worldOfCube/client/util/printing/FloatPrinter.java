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
package org.worldOfCube.client.util.printing;

import java.io.PrintStream;

/**
 * @author matheusdev
 *
 */
public class FloatPrinter {

	protected int width;
	protected PrintStream out;

	public FloatPrinter(int width) {
		out = System.out;
		changeOptions(width);
	}

	public void changeOptions(int width) {
		this.width = width;
	}

	public void setPrintStream(PrintStream out) {
		this.out = out;
	}

	public void print(float f) {
		out.printf("% 8f ", f);

		int barWidth = Math.round(getValidWidthFor(f));
		for (int i = 0; i < width; i++) {
			if (i < barWidth) {
				out.print("#");
			} else {
				out.print("-");
			}
		}
		out.println();
	}

	protected float getValidWidthFor(float f) {
		return f * width;
	}

}
