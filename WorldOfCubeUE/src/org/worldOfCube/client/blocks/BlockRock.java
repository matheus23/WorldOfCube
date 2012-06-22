package org.worldOfCube.client.blocks;

import java.awt.Color;

import org.worldOfCube.client.logic.chunks.Chunk;
import org.worldOfCube.client.res.ResLoader;

public class BlockRock extends Block {

	public static final int BLOCK_TEX = ResLoader.BLOCK_ROCK;
	private static final Color awtCol = new Color(0x595d66);

	public BlockRock(byte x, byte y, Chunk c, boolean foreground) {
		super(x, y, c, foreground);
	}
	
	public BlockRock(boolean foreground) {
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
		return c.getBlock(x, y, foreground) instanceof BlockRock;
	}

	public Color getAWTBackgroundColor() {
		return awtCol;
	}

	public char getBlockID() {
		return 3;
	}
	
}
