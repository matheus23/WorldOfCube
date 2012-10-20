package org.worldOfCube.client.logic.chunks.generation.trees;

import java.util.Random;

import org.worldOfCube.client.logic.chunks.ChunkManager;
import org.worldOfCube.client.util.PointUtil;
import org.worldOfCube.client.util.bresenhamline.Placer;

public class Tree {

	protected final TreeLog firstLog;
	protected final TreeLog firstRoot;
	protected final TreeGenerator treeGen;
	protected final TreeGenerator rootGen;

	/**
	 * Creates a new Tree, using a position and two TreeGenerators
	 * for the tree itself, or the roots.
	 * Calls a recursive log-system algorithm to create the Logs.
	 * @param x the x position
	 * @param y the y position
	 * @param rot the rotation of the first log.
	 * @param dist the length of the first log.
	 * @param treeGenerator the TreeGenerator for the tree.
	 * @param rootGenerator the TreeGenerator for the roots.
	 * @see org.worldOfCube.client.logic.chunks.generation.trees.TreeGenerator
	 */
	public Tree(int x, int y, double rot, double dist, TreeGenerator treeGenerator, TreeGenerator rootGenerator) {
		this.treeGen = treeGenerator;
		this.rootGen = rootGenerator;
		firstLog = new TreeLog(PointUtil.moveDirection(new Pos(x, y), rot, dist), rot, dist, treeGenerator);
		double rootrot = rot+180.0;
		double rootdist = dist*0.6;
		firstRoot = new TreeLog(PointUtil.moveDirection(new Pos(x, y), rootrot, rootdist), rootrot, rootdist, rootGenerator);
	}

	/**
	 * Places blocks on the given ChunkManager to create a
	 * blocky representation of this tree.
	 * @param cm the ChunkManager to create the Blocks to.
	 * @param rand an instance of Random to generate random values.
	 */
	public void build(ChunkManager cm, Random rand) {
		Placer.line((int) firstRoot.pos.x, (int) firstRoot.pos.y, (int) firstLog.pos.x, (int) firstLog.pos.y, treeGen.getWoodPlacer());
		firstLog.build(rand);
		firstRoot.build(rand);
	}

}
