package org.worldOfCube.client.logic.chunks.light;

import org.lwjgl.input.Keyboard;
import org.worldOfCube.Log;
import org.worldOfCube.client.blocks.Block;
import org.worldOfCube.client.logic.chunks.Chunk;
import org.worldOfCube.client.logic.chunks.ChunkManager;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.util.debug.PerfMonitor;

public class LightUpdater {
	
	private class UpdaterThread extends Thread {
		volatile int beginx;
		volatile int beginy;
		volatile int endx;
		volatile int endy;
		
		UpdaterThread() {
			setDaemon(true);
			setName("LightUpdater Thread");
			start();
		}
		
		public void run() {
			while (true) {
				PerfMonitor.startProfile("LIGHT UPDATE");
				// Resetting all lights:
				for (int x = beginx; x < endx; x++) {
					for (int y = beginy; y < endy; y++) {
						cManager.getChunk(x, y).resetLight(false);
						if (Thread.currentThread().isInterrupted()) {
							return;
						}
					}
				}
				// "Flood-filling" (light source at every empty tile):
				for (int x = beginx; x < endx; x++) {
					for (int y = beginy; y < endy; y++) {
						floodFillLight(cManager.getChunk(x, y), sunlight);
						if (Thread.currentThread().isInterrupted()) {
							return;
						}
					}
				}
				// Calculating light from sources:
				for (int x = beginx; x < endx; x++) {
					for (int y = beginy; y < endy; y++) {
						Chunk c = cManager.getChunk(x, y);
						for (int i = 0; i < c.getLightNum(); i++) {
							updateLightSource(
									(int)(c.getLight(i).lightX()/ResLoader.BLOCK_SIZE),
									(int)(c.getLight(i).lightY()/ResLoader.BLOCK_SIZE), 
									c.getLight(i).getLight());
							if (Thread.currentThread().isInterrupted()) {
								return;
							}
						}
					}
				}
				for (int x = beginx; x < endx; x++) {
					for (int y = beginy; y < endy; y++) {
						cManager.getChunk(x, y).flipLightBuffers();
					}
				}
				PerfMonitor.stopProfile("LIGHT UPDATE");
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) { 
					Log.out(this, "LightUpdater-Thread got interrupted. Exiting normally."); 
					return; 
				}
			}
		}
		
		void update(int bx, int by, int ex, int ey) {
			beginx = bx;
			beginy = by;
			endx = ex;
			endy = ey;
		}
	}
	
	public static RenderedLight sunlight = new RenderedLight(8, 1f);
	
	private ChunkManager cManager;
	private int lastbeginx;
	private int lastbeginy;
	private int lastendx;
	private int lastendy;
	private UpdaterThread updater;
	
	/**
	 * Constructor.
	 * 
	 * @param cm an instance of ChunkManager, to effect lights on Chunks.
	 */
	public LightUpdater(ChunkManager cm) {
		cManager = cm;
		updater = new UpdaterThread();
	}
	
	/**
	 * @param brightness the brightness to set the sunlight to.
	 */
	public void setSunlight(float brightness) {
		sunlight = new RenderedLight(8, Math.max(0f, brightness));
	}
	
	/**
	 * Force recalculating the light on the whole World.
	 * (Takes a lot of time, should not be called often.)
	 */
	public void recalcLight() {
		cManager.resetLight(false);
		floodFillLight(sunlight);
		updateAllSources();
	}
	
	/**
	 * WARNING!: If you do not call this method, the "World" instance,
	 * this Class's Thread holds, will never be garbage-collected, resulting
	 * in something like a memory-leak.
	 */
	public void destroy() {
		updater.interrupt();
	}
	
	public void tick(int wx, int wy, int ww, int wh) {
		//TODO: remove.
		Keyboard.poll();
		boolean needupdate = false;
		while (Keyboard.next()) {
			char c = Keyboard.getEventCharacter();
			if (c == '+') {
				setSunlight(sunlight.getStrength()+0.05f);
				needupdate = true;
			} else if (c == '-') {
				setSunlight(sunlight.getStrength()-0.05f);
				needupdate = true;
			} else if (c == '=') {
				setSunlight(1f);
				needupdate = true;
			}
		}
		if (needupdate) {
			update(wx, wy, ww, wh, true);
		}
	}
	
	/**
	 * If the field of view of chunks, or something changed in the world.
	 * Then this method tells the Updater thread, that the current Light
	 * representation is Dirty.
	 * The whole Lighting system is double-buffered, to make Threaded 
	 * Lighting look good. See the class Chunk for more information.
	 * 
	 * @param wx current pixel-space view x coordinate.
	 * @param wy current pixel-space view y coordinate.
	 * @param ww current viewport width in pixels.
	 * @param wh current viewport height in pixels.
	 * @param force when true, it does update, even when the view does not change.
	 * @see org.worldOfCube.client.logic.chunks.Chunk
	 */
	public void update(int wx, int wy, int ww, int wh, boolean force) {
		int beginx = wx/(cManager.csize*ResLoader.BLOCK_SIZE)-1;
		int beginy = wy/(cManager.csize*ResLoader.BLOCK_SIZE)-1;
		int endx = (wx+ww)/(cManager.csize*ResLoader.BLOCK_SIZE)+2;
		int endy = (wy+wh)/(cManager.csize*ResLoader.BLOCK_SIZE)+2;
		beginx = Math.max(0, beginx);
		beginy = Math.max(0, beginy);
		endx = Math.min(cManager.size-1, endx);
		endy = Math.min(cManager.size-1, endy);
		if (force
				|| beginx != lastbeginx
				|| beginy != lastbeginy
				|| endx != lastendx
				|| endy != lastendy) {
			updater.update(beginx, beginy, endx, endy);
		}
		lastbeginx = beginx;
		lastbeginy = beginy;
		lastendx = endx;
		lastendy = endy;
	}
	
	/**
	 * Calls updateLightSource on all lights in all Chunks.
	 * This Method should not be used frequently, because
	 * it is really slow for big Worlds.
	 */
	private void updateAllSources() {
		for (int x = 0; x < cManager.size; x++) {
			for (int y = 0; y < cManager.size; y++) {
				Chunk c = cManager.getChunk(x, y);
				for (int i = 0; i < c.getLightNum(); i++) {
					updateLightSource(
							(int)(c.getLight(i).lightX()/ResLoader.BLOCK_SIZE),
							(int)(c.getLight(i).lightY()/ResLoader.BLOCK_SIZE),
							c.getLight(i).getLight());
				}
			}
		}
	}
	
	/**
	 * Updates a light source. 
	 * 
	 * @param lx pixel-space light x position.
	 * @param ly pixel-space light y position.
	 * @param rl a pre-rendered Representation of a this Light.
	 */
	private void updateLightSource(int lx, int ly, RenderedLight rl) {
		recursiveLight(lx, ly, lx, ly, 0f, 1f, rl);
	}

	/**
	 * This is an Algorithm to calculate a Light, which 
	 * takes Walls into account. Blocks will stop light
	 * by a specific amount (specified by Block.getLightWallness()).
	 * 
	 * @param x the current world-space x position the Method is working on, used for the recursive Iteration.
	 * @param y the current world-space y position the Method is working on, used for the recursive Iteration.
	 * @param midx the middle world-space x position from the light source.
	 * @param midy the middle world-space y position from the ligth source.
	 * @param walls how much/strong this iteration has gone through walls.
	 * @param lastLight the last calculated brightness of the caller. 
	 * @param light a pre-rendered Light to read values from.
	 */
	private void recursiveLight(int x, int y, int midx, int midy, float walls, float lastLight, RenderedLight light) {
		if (!cManager.isValidPosition(x, y)) return;
		Block fg = cManager.getBlock(x, y, true);
		Block bg = cManager.getBlock(x, y, false);
		if (fg != null) walls += fg.getLightWallness();
		if (bg != null) walls += bg.getLightWallness();
		
		float newLight = light.getMidRelative(midx-x, midy-y)-(walls*0.01f);
		
		if (cManager.getLightness(x, y, false) < newLight) {
			cManager.setLightness(x, y, newLight, false);
			recursiveLight(x-1, y, midx, midy, walls, newLight, light);
			recursiveLight(x+1, y, midx, midy, walls, newLight, light);
			recursiveLight(x, y-1, midx, midy, walls, newLight, light);
			recursiveLight(x, y+1, midx, midy, walls, newLight, light);
		}
	}
	
	/**
	 * This Method will "flood-fill" the Light on the whole World.
	 * Do not call this method often. This Method takes the most
	 * time to calculate.
	 * To calculate the light, this calls "floodFillLight(Chunk, RenderedLight)",
	 * with the given argument "light" and every Chunk form the ChunkManager.
	 *  
	 * @param light the pre-rendered light to use as surface-light.
	 */
	private void floodFillLight(RenderedLight light) {
		for (int cx = 0; cx < cManager.size; cx++) {
			for (int cy = 0; cy < cManager.size; cy++) {
				floodFillLight(cManager.getChunk(cx, cy), light);
			}
		}
	}
	
	/**
	 * This Method "flood-fills" the light on the given Chunk.
	 * But it is actually not a flood-fill.
	 * If the foreground and background Blocks are null,
	 * then it sets the light at (x, y) to "light".getStrength().
	 * If the above is the case AND (x, y) has a surrounding block,
	 * then it updates a light source with updateLightSource(int, int, RenderedLight).
	 * 
	 * @param c the Chunk to calculate the surface lights for.
	 * @param light the pre-rendered surface light to use.
	 */
	private void floodFillLight(Chunk c, RenderedLight light) {
		for (byte x = 0; x < cManager.csize; x++) {
			for (byte y = 0; y < cManager.csize; y++) {
				if (c.getLocalBlock(x, y, true) == null
						&& c.getLocalBlock(x, y, false) == null) {
					if (hasSurrounding(c, x, y, false)
							|| hasSurrounding(c, x, y, true)) {
						updateLightSource(
								c.getX()*cManager.csize+x, 
								c.getY()*cManager.csize+y, 
								light);
					}
					c.setLight(x, y, light.getStrength(), false);
				}
			}
		}
	}
	
	private boolean hasSurrounding(Chunk c, int x, int y, boolean foreground) {
		return c.getBlock(x-1, y, foreground) != null 
				|| c.getBlock(x+1, y, foreground) != null
				|| c.getBlock(x, y-1, foreground) != null
				|| c.getBlock(x, y+1, foreground) != null;
	}

}
