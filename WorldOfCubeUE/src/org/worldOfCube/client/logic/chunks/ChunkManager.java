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
package org.worldOfCube.client.logic.chunks;

import org.worldOfCube.Log;
import org.worldOfCube.client.blocks.Block;
import org.worldOfCube.client.logic.chunks.generation.Generator;
import org.worldOfCube.client.logic.collision.Rectangle;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.util.Timer;

public class ChunkManager {

	public final int pixelPerChunk;

	protected final int size;
	protected final int csize;

	protected Chunk[][] chunks;

	private long loaded;
	private long toLoad = 1;

	/**
	 * Constructor. This will not create any instance of Chunks.
	 * This will only Initialize the size of the Arrays and
	 * some of the variables.
	 * It is only possible to create square-Worlds.
	 * @param size the number of Chunks on both x and y axis.
	 * @param csize the number of Blocks a Chunk has, on both x and y axis.
	 */
	public ChunkManager(int size, int csize) {
		this.size = size;
		this.csize = csize;
		this.pixelPerChunk = ResLoader.BLOCK_SIZE*csize;
		chunks = new Chunk[size][size];
		toLoad = size*size;
	}

	public int getSize() {
		return size;
	}

	public int getChunkSize() {
		return csize;
	}

	/**
	 * Creates a World with a given Generator, calling
	 * Chunk's {@link Chunk#createBlocks(Generator)}.
	 * At first it calls {@link #initChunks()}, then
	 * {@link #createChunks(Generator)}, then
	 * {@link #initAll()}.
	 * @param g the Generator for the Generation information.
	 */
	public void create(Generator g) {
		initChunks();
		Log.out("Creating chunks with world size " + size + " and chunk size " + csize);
		Timer t = new Timer().start();
		createChunks(g);
		initAll();
		Log.out("Creation took " + t.stop() + " milliseconds");
	}

	/**
	 * Calls the Constructor for every possible position in the World.
	 * It also increases the "loaded" counter.
	 */
	public void initChunks() {
		loaded = 0;
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				chunks[x][y] = new Chunk(x, y, this);
				loaded += 1;
			}
		}
	}

	/**
	 * calls {@link Chunk#create(Generator)} on each Chunk in the
	 * Array. It also increases the "loaded" counter.
	 * @param g
	 */
	private void createChunks(Generator g) {
		loaded = 0;
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				chunks[x][y].create(g);
				loaded += 1;
			}
		}
	}

	/**
	 * Renders all chunks, which "collide" with the given viewport.
	 * @param wx viewport x position.
	 * @param wy viewport y position.
	 * @param ww viewport width.
	 * @param wh viewport height.
	 */
	public void renderChunks(Rectangle viewport) {
		int beginx = (int)(viewport.x/pixelPerChunk);
		int beginy = (int)(viewport.y/pixelPerChunk);
		int endx = (int)((viewport.x+viewport.w)/pixelPerChunk)+1;
		int endy = (int)((viewport.y+viewport.h)/pixelPerChunk)+1;
		beginx = Math.max(0, beginx);
		beginy = Math.max(0, beginy);
		endx = Math.min(size, endx);
		endy = Math.min(size, endy);
		for (int x = beginx; x < endx; x++) {
			for (int y = beginy; y < endy; y++) {
				chunks[x][y].render(viewport);
			}
		}
	}

	/**
	 * Calls {@link Chunk#updateAll()} on all Chunks.
	 */
	public void updateAll() {
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				chunks[x][y].updateAll();
			}
		}
	}

	/**
	 * Calls {@link Chunk#initAll()} on all Chunks.
	 */
	private void initAll() {
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				chunks[x][y].initAll();
			}
		}
	}

	/**
	 * Returns a Block from world-space coordinates.
	 * @param totalx world-space x position.
	 * @param totaly world-space y position.
	 * @param foreground whether the block should be get from foreground, or background.
	 * @return the Block, if the given position is actually valid, else "null".
	 */
	public Block getBlock(int totalx, int totaly, boolean foreground) {
		int chunkx = totalx / csize;
		int chunky = totaly / csize;
		int finalx = totalx - (chunkx*csize);
		int finaly = totaly - (chunky*csize);
		if (chunkx >= 0 && chunky >= 0 && chunkx < size && chunky < size
				&& finalx >= 0 && finaly >= 0 && finalx < csize && finaly < csize
				&& chunks[chunkx][chunky] != null) {
			return chunks[chunkx][chunky].getLocalBlock(finalx, finaly, foreground);
		}
		return null;
	}

	/**
	 * Sets a Block in a world-space position.
	 * @param totalx world-space x position.
	 * @param totaly world-space y position.
	 * @param b the Block to set (totalx, totaly) to.
	 * @param foreground whether to set it in foreground.
	 * @return the Block form that position.
	 */
	public boolean setBlock(int totalx, int totaly, Block b, boolean foreground) {
		int chunkx = totalx / csize;
		int chunky = totaly / csize;
		byte finalx = (byte)(totalx - (chunkx*csize));
		byte finaly = (byte)(totaly - (chunky*csize));
		if (chunkx >= 0 && chunky >= 0 && chunkx < size && chunky < size
				&& finalx >= 0 && finaly >= 0 && finalx < csize && finaly < csize
				&& chunks[chunkx][chunky] != null) {
			chunks[chunkx][chunky].setLocalBlock(finalx, finaly, b, foreground);
			return true;
		}
		return false;
	}

	/**
	 * Does the same as {@link Chunk#setBlock(int, int, Block, boolean)},
	 * but in world-space.
	 * @param totalx world-space x position.
	 * @param totaly world-space y position.
	 * @param b the Block to add.
	 * @param foreground whether to add in foreground, or background.
	 * @return whether the method did successfully add a block, or not.
	 */
	public boolean addBlock(int totalx, int totaly, Block b, boolean foreground) {
		int chunkx = totalx / csize;
		int chunky = totaly / csize;
		byte finalx = (byte)(totalx - (chunkx*csize));
		byte finaly = (byte)(totaly - (chunky*csize));
		if (chunkx >= 0 && chunky >= 0 && chunkx < size && chunky < size
				&& finalx >= 0 && finaly >= 0 && finalx < csize && finaly < csize
				&& chunks[chunkx][chunky] != null) {
			chunks[chunkx][chunky].setLocalBlock(finalx, finaly, b, foreground);
			chunks[chunkx][chunky].updateDiamond(finalx, finaly);
			return true;
		}
		return false;
	}

	/**
	 * Does the same as {@link Chunk#removeBlock(int, int, boolean)},
	 * but in world-space.
	 * @param totalx world-space x position.
	 * @param totaly world-space y position.
	 * @param foreground whether to remove in foreground, or background.
	 * @return the Block, which got removed.
	 */
	public Block removeBlock(int totalx, int totaly, boolean foreground) {
		int chunkx = totalx / csize;
		int chunky = totaly / csize;
		byte finalx = (byte)(totalx - (chunkx*csize));
		byte finaly = (byte)(totaly - (chunky*csize));
		if (chunkx >= 0 && chunky >= 0 && chunkx < size && chunky < size
				&& finalx >= 0 && finaly >= 0 && finalx < csize && finaly < csize
				&& chunks[chunkx][chunky] != null) {
			Block b = chunks[chunkx][chunky].getLocalBlock(finalx, finaly, foreground);
			chunks[chunkx][chunky].setLocalBlock(finalx, finaly, null, foreground);
			chunks[chunkx][chunky].updateDiamond(finalx, finaly);
			return b;
		}
		return null;
	}

	/**
	 * Sets the brightness of light on world-space positions.
	 * @param totalx world-space x position.
	 * @param totaly world-space y position.
	 * @param lightness the brightness of light to set the light value to.
	 * @param frontbuffer whether to set in front- or back-buffer.
	 * @return whether the operation has successfully set the light, or not.
	 */
	public boolean setLightness(int totalx, int totaly, float lightness, boolean frontbuffer) {
		int chunkx = totalx / csize;
		int chunky = totaly / csize;
		byte finalx = (byte)(totalx - (chunkx*csize));
		byte finaly = (byte)(totaly - (chunky*csize));
		if (chunkx >= 0 && chunky >= 0 && chunkx < size && chunky < size
				&& finalx >= 0 && finaly >= 0 && finalx < csize && finaly < csize
				&& chunks[chunkx][chunky] != null) {
			chunks[chunkx][chunky].setLight(finalx, finaly, lightness, frontbuffer);
			return true;
		}
		return false;
	}

	/**
	 * @param totalx world-space x position.
	 * @param totaly world-space y position.
	 * @param frontbuffer whether to get the light value from the front- or back-buffer.
	 * @return the lightness at world-space (totalx, totaly).
	 */
	public float getLightness(int totalx, int totaly, boolean frontbuffer) {
		int chunkx = totalx / csize;
		int chunky = totaly / csize;
		byte finalx = (byte)(totalx - (chunkx*csize));
		byte finaly = (byte)(totaly - (chunky*csize));
		if (chunkx >= 0 && chunky >= 0 && chunkx < size && chunky < size
				&& finalx >= 0 && finaly >= 0 && finalx < csize && finaly < csize
				&& chunks[chunkx][chunky] != null) {
			return chunks[chunkx][chunky].getLight(finalx, finaly, frontbuffer);
		}
		return 0;
	}

	/**
	 * @param totalx the world-block-space x position.
	 * @param totaly the world-block-space y position.
	 * @return the Chunk, which would contain the total world-space position Block.
	 */
	public Chunk getChunkFromBlockCoords(int totalx, int totaly) {
		int chunkx = totalx / csize;
		int chunky = totaly / csize;
		if (chunkx >= 0 && chunky >= 0 && chunkx < size && chunky < size
				&& chunks[chunkx][chunky] != null) {
			return chunks[chunkx][chunky];
		}
		return null;
	}

	/**
	 * Resets the Light on all Chunks, calling {@link Chunk#resetLight(boolean)}.
	 * @param frontbuffer whether to reset in front- or back-buffer.
	 */
	public void resetLight(boolean frontbuffer) {
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				if (chunks[x][y] != null) {
					chunks[x][y].resetLight(frontbuffer);
				}
			}
		}
	}

	/**
	 * throws an ArrayIndexOutOfBoundsException, if the position is not valid.
	 * @param x world-space Chunk x position.
	 * @param y world-space Chunk y position.
	 * @return the Chunk at (x, y).
	 */
	public Chunk getChunk(int x, int y) {
		return chunks[x][y];
	}

	/**
	 * @return the Load process in %, used by the loading bar, and {@link SingleplayerWorldTHEFUCK#getLoaded()}.
	 */
	public float getLoadProgress() {
		return ((float)loaded/(float)toLoad)*100f;
	}

	/**
	 * @param x the world-space Chunk x position.
	 * @param y the world-space Chunk y position.
	 * @return whether the given position would be valid.
	 */
	public boolean isValidPosition(int x, int y) {
		return (x >= 0 && y >= 0 && x < csize*size && y < csize*size);
	}

}
