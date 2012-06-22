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
