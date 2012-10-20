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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.worldOfCube.client.util.PointUtil;

public class TreeLog {

	public TreeGenerator treegen;
	public Pos pos;
	public List<TreeLog> childs = new ArrayList<TreeLog>(2);

	/**
	 * Creates a Log and recursively adds Logs to this log as children.
	 * @param pos the starting position of this Log.
	 * @param rotation the rotation of this Log.
	 * @param dist the length of this Log.
	 * @param tg the TreeGenerator to get derived Rotations/Lengths from,
	 * to create children.
	 */
	public TreeLog(final Pos pos, final double rotation, final double dist, final TreeGenerator tg) {
		this.pos = pos;
		this.treegen = tg;
		if (dist > tg.getMinimalDist()) {
			double newrot1 = tg.deriveRotation(rotation);
			double newrot2 = tg.deriveRotation(rotation);
			double newdist1 = tg.deriveDistance(dist);
			double newdist2 = tg.deriveDistance(dist);
			childs.add(new TreeLog(PointUtil.moveDirection(pos, newrot1, newdist1), newrot1, newdist1, tg));
			childs.add(new TreeLog(PointUtil.moveDirection(pos, newrot2, newdist2), newrot2, newdist2, tg));
		}
	}

	public void build(Random rand) {
		treegen.build(this, rand);
		for (int i = 0; i < childs.size(); i++) {
			childs.get(i).build(rand);
		}
	}

}
