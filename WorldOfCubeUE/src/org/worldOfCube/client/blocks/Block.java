package org.worldOfCube.client.blocks;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Color;

import org.worldOfCube.client.logic.chunks.Chunk;
import org.worldOfCube.client.logic.collision.Rectangle;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.res.Sprite;
import org.worldOfCube.client.util.Config;
import org.worldOfCube.client.util.StateManager;
import org.worldOfCube.client.util.Var;

public abstract class Block {

	public static Rectangle[] collisions = new Rectangle[] {
		new Rectangle(0, 0, ResLoader.BLOCK_SIZE, ResLoader.BLOCK_SIZE)
	};
	public static final float BACKGROUND_DARKNESS = 0.2f;
	public static final float BG_DIVIDE = 1.2f;
	public static float DRAW_SIZE = (float)ResLoader.BLOCK_SIZE; 
	
	protected byte x;
	protected byte y;
	protected byte borderID;
	protected boolean foreground;
	protected Chunk c;

	/**
	 * Constructor.
	 * @param x chunk space x position
	 * @param y chunk space y position
	 * @param c the chunk to add the block to
	 * @param foreground whether the block is in foreground or not
	 */
	public Block(byte x, byte y, Chunk c, boolean foreground) {
		this.x = x;
		this.y = y;
		this.c = c;
		this.foreground = foreground;
	}
	
	/**
	 * Create a block with undefined position and chunk.
	 * Used for creating blocks when adding blocks to the world
	 * without knowing where the block will be placed at first.
	 * @param foreground
	 * @see org.worldOfCube.client.logic.chunks.ChunkManager#addBlock(int, int, Block, boolean)
	 * @see org.worldOfCube.client.logic.chunks.Chunk#addBlock(int, int, Block, boolean)
	 * @see org.worldOfCube.client.blocks.Block#create(byte, byte, Chunk)
	 */
	public Block(boolean foreground) {
		c = null;
		this.foreground = foreground;
	}
	
	/**
	 * Used when the block was created through the foreground-only
	 * Constructor.
	 * @param x chunk space x position
	 * @param y chunk space y position
	 * @param c the chunk to add the block to
	 * @see org.worldOfCube.client.blocks.Block#Block(boolean)
	 */
	public void create(byte x, byte y, Chunk c) {
		this.x = x;
		this.y = y;
		this.c = c;
	}

	/**
	 * Override this for special actions.
	 * At default this only recalculates the borderID.
	 * Gets called when a block nearby get added,
	 * removed or in other special cases.
	 */
	public void update() {
		borderID = calculateBorder();
	}
	
	/**
	 * @return an awt Color instance, which will be used 
	 * for displaying blocks in the server's WorldViewer
	 * @see org.worldOfCube.server.gui.viewer.WorldViewer
	 */
	public abstract Color getAWTBackgroundColor();

	/**
	 * Called, when a block gets rendered by a chunk
	 * @see org.worldOfCube.client.logic.chunks.Chunk#render(float, float, float, float)
	 */
	public abstract void render();
	
	/**
	 * Called, when a block gets rendered by a chunk and
	 * "foreground" is false.
	 * @see org.worldOfCube.client.logic.chunks.Chunk#render(float, float, float, float)
	 */
	public abstract void renderBackground();
	
	/**
	 * Called, after a block got created.
	 */
	public abstract void init();
	
	/**
	 * Key for creating blocks which fit to a group of other block types. 
	 * @param x world space x-position of the other block
	 * @param y world space y-position of the other block
	 * @return whether the block is a valid neighbor or not
	 */
	public abstract boolean isValidNeighbor(int x, int y);
	
	public boolean containsAlpha() {
		return false;
	}
	
	/**
	 * @return the number of times this block reduces the light 
	 * (Foregroundblock's standard: 5 * 0.01)
	 * (Backgroundblock's standard: 0.2 * 0.01);
	 */
	public float getLightWallness() {
		return foreground ? 8f : 0.2f;
	}
	
	/**
	 * Standard collision rect array is "collisions".
	 * @return a number of Rectangles, which represent the collidable
	 * area of this specific block.
	 * @see org.worldOfCube.client.blocks.Block#collisions
	 * @see org.worldOfCube.client.logic.collision.Rectangle
	 */
	public Rectangle[] getCollisionRects() {
		collisions[0].x = (c.getX() * c.getSize() + x) * ResLoader.BLOCK_SIZE;
		collisions[0].y = (c.getY() * c.getSize() + y) * ResLoader.BLOCK_SIZE;
		return collisions;
	}
	
	/**
	 * A method to be overridden.
	 * Gets called, when a block gets destroyed.
	 */
	public void destroy() {
	}
	
	/**
	 * @return the chunk the block is added to,
	 * or null, if the block was not created yet.
	 */
	public Chunk getChunk() {
		return c;
	}
	
	/**
	 * @return the borderID used from the ResLoader to get the
	 * right sprite to be drawn.
	 * @see org.worldOfCube.client.res.ResLoader
	 */
	public int getBorderID() {
		return borderID;
	}

	/**
	 * Internal help-method to render a Block with a given sprite.
	 * @param sprite the sprite, which is a representation of this block.
	 */
	protected void renderIntern(Sprite sprite) {
		boolean newRendering = Config.get("block_rendering").equals("vao");
		glPushMatrix();
		glTranslatef(
				x * ResLoader.BLOCK_SIZE, 
				y * ResLoader.BLOCK_SIZE, 0f);
		Var.col1 = c.getLight(x, y, true);
		Var.col2 = c.getLight((byte)(x+1), y, true);
		Var.col3 = c.getLight((byte)(x+1), (byte)(y+1), true);
		Var.col4 = c.getLight(x, (byte)(y+1), true);
		
		renderSpriteIntern(sprite, newRendering);
		
		glPopMatrix();
	}

	/**
	 * Internal help-method to render a Block with a given sprite as background block.
	 * @param sprite the sprite, which is a representation of this block.
	 */
	protected void renderBackgroundIntern(Sprite sprite) {
		boolean newRendering = Config.get("block_rendering").equals("new");
		glPushMatrix();
		glTranslatef(x * ResLoader.BLOCK_SIZE, 
					y * ResLoader.BLOCK_SIZE, 0f);
		Var.col1 = Math.min(1f, c.getLight(x, y, true)) / BG_DIVIDE;
		Var.col2 = Math.min(1f, c.getLight((byte)(x+1), y, true)) / BG_DIVIDE;
		Var.col3 = Math.min(1f, c.getLight((byte)(x+1), (byte)(y+1), true)) / BG_DIVIDE;
		Var.col4 = Math.min(1f, c.getLight(x, (byte)(y+1), true)) / BG_DIVIDE;
		
		renderSpriteIntern(sprite, newRendering);
		
		glPopMatrix();
	}
	
	/**
	 * Internal method to render a sprite with a given "techneque". 
	 * @param sprite which sprite to use for rendering.
	 * @param vao whether to render this block as vao, or not
	 */
	private void renderSpriteIntern(Sprite sprite, boolean vao) {
		if (!vao) {
			if (containsAlpha() || Var.col1 != 0f || Var.col2 != 0f || Var.col3 != 0f || Var.col4 != 0f) {
				sprite.bind();
				glBegin(GL_QUADS);
				{
					glColor3f(Var.col1, Var.col1, Var.col1);
					sprite.glTexCoord(0);
					glVertex2f(0f, 0f);
	
					glColor3f(Var.col2, Var.col2, Var.col2);
					sprite.glTexCoord(1);
					glVertex2f(DRAW_SIZE, 0f);
	
					glColor3f(Var.col3, Var.col3, Var.col3);
					sprite.glTexCoord(2);
					glVertex2f(DRAW_SIZE, DRAW_SIZE);
	
					glColor3f(Var.col4, Var.col4, Var.col4);
					sprite.glTexCoord(3);
					glVertex2f(0f, DRAW_SIZE);
				}
				glEnd();
			} else {
				glColor3f(0f, 0f, 0f);
				StateManager.bindTexture(null);
				glBegin(GL_QUADS);
				{
					glVertex2f(0f, 0f);
					glVertex2f(DRAW_SIZE, 0f);
					glVertex2f(DRAW_SIZE, DRAW_SIZE);
					glVertex2f(0f, DRAW_SIZE);
				}
				glEnd();
			}
		} else {
			if (containsAlpha() || Var.col1 != 0f || Var.col2 != 0f || Var.col3 != 0f || Var.col4 != 0f) {
				sprite.bind();
				ResLoader.getBlockRenderer(borderID).setColor(Var.col1, Var.col2, Var.col3, Var.col4);
				ResLoader.getBlockRenderer(borderID).render();
			} else {
				StateManager.bindTexture(null);
				ResLoader.getBlockRenderer(borderID).setColor(0f, 0f, 0f, 0f);
				ResLoader.getBlockRenderer(borderID).render();
			}
		}
	}
	
	/**
	 * Calls isValidNeighbor() for every neighbor in a diamond-shape,
	 * to calculate the borderID, used to render the right sprite
	 * from the ResLoader.
	 * @return the borderID.
	 */
	protected byte calculateBorder() {
		boolean left = isValidNeighbor(x-1, y);
		boolean right = isValidNeighbor(x+1, y);
		boolean top = isValidNeighbor(x, y-1);
		boolean bottom = isValidNeighbor(x, y+1);
		if (left) {
			if (right) {
				if (top) {
					if (bottom) {
						return ResLoader.FILLED;
					} else {
						return ResLoader.BORDER_BOTTOM;
					}
				} else {
					if (bottom) {
						return ResLoader.BORDER_TOP;
					} else {
						return ResLoader.HPIPE;
					}
				}
			} else {
				if (top) {
					if (bottom) {
						return ResLoader.BORDER_RIGHT;
					} else {
						return ResLoader.CORNER_BOTTOMRIGHT;
					}
				} else {
					if (bottom) {
						return ResLoader.CORNER_TOPRIGHT;
					} else {
						return ResLoader.RIGHT;
					}
				}
			}
		} else {
			if (right) {
				if (top) {
					if (bottom) {
						return ResLoader.BORDER_LEFT;
					} else {
						return ResLoader.CORNER_BOTTOMLEFT;
					}
				} else {
					if (bottom) {
						return ResLoader.CORNER_TOPLEFT;
					} else {
						return ResLoader.LEFT;
					}
				}
			} else {
				if (top) {
					if (bottom) {
						return ResLoader.VPIPE;
					} else {
						return ResLoader.BOTTOM;
					}
				} else {
					if (bottom) {
						return ResLoader.TOP;
					} else {
						return ResLoader.ALONE;
					}
				}
			}
		}
	}
	
}
