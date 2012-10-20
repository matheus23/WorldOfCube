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
package org.worldOfCube.client.logic.chunks.generation.trees;

import java.util.Random;

import org.worldOfCube.client.util.bresenhamline.PlaceAction;

/**
 * Used by "Tree", to generate Trees with different looks.
 * @author matheusdev
 * @see org.worldOfCube.client.logic.chunks.generation.trees.Tree
 */
public interface TreeGenerator {

	/**
	 * @param predist the distance of the previous Log.
	 * @return a new distance, which should always be
	 * smaller than the previous distance. Else it could
	 * lead to a never-ending recursive Algorithm, causing
	 * a StackOverflow.
	 */
	public double deriveDistance(double predist);

	/**
	 * Derive the distance of the previous Log.
	 * @param prerot the rotation of the previous Log.
	 * @return the new Rotation derived from prerot.
	 */
	public double deriveRotation(double prerot);

	/**
	 * @return the Log-length, at which Logs won't grow
	 * any further anymore.
	 */
	public double getMinimalDist();

	/**
	 * Used to build a TreeLog to a World (in a representation of Blocks).
	 * @param tl the Log to be build.
	 * @param rand an instance of Random to create random values.
	 */
	public void build(TreeLog tl, Random rand);

	public PlaceAction getWoodPlacer();
	public PlaceAction getLeavesPlacer();

}
