package org.worldOfCube.client.logic.chunks.generation.trees;

import java.util.Random;

import org.worldOfCube.client.blocks.BlockLeaves;
import org.worldOfCube.client.blocks.BlockTreewood;
import org.worldOfCube.client.logic.chunks.ChunkManager;
import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.util.bresenhamline.PlaceAction;
import org.worldOfCube.client.util.bresenhamline.Placer;

/**
 * Class for generating the little Trees in WorldOfCube.
 * Implements TreeGenerator.
 * This Class does not provide Root-Generation, for that,
 * see the FineRootGenerator.
 * @author matheusdev
 * @see org.worldOfCube.client.logic.chunks.generation.trees.FineRootGenerator
 */
public class LittleTreeGenerator implements TreeGenerator {

	private class TreewoodPlacer implements PlaceAction {

		private ChunkManager cm;

		public TreewoodPlacer(ChunkManager cm) {
			this.cm = cm;
		}

		@Override
		public void action(int x, int y) {
			cm.addBlock(x, y, new BlockTreewood(false), false);
//			cm.addBlock(x+1, y, new BlockTreewood(false), false);
//			cm.addBlock(x+1, y+1, new BlockTreewood(false), false);
//			cm.addBlock(x, y+1, new BlockTreewood(false), false);
		}
	}

	private class LeavesPlacer implements PlaceAction {

		private ChunkManager cm;

		public LeavesPlacer(ChunkManager cm) {
			this.cm = cm;
		}

		@Override
		public void action(int x, int y) {
			cm.addBlock(x, y, new BlockLeaves(true), true);
		}
	}

	protected final Random rand;
	protected final TreewoodPlacer twp;
	protected final LeavesPlacer lp;
	protected final World world;

	public LittleTreeGenerator(Random rand, World world) {
		this.rand = rand;
		this.world = world;
		twp = new TreewoodPlacer(world.getChunkManager());
		lp = new LeavesPlacer(world.getChunkManager());
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.logic.chunks.generation.trees.TreeGenerator#getWoodPlacer()
	 */
	@Override
	public double getMinimalDist() {
		return 3.0;
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.logic.chunks.generation.trees.TreeGenerator#getWoodPlacer()
	 */
	@Override
	public double deriveRotation(double prerot) {
		return prerot + (rand.nextBoolean() ? -(rand.nextDouble()*20.0+20.0) : rand.nextDouble()*20.0+20.0);
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.logic.chunks.generation.trees.TreeGenerator#getWoodPlacer()
	 */
	@Override
	public double deriveDistance(double predist) {
		return predist / (1.0 + (rand.nextDouble() / 2.0));
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.logic.chunks.generation.trees.TreeGenerator#getWoodPlacer()
	 */
	@Override
	public void build(TreeLog tl, Random rand) {
		if (tl.childs.size() != 0) {
			for (int i = 0; i < tl.childs.size(); i++) {
				Placer.line((int)tl.pos.x, (int)tl.pos.y, (int)tl.childs.get(i).pos.x, (int)tl.childs.get(i).pos.y, twp);
			}
		} else {
			Placer.circle((int)tl.pos.x, (int)tl.pos.y, 2.0 + rand.nextDouble(), lp, world);
		}
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.logic.chunks.generation.trees.TreeGenerator#getWoodPlacer()
	 */
	@Override
	public PlaceAction getWoodPlacer() {
		return twp;
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.logic.chunks.generation.trees.TreeGenerator#getLeavesPlacer()
	 */
	@Override
	public PlaceAction getLeavesPlacer() {
		return lp;
	}

}
