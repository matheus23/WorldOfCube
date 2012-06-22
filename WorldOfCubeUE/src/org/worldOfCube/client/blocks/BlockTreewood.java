package org.worldOfCube.client.blocks;

import java.awt.Color;

import org.worldOfCube.client.logic.chunks.Chunk;
import org.worldOfCube.client.res.ResLoader;

public class BlockTreewood extends Block {

	public static final int BLOCK_TEX = ResLoader.BLOCK_TREEWOOD;
	private static final Color awtCol = new Color(0x595d66);

	public BlockTreewood(byte x, byte y, Chunk c, boolean foreground) {
		super(x, y, c, foreground);
	}
	
	public BlockTreewood(boolean foreground) {
		super(foreground);
	}

	public void init() {
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

	public Color getAWTBackgroundColor() {
		return awtCol;
	}

	public boolean containsAlpha() {
		return borderID != ResLoader.FILLED;
	}

}
