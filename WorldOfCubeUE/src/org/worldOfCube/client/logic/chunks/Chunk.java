package org.worldOfCube.client.logic.chunks;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.ArrayList;
import java.util.List;

import org.matheusdev.util.matrix.matrix2.MatrixN2f;
import org.matheusdev.util.matrix.matrix3.MatrixN3o;
import org.worldOfCube.client.blocks.Block;
import org.worldOfCube.client.blocks.BlockEarth;
import org.worldOfCube.client.blocks.BlockRock;
import org.worldOfCube.client.logic.chunks.generation.Generator;
import org.worldOfCube.client.logic.chunks.light.LightSource;
import org.worldOfCube.client.logic.collision.Rectangle;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.util.Rand;

public class Chunk {

	public static final int EARTH_OFFSET = 128;
	public static final int EARTH_SHUFFLE = 8;

	public static final int FRONT = 0;
	public static final int BACK = 1;

	private ChunkManager chunkManager;
	private int x;
	private int y;
	private boolean created = false;
	private MatrixN3o<Block> blocks;
	private MatrixN2f lightBuffer0;
	private MatrixN2f lightBuffer1;
	private MatrixN2f lightFront;
	private MatrixN2f lightBack;
	private List<LightSource> lights = new ArrayList<LightSource>(0);

	/**
	 * Constructor.
	 * @param x the chunk-space x position for the chunk.
	 * @param y the chunk-space y position for the chunk.
	 * @param cm the ChunkManager managing this Chunk.
	 */
	public Chunk(int x, int y, ChunkManager cm) {
		chunkManager = cm;
		this.x = x;
		this.y = y;
		lightBuffer0 = new MatrixN2f(chunkManager.csize, chunkManager.csize);
		lightBuffer1 = new MatrixN2f(chunkManager.csize, chunkManager.csize);
		lightFront = lightBuffer0;
		lightBack = lightBuffer1;
		blocks = new MatrixN3o<Block>(chunkManager.csize, chunkManager.csize, 2);
	}

	/**
	 * Create this Chunk according to information given in
	 * the Generator.
	 * If the Chunk was already created, it won't be created
	 * twice.
	 * @param g the Generator to create the Chunk with.
	 * @return whether the Chunk creation had to be done, or the
	 * Chunk was already created.
	 */
	public boolean create(Generator g) {
		if (!created) {
			createBlocks(g);
			created = true;
			return true;
		}
		return false;
	}

	/**
	 * Creates all Blocks for this Chunk, using a Generator
	 * and using Generator's isValid(int, int, int).
	 * @param g the Generator to create this Chunk with.
	 * @see org.worldOfCube.client.logic.chunks.generation.Generator#isValid(int, int, int)
	 */
	public void createBlocks(Generator g) {
		for (byte bx = 0; bx < chunkManager.csize; bx++) {
			for (byte by = 0; by < chunkManager.csize; by++) {
				int totalx = x * chunkManager.csize + bx;
				int totaly = y * chunkManager.csize + by;
				if (g.isValid(totalx, totaly)) {
					if (g.isValid(totalx, totaly, EARTH_OFFSET + Rand.rangeInt(EARTH_SHUFFLE, g.getRand()))) {
						if (!g.isCaveSet(totalx, totaly)) {
							blocks.set(new BlockRock(bx, by, this, true), bx, by, FRONT);
						}
						blocks.set(new BlockRock(bx, by, this, false), bx, by, BACK);
					} else {
						if (!g.isCaveSet(totalx, totaly)) {
							blocks.set(new BlockEarth(bx, by, this, true), bx, by, FRONT);
						}
						blocks.set(new BlockEarth(bx, by, this, false), bx, by, BACK);
					}
				}
			}
		}
	}

	/**
	 * Updates all Blocks in this Chunk.
	 * This Method is used in the initialization of the
	 * CunkManager, to make all Blocks be connected to each
	 * other.
	 */
	public void updateAll() {
		for (int x = 0; x < chunkManager.csize; x++) {
			for (int y = 0; y < chunkManager.csize; y++) {
				if (blocks.get(x, y, FRONT) != null) {
					blocks.get(x, y, FRONT).update();
				}
				if (blocks.get(x, y, BACK) != null) {
					blocks.get(x, y, BACK).update();
				}
			}
		}
	}

	/**
	 * @param src the LightSource to be tested.
	 * @return whether the LightSource already exists on
	 * this Chunk.
	 */
	public boolean containsLight(LightSource src) {
		return lights.contains(src);
	}

	/**
	 * Adds "src" to the internally used List.
	 * @param src the LightSoucre to be added.
	 */
	public void registerLight(LightSource src) {
		lights.add(src);
	}

	/**
	 * Removes "src" from the internally used List.
	 * @param src the LightSoucre to be removed.
	 */
	public void removeLight(LightSource src) {
		lights.remove(src);
	}

	/**
	 * @return how many lights the Chunk currently has.
	 */
	public int getLightNum() {
		return lights.size();
	}

	/**
	 * @param ind the index of the LightSource to get.
	 * @return the LightSource from a internally used List.
	 */
	public LightSource getLight(int ind) {
		return lights.get(ind);
	}

	/**
	 * Sets the Light value at (x, y) to "lightness".
	 * If "frontbuffer" is true, the change will be
	 * done right away, if not, it will "render" to the
	 * back buffer. Change buffers using flipLightBuffers().
	 * @param x the chunk-space x position.
	 * @param y teh chunk-space y position.
	 * @param lightness the brightness value to set the light to.
	 * @param frontbuffer whether to set it in the frontbuffer or not.
	 * @see org.worldOfCube.client.logic.chunks.Chunk#flipLightBuffers()
	 */
	public void setLight(byte x, byte y, float lightness, boolean frontbuffer) {
		if (frontbuffer) {
			lightFront.set(lightness, x, y);
		} else {
			lightBack.set(lightness, x, y);
		}
	}

	/**
	 * @param bx the chunk-space x position.
	 * @param by the chunk-space y position.
	 * @param frontbuffer whether to get it from the frontbuffer, or not.
	 * @return the light-value requested.
	 */
	public float getLight(byte bx, byte by, boolean frontbuffer) {
		if (bx < 0 || by < 0 || bx >= chunkManager.csize || by >= chunkManager.csize) {
			return chunkManager.getLightness(x*chunkManager.csize + bx, y*chunkManager.csize + by, frontbuffer);
		}
		return frontbuffer ? lightFront.get(bx, by) : lightBack.get(bx, by);
	}

	/**
	 * Calls Block.init() on all Blocks, this Chunk contains.
	 * @see org.worldOfCube.client.blocks.Block#init()
	 */
	public void initAll() {
		for (int x = 0; x < chunkManager.csize; x++) {
			for (int y = 0; y < chunkManager.csize; y++) {
				if (blocks.get(x, y, FRONT) != null) {
					blocks.get(x, y, FRONT).init();
				}
			}
		}
	}

	/**
	 * Resets the light on this Chunk to 0 in the specified
	 * buffer (either frontbuffer or backbuffer).
	 * @param frontbuffer whether to reset the light in.
	 */
	public void resetLight(boolean frontbuffer) {
		if (frontbuffer) {
			for (int x = 0; x < chunkManager.csize; x++) {
				for (int y = 0; y < chunkManager.csize; y++) {
					lightFront.set(0f, x, y);
				}
			}
		} else {
			for (int x = 0; x < chunkManager.csize; x++) {
				for (int y = 0; y < chunkManager.csize; y++) {
					lightBack.set(0f, x, y);
				}
			}
		}
	}

	/**
	 * The Lighting System is double Buffered.
	 * That makes it possible to Thread it without seeing
	 * bad artifacts.
	 * The Lighting is updated in the LightUpdater.
	 * To see how lighting is done, go there.
	 * @see org.worldOfCube.client.logic.chunks.light.LightUpdater
	 */
	public void flipLightBuffers() {
		if (lightFront == lightBuffer0) {
			lightFront = lightBuffer1;
			lightBack = lightBuffer0;
		} else {
			lightFront = lightBuffer0;
			lightBack = lightBuffer1;
		}
	}

	/**
	 * @param x the chunk-space x position.
	 * @param y the chunk-space y position.
	 * @param foreground whether to get a foreground or background Block.
	 * @return the Block at (x, y) in this Chunk, or "null", if (x, y) are
	 * not valid.
	 */
	public Block getLocalBlock(int x, int y, boolean foreground) {
		if (x >= 0 && y >= 0 && x < chunkManager.csize && y < chunkManager.csize) {
			return foreground ? blocks.get(x, y, FRONT) : blocks.get(x, y, BACK);
		}
		return null;
	}

	/**
	 * Sets a Local Block in this Chunk at (x, y) to "b".
	 * At first it destroys the previous block, if existing
	 * at (x, y). then it calls Block.create with the Chunk's
	 * this-instance, if the given Block "b", was not created before.
	 * Finally it adds "b" to the Chunk.
	 * @param x the chunk-space x position.
	 * @param y the chunk-space y position.
	 * @param b the Block to set at (x, y).
	 * @param foreground whether the block should be in foreground or background.
	 * @return whether the given position (x, y) was valid.
	 */
	public boolean setLocalBlock(byte x, byte y, Block b, boolean foreground) {
		if (x >= 0 && y >= 0 && x < chunkManager.csize && y < chunkManager.csize) {
			Block block = foreground ? blocks.get(x, y, FRONT) : blocks.get(x, y, BACK);
			if (block != null) {
				block.destroy();
			}
			if (b != null) {
				if (b.getChunk() == null) {
					b.create(x, y, this);
				}
			}
			if (foreground) {
				blocks.set(b, x, y, FRONT);
			} else {
				blocks.set(b, x, y, BACK);
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns a Block relative to the Chunk-coordinate system.
	 * That means, this method called with (0, 0), would return
	 * the top-left Block of this chunk, and (-1, 0) would return
	 * the top-right Block of the Chunk left to this Chunk.
	 * @param bx the relative chunk-space x position.
	 * @param by the relative chunk-space y position.
	 * @param foreground whether to get the Block from foreground or background.
	 * @return the Block from the requested position.
	 */
	public Block getBlock(int bx, int by, boolean foreground) {
		if (bx >= 0 && by >= 0 && bx < chunkManager.csize && by < chunkManager.csize) {
			return getLocalBlock(bx, by, foreground);
		} else {
			int totalx = x * chunkManager.csize + bx;
			int totaly = y * chunkManager.csize + by;
			int chunkx = totalx / chunkManager.csize;
			int chunky = totaly / chunkManager.csize;
			int finalx = totalx - (chunkx * chunkManager.csize);
			int finaly = totaly - (chunky * chunkManager.csize);
			if (chunkx >= 0 && chunky >= 0 && chunkx < chunkManager.size
					&& chunky < chunkManager.size && finalx >= 0 && finaly >= 0 && finalx < chunkManager.csize
					&& finaly < chunkManager.csize) {
				return chunkManager.getChunk(chunkx, chunky).getLocalBlock(finalx, finaly, foreground);
			}
			return null;
		}
	}

	/**
	 * Sets a Block relative to this Chunk's position to "b",
	 * the same way getBlock(int, int, boolean) works.
	 * Setting (0, 0) would set the Block at the top-left of this
	 * Chunk. Setting (-1, 0) would set the Block at the top-right
	 * of the Chunk to the left of this Chunk.
	 * @param bx the relative chunk-space x position.
	 * @param by the relative chunk-space y position.
	 * @param b the Block to set at (bx, by).
	 * @param foreground whether to set the Block in foreground or background.
	 * @return whether the Method actually has set a Block.
	 */
	public boolean setBlock(int bx, int by, Block b, boolean foreground) {
		if (bx >= 0 && by >= 0 && bx < chunkManager.csize && by < chunkManager.csize) {
			setLocalBlock((byte)bx, (byte)by, b, foreground);
			return true;
		} else {
			int totalx = x * chunkManager.csize + bx;
			int totaly = y * chunkManager.csize + by;
			int chunkx = totalx / chunkManager.csize;
			int chunky = totaly / chunkManager.csize;
			byte finalx = (byte) (totalx - (chunkx * chunkManager.csize));
			byte finaly = (byte) (totaly - (chunky * chunkManager.csize));
			if (chunkx >= 0 && chunky >= 0 && chunkx < chunkManager.size
					&& chunky < chunkManager.size && finalx >= 0 && finaly >= 0 && finalx < chunkManager.csize
					&& finaly < chunkManager.csize) {
				chunkManager.getChunk(chunkx, chunky).setLocalBlock(finalx, finaly, b, foreground);
				return true;
			}
			return false;
		}
	}

	/**
	 * Updates a Block using Chunk.getBlock(int, int boolean),
	 * so it works the same chunk-space relative way.
	 * @param x the relative chunk-space y position.
	 * @param y the relative chunks-space x position.
	 * @param foreground whether the Block to update is in foreground.
	 * @return whether the Method did successfully update a block, or not.
	 * @see org.worldOfCube.client.logic.chunks.Chunk#getBlock(int, int, boolean)
	 */
	public boolean updateBlock(int x, int y, boolean foreground) {
		Block b;
		if ((b = getBlock(x, y, foreground)) != null) {
			b.update();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Updates a local Block in local chunk-space. If the
	 * (x, y) position is out of range, an ArrayIndexOutOfBoundsException
	 * will be thrown.
	 * @param x the chunk-space x position.
	 * @param y the chunk-space y position.
	 * @param foreground whether to update a block in the foreground.
	 * @return whether the Method successfully updated a block.
	 */
	public boolean updateLocalBlock(int x, int y, boolean foreground) {
		Block b = foreground ? blocks.get(x, y, FRONT) : blocks.get(x, y, BACK);
		if (b != null) {
			b.update();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return the world-chunk-space x position.
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the world-chunk-space y position.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Updates all blocks on foreground and background
	 * in this diamond shape:
	 *  O
	 * OOO
	 *  O
	 * where "0"'s will be updated.
	 * This method uses {@link #updateBlock(int, int, boolean)},
	 * so it also calls updates to chunks relative to this one.
	 * @param x the chunk-space x position.
	 * @param y the chunk-space y position.
	 */
	public void updateDiamond(int x, int y) {
		updateBlock(x - 1, y, true); // Left foreground
		updateBlock(x + 1, y, true); // Right foreground
		updateBlock(x, y - 1, true); // Top foreground
		updateBlock(x, y + 1, true); // Bottom foreground
		updateBlock(x - 1, y, false); // Left background
		updateBlock(x + 1, y, false); // Right background
		updateBlock(x, y - 1, false); // Top background
		updateBlock(x, y + 1, false); // Bottom background
		updateBlock(x, y, true); // Mid foreground
		updateBlock(x, y, false); // Mid background
	}

	/**
	 * Adds a Block to this Chunk and automatically updates
	 * in a Diamond-shape around the position, the Block
	 * was placed.
	 * @param x the chunk-space x position.
	 * @param y the chunk-space y position.
	 * @param b the Block to be added.
	 * @param foreground whether to add in foreground, or background.
	 * @return true, if successful.
	 */
	public boolean addBlock(int x, int y, Block b, boolean foreground) {
		if (!setBlock(x, y, b, foreground)) {
			return false;
		}
		updateDiamond(x, y);
		return true;
	}

	/**
	 * Removes a Block at (x, y) and automatically updates
	 * in a Diamond-shape around that position, using
	 * {@link #setBlock(int, int, Block, boolean)} and {@link #updateDiamond(int, int)}.
	 * @param x the chunk-space x position.
	 * @param y the chunk-space y position.
	 * @param foreground whether to remove in foreground, or background.
	 * @return whether it was successful or not.
	 */
	public boolean removeBlock(int x, int y, boolean foreground) {
		if (!setBlock(x, y, null, foreground)) {
			return false;
		}
		updateDiamond(x, y);
		return true;
	}

	/**
	 * This method renders this chunk to the OpenGL PBO/FBO,
	 * using Features, deprecated in OpenGL 3.2+. This game
	 * is written, so it supports OpenGL 1.1 and lower.
	 * 
	 * At first, it pushes the Matrix Stack from OpenGL, which
	 * will be popped at the end. Then it Translates the
	 * Modelview-Matrix by the pixel-space position of this
	 * Chunk. After that it renders every Block, which is in
	 * the given viewport, using {@link #renderBlocksAt(int, int)}.
	 * @param wx viewport x position.
	 * @param wy viewport y position.
	 * @param ww viewport width.
	 * @param wh viewport height.
	 */
	public void render(Rectangle viewport) {
		glPushMatrix();
		{
			glTranslatef(x*chunkManager.csize*ResLoader.BLOCK_SIZE,
					y*chunkManager.csize*ResLoader.BLOCK_SIZE, 0f);

			int totalbx = ((int) viewport.x) / ResLoader.BLOCK_SIZE;
			int totalby = ((int) viewport.y) / ResLoader.BLOCK_SIZE;
			int beginx = totalbx - (x * chunkManager.csize);
			int beginy = totalby - (y * chunkManager.csize);
			int endx = beginx + ((int) viewport.w) / ResLoader.BLOCK_SIZE + 1;
			int endy = beginy + ((int) viewport.h) / ResLoader.BLOCK_SIZE + 1;
			beginx = Math.max(0, beginx);
			beginy = Math.max(0, beginy);
			endx = Math.min(chunkManager.csize-1, endx);
			endy = Math.min(chunkManager.csize-1, endy);

			for (int x = beginx; x <= endx; x++) {
				for (int y = beginy; y <= endy; y++) {
					renderBlocksAt(x, y);
				}
			}
		}
		glPopMatrix();
	}

	/**
	 * This renders a Block with OpenGL at (x, y).
	 * If (x, y) are not valid positions, the method
	 * will throw a NullPointerException.
	 * @param x
	 * @param y
	 */
	public void renderBlocksAt(int x, int y) {
		// If the Block in the front is not null, render it:
		if (blocks.get(x, y, FRONT) != null) {
			// If the Block in the front containsAlpha(), than the block behind is visible,
			// and must be rendered first (behind).
			if (blocks.get(x, y, FRONT).containsAlpha()) {
				// If the Block in the back is not null, render him.
				if (blocks.get(x, y, BACK) != null) {
					blocks.get(x, y, BACK).renderBackground();
				}
			}
			// Render the block in foreground.
			blocks.get(x, y, FRONT).render();
		} else {
			// If there is no Block in foreground, but a block in the background,
			// we simply render that one.
			if (blocks.get(x, y, BACK) != null) {
				blocks.get(x, y, BACK).renderBackground();
			}
		}
	}

	/**
	 * @return the size of this {@link Chunk}.
	 */
	public int getSize() {
		return chunkManager.csize;
	}

	/**
	 * @param bx the chunk-space block x position.
	 * @param by the chunk-space block y position.
	 * @param foreground whether to check in foreground or background.
	 * @return whether the position (bx, by) has a surrounding block in a diamond-shape.
	 */
	public boolean hasSurrounding(byte bx, byte by, boolean foreground) {
		return getLocalBlock(bx+1, by, foreground) != null
				|| getLocalBlock(bx-1, by, foreground) != null
				|| getLocalBlock(bx, by+1, foreground) != null
				|| getLocalBlock(bx, by-1, foreground) != null;
	}

}
