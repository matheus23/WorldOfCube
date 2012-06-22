package org.worldOfCube.client.logic.chunks.generation.trees;

import java.util.Random;

import org.worldOfCube.client.blocks.BlockTreewood;
import org.worldOfCube.client.logic.chunks.ChunkManager;
import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.util.bresenhamline.PlaceAction;
import org.worldOfCube.client.util.bresenhamline.Placer;

/**
 * A class for generating the little Roots.
 * This is almost the same as FineTreeGenerator, except for not
 * creating Leaves-patches.
 * Implements TreeGenerator 
 * @author matheusdev
 * @see org.worldOfCube.client.logic.chunks.generation.trees.TreeGenerator
 */
public class LittleRootGenerator implements TreeGenerator {
	
	private class TreewoodPlacer implements PlaceAction {
		
		private ChunkManager cm;
		
		public TreewoodPlacer(ChunkManager cm) {
			this.cm = cm;
		}
		
		public void action(int x, int y) {
			cm.addBlock(x, y, new BlockTreewood(false), false);
			cm.addBlock(x+1, y, new BlockTreewood(false), false);
			cm.addBlock(x+1, y+1, new BlockTreewood(false), false);
			cm.addBlock(x, y+1, new BlockTreewood(false), false);
		}
	}
	
	private Random rand;
	private TreewoodPlacer twp;
	
	public LittleRootGenerator(Random rand, World world) {
		this.rand = rand;
		twp = new TreewoodPlacer(world.getChunkManager());
	}
	
	public double getMinimalDist() {
		return 3.0;
	}
	
	public double deriveRotation(double prerot) {
		return prerot + (rand.nextBoolean() ? -(rand.nextDouble()*20.0+20.0) : rand.nextDouble()*20.0+20.0);
	}
	
	public double deriveDistance(double predist) {
		return predist / (1.0+(rand.nextDouble()/8.0));
	}
	
	public void build(TreeLog tl, Random rand) {
		for (int i = 0; i < tl.childs.size(); i++) {
			Placer.line((int)tl.pos.x, (int)tl.pos.y, (int)tl.childs.get(i).pos.x, (int)tl.childs.get(i).pos.y, twp);
		}
	}

}
