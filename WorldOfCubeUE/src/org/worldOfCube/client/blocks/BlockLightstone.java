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

	@Override
	public void create(byte x, byte y, Chunk c) {
		super.create(x, y, c);
		if (foreground) {
			c.registerLight(this);
		}
	}

	@Override
	public void init() {
	}

	@Override
	public void destroy() {
		c.removeLight(this);
	}

	@Override
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

	@Override
	public void render() {
		renderIntern(ResLoader.get(BLOCK_TEX, borderID));
	}

	@Override
	public void renderBackground() {
		renderBackgroundIntern(ResLoader.get(BLOCK_TEX, borderID));
	}

	@Override
	public boolean containsAlpha() {
		return borderID != ResLoader.FILLED;
	}

	@Override
	public boolean isValidNeighbor(int x, int y) {
		return c.getBlock(x, y, foreground) instanceof BlockLightstone;
	}

	@Override
	public Color getAWTBackgroundColor() {
		return awtCol;
	}

	@Override
	public float lightX() {
		return (c.getX()*c.getSize()+x)*ResLoader.BLOCK_SIZE+ResLoader.BLOCK_SIZE/2;
	}

	@Override
	public float lightY() {
		return (c.getY()*c.getSize()+y)*ResLoader.BLOCK_SIZE+ResLoader.BLOCK_SIZE/2;
	}

	public int chunkX() {
		return x;
	}

	public int chunkY() {
		return y;
	}

	@Override
	public RenderedLight getLight() {
		return light;
	}

}
