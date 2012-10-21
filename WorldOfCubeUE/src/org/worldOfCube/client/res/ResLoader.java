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
package org.worldOfCube.client.res;

import static org.lwjgl.opengl.ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.universeengine.opengl.texture.UniTexture;
import org.universeengine.opengl.texture.UniTextureLoader;
import org.universeengine.opengl.texture.UniTextureLoader.DecodePack;
import org.worldOfCube.Log;
import org.worldOfCube.client.util.StateManager;
import org.worldOfCube.client.util.opengl.BlockVAO;

public final class ResLoader {

	public static final String res = "org/worldOfCube/resources/";

	/*
	 * SpriteSheet ID's:
	 */
	public static enum Sheets {
		BLOCKS,
		GUI_BORDER_BLUE,
		GUI_BORDER_NORMAL,
		GUI_LOADBAR,
		GUI_INV_SLOT,
		PLAYER_SHEET;
	}

	/*
	 * Sprite ID's:
	 */
	public static final int BORDER_SIZE = 16;
	public static enum Borders {
		GUI_BORDER_TL,
		GUI_BORDER_T,
		GUI_BORDER_TR,
		GUI_BORDER_L,
		GUI_MID,
		GUI_BORDER_R,
		GUI_BORDER_BL,
		GUI_BORDER_B,
		GUI_BORDER_BR;
	}

	public static final int INV_SLOT_SIZE = 32;
	public static enum Slots {
		UNSELECTED,
		SELECTED;
	}

	public static enum Loadbars {
		LEFT,
		RIGHT,
		MID;
	}

	public static enum PlayerParts {
		ARM,
		HEAD,
		BODY,
		LEG_FRONT,
		LEG_BACK,
		HEAD_MINI;
	}

	public static enum TileTypes {
		ALONE,
		TOP,
		BOTTOM,
		LEFT,
		RIGHT,
		HPIPE,
		VPIPE,
		CORNER_TOPLEFT,
		CORNER_TOPRIGHT,
		CORNER_BOTTOMRIGHT,
		CORNER_BOTTOMLEFT,
		BORDER_TOP,
		BORDER_BOTTOM,
		BORDER_LEFT,
		BORDER_RIGHT,
		FILLED;
	}

	public static final int BLOCK_PART_SIZE = 64;
	public static final int BLOCK_SIZE = 16;
	public static enum Blocks {
		EARTH,
		GRASS,
		LEAVES,
		LIGHTSTONE,
		ROCK,
		TREEWOOD,
		WOOD;
	}

	private static SpriteSheet[] sheets = new SpriteSheet[Sheets.values().length];
	private static BlockVAO[] blocks = new BlockVAO[Blocks.values().length];

	public static UniTexture guiBackground;
	// Titlescreen-Background
	public static UniTexture tsBackground;
	//	public static UniTexture cursor;

	public static void load() {
		Log.out("Using texture rect = " + StateManager.isUsingTexRect());
		UniTextureLoader.flipImages = false;
		//TODO: Blocks: Add to sheet, give sprites.
		sheets[Sheets.BLOCKS.ordinal()] = new SpriteSheet(res + "blocks/blocks.png", Blocks.values().length * TileTypes.values().length, StateManager.isUsingTexRect());
		splitUpBlocks(sheets[Sheets.BLOCKS.ordinal()]);

		loadBlockRenderers();

		sheets[Sheets.PLAYER_SHEET.ordinal()] = new SpriteSheet(res + "sprites/player/mike_ripped.png", PlayerParts.values().length, StateManager.isUsingTexRect());
		splitPlayerSprite(sheets[Sheets.PLAYER_SHEET.ordinal()]);

		sheets[Sheets.GUI_BORDER_BLUE.ordinal()] = new SpriteSheet(res + "gui/border_blue.png", 9, StateManager.isUsingTexRect());
		sheets[Sheets.GUI_BORDER_NORMAL.ordinal()] = new SpriteSheet(res + "gui/border_normal.png", 9, StateManager.isUsingTexRect());
		splitUpGUIBorder(sheets[Sheets.GUI_BORDER_BLUE.ordinal()]);
		splitUpGUIBorder(sheets[Sheets.GUI_BORDER_NORMAL.ordinal()]);

		sheets[Sheets.GUI_LOADBAR.ordinal()] = new SpriteSheet(res + "gui/loadbar.png", 3, StateManager.isUsingTexRect());
		splitUpGUILoadBar(sheets[Sheets.GUI_LOADBAR.ordinal()]);

		sheets[Sheets.GUI_INV_SLOT.ordinal()] = new SpriteSheet(res + "gui/inv_tile.png", 2, StateManager.isUsingTexRect());
		splitUpGUIInvSlots(sheets[Sheets.GUI_INV_SLOT.ordinal()]);

		glEnable(GL_TEXTURE_2D);
		String loadpath = res + "gui/backgroundtile.png";
		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(loadpath);
		try {
			if (stream == null || stream.available() == 0) {
				Log.err("Could not open InputStream from " + loadpath);
			}
		} catch (IOException e) {
			Log.err("Could not open InputStream from " + loadpath);
			e.printStackTrace();
		}
		DecodePack pack = UniTextureLoader.loadImageBufferPNG(stream);
		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, pack.width, pack.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pack.data);
		guiBackground = new UniTexture(id, pack.width, pack.height);
		glBindTexture(GL_TEXTURE_2D, 0);

		//		cursor = loadTex(res + "Cursor.png", false);
	}

	public static void loadTitle() {
		tsBackground = loadTex(res + "WorldOfCubeTitlescreen.png", false);
	}

	public static void unloadTitle() {
		tsBackground.delete();
		tsBackground = null;
	}

	public static void unload() {
		for (int i = 0; i < sheets.length; i++) {
			if (sheets[i] != null) {
				sheets[i].delete();
				sheets[i] = null;
			}
		}
		guiBackground.delete();
	}

	private static void splitPlayerSprite(SpriteSheet sheet) {
		sheet.giveSprite(PlayerParts.ARM.ordinal(), 		 0,  0,  7, 13);
		sheet.giveSprite(PlayerParts.HEAD.ordinal(), 		 7,  0, 26, 29);
		sheet.giveSprite(PlayerParts.BODY.ordinal(), 		33,  0, 14, 19);
		sheet.giveSprite(PlayerParts.LEG_FRONT.ordinal(), 	47,  0,  9, 13);
		sheet.giveSprite(PlayerParts.LEG_BACK.ordinal(), 	47, 13,  9, 13);
		sheet.giveSprite(PlayerParts.HEAD_MINI.ordinal(), 	33, 19, 15, 15);
	}

	private static void splitUpBlocks(SpriteSheet sheet) {
		splitUpBlocksOffset(sheet, Blocks.EARTH,		 0, 0);
		splitUpBlocksOffset(sheet, Blocks.GRASS,		 1, 0);
		splitUpBlocksOffset(sheet, Blocks.ROCK,			 2, 0);
		splitUpBlocksOffset(sheet, Blocks.TREEWOOD,		 3, 0);
		splitUpBlocksOffset(sheet, Blocks.WOOD,			 4, 0);
		splitUpBlocksOffset(sheet, Blocks.LEAVES,		 5, 0);
		splitUpBlocksOffset(sheet, Blocks.LIGHTSTONE,	 6, 0);
	}

	private static void splitUpBlocksOffset(SpriteSheet sheet, Blocks block, int xPos, int yPos) {
		splitUp(sheet, block, xPos * BLOCK_PART_SIZE, yPos * BLOCK_PART_SIZE);
	}

	private static void splitUp(SpriteSheet sheet, Blocks block, float xoff, float yoff) {
		sheet.giveSprite(getIndex(block, TileTypes.ALONE),				  0 + xoff,  0 + yoff, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(getIndex(block, TileTypes.CORNER_TOPLEFT),		 16 + xoff,  0 + yoff, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(getIndex(block, TileTypes.BORDER_TOP),			 32 + xoff,  0 + yoff, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(getIndex(block, TileTypes.CORNER_TOPRIGHT),	 48 + xoff,  0 + yoff, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(getIndex(block, TileTypes.TOP),				  0 + xoff, 16 + yoff, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(getIndex(block, TileTypes.BORDER_LEFT),		 16 + xoff, 16 + yoff, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(getIndex(block, TileTypes.FILLED),				 32 + xoff, 16 + yoff, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(getIndex(block, TileTypes.BORDER_RIGHT),		 48 + xoff, 16 + yoff, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(getIndex(block, TileTypes.BOTTOM),				  0 + xoff, 32 + yoff, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(getIndex(block, TileTypes.CORNER_BOTTOMLEFT),	 16 + xoff, 32 + yoff, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(getIndex(block, TileTypes.BORDER_BOTTOM),		 32 + xoff, 32 + yoff, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(getIndex(block, TileTypes.CORNER_BOTTOMRIGHT),  48 + xoff, 32 + yoff, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(getIndex(block, TileTypes.LEFT),				  0 + xoff, 48 + yoff, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(getIndex(block, TileTypes.RIGHT),				 16 + xoff, 48 + yoff, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(getIndex(block, TileTypes.VPIPE),				 32 + xoff, 48 + yoff, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(getIndex(block, TileTypes.HPIPE),				 48 + xoff, 48 + yoff, BLOCK_SIZE, BLOCK_SIZE);
	}

	public static int getIndex(Blocks block, TileTypes type) {
		return getIndex(block, type.ordinal());
	}

	public static int getIndex(Blocks block, int borderID) {
		return (block.ordinal() * TileTypes.values().length) + borderID;
	}

	private static void loadBlockRenderers() {
		for (int i = 0; i < Blocks.values().length; i++) {
			blocks[i] = new BlockVAO(i);
		}
	}

	private static void splitUpGUIBorder(SpriteSheet sheet) {
		sheet.giveSprite(Borders.GUI_BORDER_TL.ordinal(),	 0,  0,  BORDER_SIZE, BORDER_SIZE);
		sheet.giveSprite(Borders.GUI_BORDER_T.ordinal(),	16,  0,  BORDER_SIZE, BORDER_SIZE);
		sheet.giveSprite(Borders.GUI_BORDER_TR.ordinal(),	32,  0,  BORDER_SIZE, BORDER_SIZE);
		sheet.giveSprite(Borders.GUI_BORDER_L.ordinal(),	 0,  16, BORDER_SIZE, BORDER_SIZE);
		sheet.giveSprite(Borders.GUI_MID.ordinal(),			16,  16, BORDER_SIZE, BORDER_SIZE);
		sheet.giveSprite(Borders.GUI_BORDER_R.ordinal(),	32,  16, BORDER_SIZE, BORDER_SIZE);
		sheet.giveSprite(Borders.GUI_BORDER_BL.ordinal(),	 0,  32, BORDER_SIZE, BORDER_SIZE);
		sheet.giveSprite(Borders.GUI_BORDER_B.ordinal(),	16,  32, BORDER_SIZE, BORDER_SIZE);
		sheet.giveSprite(Borders.GUI_BORDER_BR.ordinal(),	32,  32, BORDER_SIZE, BORDER_SIZE);
	}

	private static void splitUpGUIInvSlots(SpriteSheet sheet) {
		sheet.giveSprite(Slots.UNSELECTED.ordinal(), 0, 0, INV_SLOT_SIZE, INV_SLOT_SIZE);
		sheet.giveSprite(Slots.SELECTED.ordinal(), 0, INV_SLOT_SIZE, INV_SLOT_SIZE, INV_SLOT_SIZE);
	}

	private static void splitUpGUILoadBar(SpriteSheet sheet) {
		sheet.giveSprite(Loadbars.LEFT.ordinal(), 0, 0, 4, 8);
		sheet.giveSprite(Loadbars.MID.ordinal(), 4, 0, 8, 8);
		sheet.giveSprite(Loadbars.RIGHT.ordinal(), 12, 0, 4, 8);
	}

	public static UniTexture loadTex(String loadpath, boolean texRect) {
		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(loadpath);
		try {
			if (stream == null || stream.available() == 0) {
				Log.err("Could not open InputStream from " + loadpath);
			}
		} catch (IOException e) {
			Log.err("Could not open InputStream from " + loadpath);
			e.printStackTrace();
		}
		DecodePack pack = UniTextureLoader.loadImageBufferPNG(stream);

		glEnable(GL_TEXTURE_2D);
		int id = glGenTextures();
		glBindTexture(texRect ? GL_TEXTURE_RECTANGLE_ARB : GL_TEXTURE_2D, id);
		if (texRect) {
			glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		} else {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		}
		glTexImage2D(texRect ? GL_TEXTURE_RECTANGLE_ARB : GL_TEXTURE_2D, 0, GL_RGBA,
				pack.width, pack.height,
				0, GL_RGBA, GL_UNSIGNED_BYTE, pack.data);
		glBindTexture(texRect ? GL_TEXTURE_RECTANGLE_ARB : GL_TEXTURE_2D, 0);

		return new UniTexture(id, pack.width, pack.height);
	}

	public static Sprite get(int spritesheet, int sprite) {
		return sheets[spritesheet].getSprite(sprite);
	}

	public static Sprite get(Blocks block, TileTypes type) {
		return sheets[Sheets.BLOCKS.ordinal()].getSprite(getIndex(block, type));
	}

	public static Sprite get(Blocks block, int borderID) {
		return sheets[Sheets.BLOCKS.ordinal()].getSprite(getIndex(block, borderID));
	}

	public static Sprite get(Sheets sheet, int sprite) {
		return sheets[sheet.ordinal()].getSprite(sprite);
	}

	public static BlockVAO getBlockRenderer(int borderID) {
		return blocks[borderID];
	}

	public static SpriteSheet getSheet(Sheets sheet) {
		return sheets[sheet.ordinal()];
	}

	public static BufferedImage loadBufferedImage(String path) {
		BufferedImage copy = null;
		try {
			GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
			BufferedImage buffImg = ImageIO.read(Thread.currentThread().getContextClassLoader().getResource(path));
			copy = gc.createCompatibleImage(buffImg.getWidth(), buffImg.getHeight(), buffImg.getTransparency());
			Graphics2D g2d = copy.createGraphics();
			g2d.drawImage(buffImg, 0, 0, null);
			g2d.dispose();
		} catch (IOException e) {
			System.err.println("Failed to load BufferedImage " + path);
			e.printStackTrace();
			System.exit(1);
		}
		return copy;
	}

}
