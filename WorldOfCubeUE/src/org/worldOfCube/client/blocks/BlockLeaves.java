package org.worldOfCube.client.blocks;

import java.awt.Color;

import org.worldOfCube.client.logic.chunks.Chunk;
import org.worldOfCube.client.res.ResLoader;

public class BlockLeaves extends Block {

	public static final int BLOCK_TEX = ResLoader.BLOCK_LEAVES;
	private static final Color awtCol = new Color(0x595d66);

	public BlockLeaves(byte x, byte y, Chunk c, boolean foreground) {
		super(x, y, c, foreground);
	}
	
	public BlockLeaves(boolean foreground) {
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
		return c.getBlock(x, y, foreground) instanceof BlockLeaves;
	}

	public Color getAWTBackgroundColor() {
		return awtCol;
	}

	public boolean containsAlpha() {
		return true;
	}

}
