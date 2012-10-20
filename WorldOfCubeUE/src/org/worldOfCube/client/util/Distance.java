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

public final class Distance {

	public static double squared(double x1, double y1, double x2, double y2) {
		double dx = (x2-x1);
		double dy = (y2-y1);
		return dx*dx + dy*dy ;
	}

	public static float squared(float x1, float y1, float x2, float y2) {
		float dx = (x2-x1);
		float dy = (y2-y1);
		return dx*dx + dy*dy ;
	}

	public static int squared(int x1, int y1, int x2, int y2) {
		int dx = (x2-x1);
		int dy = (y2-y1);
		return dx*dx + dy*dy ;
	}

	public static double get(double x1, double y1, double x2, double y2) {
		return Math.sqrt(squared(x1, y1, x2, y2));
	}

	public static float get(float x1, float y1, float x2, float y2) {
		return (float)Math.sqrt(squared(x1, y1, x2, y2));
	}

	public static int get(int x1, int y1, int x2, int y2) {
		return (int)Math.sqrt(squared(x1, y1, x2, y2));
	}

	public static int getLinear(int x1, int y1, int x2, int y2) {
		int dx = x2-x1;
		int dy = y2-y1;
		return (Math.abs(dx)+Math.abs(dy))/2;
	}

	public static float getLinear(float x1, float y1, float x2, float y2) {
		float dx = x2-x1;
		float dy = y2-y1;
		return (Math.abs(dx)+Math.abs(dy))/2;
	}

	public static double getLinear(double x1, double y1, double x2, double y2) {
		double dx = x2-x1;
		double dy = y2-y1;
		return (Math.abs(dx)+Math.abs(dy))/2;
	}

}
