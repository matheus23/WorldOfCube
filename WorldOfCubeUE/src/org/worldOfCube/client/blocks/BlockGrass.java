package org.worldOfCube.client.blocks;

import java.awt.Color;

import org.worldOfCube.client.logic.chunks.Chunk;
import org.worldOfCube.client.res.ResLoader;

public class BlockGrass extends Block {

	public static final int BLOCK_TEX = ResLoader.BLOCK_GRASS;

	private static final Color awtCol = new Color(0x487634);
	
	public BlockGrass(byte x, byte y, Chunk c, boolean foreground) {
		super(x, y, c, foreground);
	}
	
	public BlockGrass(boolean foreground) {
		super(foreground);
	}

	public void init() {
		if (borderID == ResLoader.FILLED) {
			replaceWithEarth();
		}
	}

	public void update() {
		super.update();
		if (borderID == ResLoader.FILLED) {
			replaceWithEarth();
		}
	}

	public void render() {
		super.renderIntern(ResLoader.get(BLOCK_TEX, borderID));
	}
	
	public void renderBackground() {
		super.renderBackgroundIntern(ResLoader.get(BLOCK_TEX, borderID));
	}

	public boolean isValidNeighbor(int x, int y) {
		Block b = c.getBlock(x, y, foreground);
		if (b != null) {
			return b instanceof BlockTreewood 
					|| b instanceof BlockGrass 
					|| b instanceof BlockEarth
					|| b instanceof BlockRock
					|| b instanceof BlockWood;
		}
		return false;
	}
	
	public boolean containsAlpha() {
		return borderID != ResLoader.FILLED;
	}
	
	public void destroy() {
	}

	public void replaceWithEarth() {
		BlockEarth eb = new BlockEarth(x, y, c, foreground);
		c.setLocalBlock(x, y, eb, foreground);
		eb.update();
	}

	public Color getAWTBackgroundColor() {
		return awtCol;
	}

}
