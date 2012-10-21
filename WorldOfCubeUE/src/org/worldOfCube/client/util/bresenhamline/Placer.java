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
package org.worldOfCube.client.util.bresenhamline;

import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.util.Distance;


public final class Placer {

	private Placer() {
	}

	public static void line(int x0, int y0, int x1, int y1, PlaceAction placer, boolean touchingEdges) {
		if ((x0 - x1) > 0) {
			line(x1, y1, x0, y0, placer, touchingEdges);
			return;
		}
		if (Math.abs(y1 - y0) > Math.abs(x1 - x0)) {
			line2(y0, x0, y1, x1, placer, touchingEdges);
			return;
		}
		int x = x0;
		int y = y0;
		int sum = x1 - x0;
		int dx = 2 * (x1 - x0);
		int dy = Math.abs(2 * (y1 - y0));
		int neededDy = ((y1 - y0) > 0) ? 1 : -1;

		for (int i = 0; i <= x1 - x0; i++) {
			placer.action(x, y);
			x++;
			sum -= dy;
			if (sum < 0) {
				if (touchingEdges) {
					placer.action(x, y);
				}
				y = y + neededDy;
				sum += dx;
			}
		}
	}

	private static void line2(int x0, int y0, int x1, int y1, PlaceAction placer, boolean touchingEdges) {
		if ((x0 - x1) > 0) {
			line2(x1, y1, x0, y0, placer, touchingEdges);
			return;
		}

		int x = x0;
		int y = y0;
		int sum = x1 - x0;
		int dx = 2 * (x1 - x0);
		int dy = Math.abs(2 * (y1 - y0));
		int neededDy = ((y1 - y0) > 0) ? 1 : -1;

		for (int i = 0; i <= x1 - x0; i++) {
			placer.action(y, x);
			x++;
			sum -= dy;
			if (sum < 0) {
				if (touchingEdges) {
					placer.action(y, x);
				}
				y = y + neededDy;
				sum += dx;
			}
		}
	}

	public static void circle(int rx, int ry, double dist, PlaceAction pa, World world) {
		int beginx = rx-(int)dist;
		int beginy = ry-(int)dist;
		int endx = rx+(int)dist;
		int endy = ry+(int)dist;
		beginx = Math.max(0, beginx);
		beginy = Math.max(0, beginy);
		endx = Math.min(world.totalBlocks, endx);
		endy = Math.min(world.totalBlocks, endy);
		double drx = rx;
		double dry = ry;
		for (int x = beginx; x < endx; x++) {
			for (int y = beginy; y < endy; y++) {
				if (Distance.get(drx, dry, x, y) <= dist) {
					pa.action(x, y);
				}
			}
		}
	}

}
