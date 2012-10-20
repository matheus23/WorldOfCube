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
package org.worldOfCube.client.logic.entity.components;

import org.worldOfCube.client.logic.chunks.Chunk;
import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.logic.chunks.light.LightSource;
import org.worldOfCube.client.logic.chunks.light.RenderedLight;
import org.worldOfCube.client.res.ResLoader;

public class ComponentMovingLight {

	private class MovingLightSource implements LightSource {

		ComponentMovingLight comp;
		RenderedLight light;

		public MovingLightSource(ComponentMovingLight cml, RenderedLight light) {
			this.comp = cml;
			this.light = light;
		}

		@Override
		public float lightX() {
			return comp.x;
		}

		@Override
		public float lightY() {
			return comp.y;
		}

		@Override
		public RenderedLight getLight() {
			return light;
		}

	}

	private Chunk lastChunk;
	private MovingLightSource light;
	private int x, y;

	public ComponentMovingLight(RenderedLight rLight) {
		this.light = new MovingLightSource(this, rLight);
	}

	private Chunk computeChunk(int x, int y, World world) {
		int pixelsPerChunk = ResLoader.BLOCK_SIZE*world.getChunkManager().getChunkSize();
		int chunkx = x / pixelsPerChunk;
		int chunky = y / pixelsPerChunk;
		return world.getChunkManager().getChunk(chunkx, chunky);
	}

	/**
	 * @param x pixel-space world x coordinate.
	 * @param y pixel-space world y coordinate.
	 */
	public void tick(int x, int y, World world) {
		this.x = x;
		this.y = y;
		Chunk newChunk = computeChunk(x, y, world);
		if (lastChunk == null) {
			newChunk.registerLight(light);
		} else if (!newChunk.equals(lastChunk)) {
			lastChunk.removeLight(light);
			newChunk.registerLight(light);
		}
	}

}
