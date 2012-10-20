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
package org.worldOfCube.client.logic.chunks.generation.cave;

import java.util.Random;

import org.worldOfCube.client.util.Rand;

public class CavePathGenerator {

	private boolean[][] cave;
	private int size;

	public CavePathGenerator(boolean[][] cave, int size, int startx, int starty, int dx, int dy, Random rand) {
		this.cave = cave;
		this.size = size;
		generate(rand, dx, dy, startx, starty);
	}

	private void generate(Random rand, int dx, int dy, int x, int y) {
		int repeat = (rand.nextInt()%50)+30;
		for (int i = 0; i < repeat; i++) {
			x += dx;
			y += dy;
			dx += Rand.plusMinusRangeInt(2, rand);
			dy += Rand.plusMinusRangeInt(2, rand);
			deletePart(x, y);
			System.out.println("Blah");
		}
	}

	private void deletePart(int x, int y) {
		delete(x-1, y-1);
		delete(x-1, y);
		delete(x-1, y+1);
		delete(x, y-1);
		delete(x, y);
		delete(x, y+1);
		delete(x+1, y-1);
		delete(x+1, y);
		delete(x+1, y+1);
	}

	private void delete(int x, int y) {
		if (x >= 0 && y >= 0 && x < size && y < size) {
			cave[x][y] = true;
		}
	}

}
