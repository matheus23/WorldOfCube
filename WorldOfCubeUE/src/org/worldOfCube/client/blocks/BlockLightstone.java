package org.worldOfCube.client.blocks;

import java.awt.Color;

import org.worldOfCube.client.logic.chunks.Chunk;
import org.worldOfCube.client.logic.chunks.light.LightSource;
import org.worldOfCube.client.logic.chunks.light.RenderedLight;
import org.worldOfCube.client.res.ResLoader;

public class BlockLightstone extends Block implements LightSource {
	
	private static final int BLOCK_TEX = ResLoader.BLOCK_LIGHTSTONE;
	private static final Color awtCol = new Color(1f, 1f, 0.5f);
	public static final RenderedLight light = new RenderedLight(16, 1f);

	public BlockLightstone(byte x, byte y, Chunk c, boolean foreground) {
		super(x, y, c, foreground);
		if (foreground) {
			c.registerLight(this);
		}
	}
	
	public BlockLightstone(boolean foreground) {
		super(foreground);
	}
	
	public void create(byte x, byte y, Chunk c) {
		super.create(x, y, c);
		if (foreground) {
			c.registerLight(this);
		}
	}

	public void init() {
	}
	
	public void destroy() {
		c.removeLight(this);
	}
	
	public void update() {
		super.update();
		if (!foreground) {
			Block above = c.getLocalBlock(x, y, true);
			if (above == null && !c.containsLight(this)) {
				c.registerLight(this);
			} else if (above != null && c.containsLight(this)) {
				c.removeLight(this);
			}
		}
	}

	public void render() {
		renderIntern(ResLoader.get(BLOCK_TEX, borderID));
	}
	
	public void renderBackground() {
		renderBackgroundIntern(ResLoader.get(BLOCK_TEX, borderID));
	}
	
	public boolean containsAlpha() {
		return borderID != ResLoader.FILLED;
	}
	
	public boolean isValidNeighbor(int x, int y) {
		return c.getBlock(x, y, foreground) instanceof BlockLightstone;
	}
	
	public Color getAWTBackgroundColor() {
		return awtCol;
	}

	public float lightX() {
		return (c.getX()*c.getSize()+x)*ResLoader.BLOCK_SIZE+ResLoader.BLOCK_SIZE/2;
	}

	public float lightY() {
		return (c.getY()*c.getSize()+y)*ResLoader.BLOCK_SIZE+ResLoader.BLOCK_SIZE/2;
	}
	
	public int chunkX() {
		return x;
	}
	
	public int chunkY() {
		return y;
	}

	public RenderedLight getLight() {
		return light;
	}

}
