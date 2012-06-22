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
		
		public float lightX() {
			return comp.x;
		}
		
		public float lightY() {
			return comp.y;
		}
		
		public int chunkX() {
			return comp.x % world.getChunkManager().csize;
		}
		
		public int chunkY() {
			return comp.y % world.getChunkManager().csize;
		}
		
		public RenderedLight getLight() {
			return light;
		}
		
	}
	
	private Chunk lastChunk;
	private World world;
	private MovingLightSource light;
	private int x, y;
	
	public ComponentMovingLight(World world, RenderedLight rLight) {
		this.world = world;
		this.light = new MovingLightSource(this, rLight);
	}
	
	private Chunk computeChunk(int x, int y) {
		int pixelsPerChunk = ResLoader.BLOCK_SIZE*world.getChunkManager().csize;
		int chunkx = x / pixelsPerChunk;
		int chunky = y / pixelsPerChunk;
		return world.getChunkManager().getChunk(chunkx, chunky);
	}
	
	/**
	 * @param x pixel-space world x coordinate.
	 * @param y pixel-space world y coordinate.
	 */
	public void tick(int x, int y) {
		this.x = x;
		this.y = y;
		Chunk newChunk = computeChunk(x, y);
		if (lastChunk == null) {
			newChunk.registerLight(light);
		} else if (!newChunk.equals(lastChunk)) {
			lastChunk.removeLight(light);
			newChunk.registerLight(light);
		}
	}

}
