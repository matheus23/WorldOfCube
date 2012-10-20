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

import org.worldOfCube.client.util.Distance;

public class CaveGenerator {

	private boolean[][] vals;
	private int size;
	private Random rand;

	/**
	 * Calls the other Constructor with "new boolean[size][size]" as first argument.
	 */
	public CaveGenerator(int size, int repeat, float leaningmid, float leaningborder, Random rand) {
		this(new boolean[size][size], size, repeat, leaningmid, leaningborder, rand);
	}

	/**
	 * Creates a new boolean representation of caves, where
	 * true is a cave, and false is a wall.
	 * @param vals a boolean array to save the values to. (may be pre-created)
	 * @param size the size of the grid for both x and y dimension.
	 * @param repeat the number of times to repeat the algorithm.
	 * @param leaningmid the "leaning"-value which defines how often a cave will appear in the mid.
	 * @param leaningborder the "leaning"-value which defines how often a cave will appear on the border.
	 * @param rand an instance of Random.
	 */
	public CaveGenerator(boolean[][] vals, int size, int repeat, float leaningmid, float leaningborder, Random rand) {
		this.size = size;
		this.vals = vals;
		this.rand = rand;
		generate(repeat, leaningmid, leaningborder);
	}

	/**
	 * Randomizes the vals on the Grid to either true or false, using the Random instance.
	 * Interpolates leaningmid and leaningborder to create specific cave-possibilities.
	 */
	private void randomize(float leaningmid, float leaningborder) {
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				if (vals[x][y] != true) {
					vals[x][y] = rand.nextFloat() >=
							getLeaning(Distance.getLinear(x, y, size/2, size/2)*1.4f, leaningmid, leaningborder);
				}
			}
		}
	}

	/**
	 * Helper function to interpolate two "leaning" values.
	 * @param dist the distance to the mid of the grid.
	 * @param l1 the leaning in the mid
	 * @param l2 the leaning on the border
	 * @return the interpolated, new "leaning" value.
	 */
	private float getLeaning(float dist, float l1, float l2) {
		return 1f-(l1+((l2-l1)*(dist/(size/2))));
	}

	/**
	 * The method/algorithm to generate the cave.
	 * Calls randomize() in the beginning.
	 * The algorithm is repeated "repeat"-times.
	 * It calls decide() on each possible position on the grid.
	 * @param repeat the number of times to repeat the algorithm.
	 * @param leaning the leaning in the mid.
	 * @param leaningborder the leaning on the border.
	 */
	private void generate(int repeat, float leaning, float leaningborder) {
		randomize(leaning, leaningborder);
		for (int i = 0; i < repeat; i++) {
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					decide(x, y);
				}
			}
		}
	}

	/**
	 * Decides, whether to use set a specific point
	 * of the grid to true or false.
	 * Sets the position on the grid to true, when
	 * the number of neighbors being true is greater
	 * or equal to 5.
	 * @param x the x position on the grid.
	 * @param y the y position on the grid.
	 */
	private void decide(int x, int y) {
		int count = 0;
		for (int xi = x-1; xi <= x+1; xi++) {
			for (int yi = y-1; yi <= y+1; yi++) {
				if (get(xi, yi)) {
					count++;
				}
			}
		}
		if (count >= 5) {
			vals[x][y] = true;
		} else {
			vals[x][y] = false;
		}
	}

	/**
	 * @param x the x position on the grid.
	 * @param y the y position on the grid.
	 * @return if x and y are valid, then it returns the value on the grid
	 * at position [x][y], else it returns false.
	 */
	public boolean get(int x, int y) {
		return (x >= 0 && y >= 0 && x < size && y < size && vals[x][y]);
	}

}
