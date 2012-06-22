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

import javax.imageio.ImageIO;

import org.universeengine.opengl.texture.UniTexture;
import org.universeengine.opengl.texture.UniTextureLoader;
import org.universeengine.opengl.texture.UniTextureLoader.DecodePack;
import org.worldOfCube.Log;
import org.worldOfCube.client.util.StateManager;
import org.worldOfCube.client.util.opengl.BlockVAO;

public final class ResLoader {
	
	/*
	 * SpriteSheet ID's:
	 */
	public static final int BLOCK_EARTH = 		0;
	public static final int BLOCK_GRASS = 		1;
	public static final int BLOCK_ROCK = 		2;
	public static final int BLOCK_LIGHTSTONE = 	3;
	public static final int BLOCK_TREEWOOD = 	4;
	public static final int BLOCK_LEAVES = 		5;
	public static final int BLOCK_WOOD = 		6;
	
	public static final int GUI_BORDER_BLUE = 	32;
	public static final int GUI_BORDER_NORMAL = 33;
	
	public static final int GUI_LOADBAR = 		34;
	
	public static final int GUI_INV_SLOT = 		40;
	
	public static final int PLAYER_SHEET = 		48;
	
	/*
	 * Sprite ID's:
	 */
	public static final int GUI_BORDER_TL = 0;
	public static final int GUI_BORDER_T = 1;
	public static final int GUI_BORDER_TR = 2;
	public static final int GUI_BORDER_L = 3;
	public static final int GUI_MID = 4;
	public static final int GUI_BORDER_R = 5;
	public static final int GUI_BORDER_BL = 6;
	public static final int GUI_BORDER_B = 7;
	public static final int GUI_BORDER_BR = 8;
	
	public static final int GUI_BORDER_S = 16;
	
	public static final int GUI_INV_SLOT_UNSEL = 0;
	public static final int GUI_INV_SLOT_SEL = 1;
	
	public static final int GUI_INV_SLOT_SIZE = 32;
	
	public static final int GUI_LOADBAR_LEFT = 0;
	public static final int GUI_LOADBAR_RIGHT = 1;
	public static final int GUI_LOADBAR_MID = 2;
	
	public static final int ARM = 0;
	public static final int HEAD = 1;
	public static final int BODY = 2;
	public static final int LEG_FRONT = 3;
	public static final int LEG_BACK = 4;
	public static final int HEAD_MINI = 5;
	
	public static final int BLOCK_SIZE = 16;
	
	public static final byte ALONE = 0;
	public static final byte TOP = 1;
	public static final byte BOTTOM = 2;
	public static final byte LEFT = 3;
	public static final byte RIGHT = 4;
	public static final byte HPIPE = 5;
	public static final byte VPIPE = 6;
	public static final byte CORNER_TOPLEFT = 7;
	public static final byte CORNER_TOPRIGHT = 8;
	public static final byte CORNER_BOTTOMRIGHT = 9;
	public static final byte CORNER_BOTTOMLEFT = 10;
	public static final byte BORDER_TOP = 11;
	public static final byte BORDER_BOTTOM = 12;
	public static final byte BORDER_LEFT = 13;
	public static final byte BORDER_RIGHT = 14;
	public static final byte FILLED = 15;
	
	public static final int NUM_BLOCK_TYPES = 16;
	
	private static SpriteSheet[] sheets = new SpriteSheet[64];
	private static BlockVAO[] blocks = new BlockVAO[NUM_BLOCK_TYPES];
	
	public static UniTexture guiBackground;
	// Titlescreen-Background
	public static UniTexture tsBackground;
	
	public static void load() {
		Log.out(ResLoader.class, "Using texture rect = " + StateManager.isUsingTexRect());
		UniTextureLoader.flipImages = false;
		//TODO: Blocks: Add to sheets, give sprites.
		sheets[BLOCK_EARTH     ] = new SpriteSheet("res/blocks/blocksEarth.png",      	NUM_BLOCK_TYPES, StateManager.isUsingTexRect());
		sheets[BLOCK_GRASS     ] = new SpriteSheet("res/blocks/blocksGrass.png",      	NUM_BLOCK_TYPES, StateManager.isUsingTexRect());
		sheets[BLOCK_ROCK      ] = new SpriteSheet("res/blocks/blocksRock.png",       	NUM_BLOCK_TYPES, StateManager.isUsingTexRect());
		sheets[BLOCK_LIGHTSTONE] = new SpriteSheet("res/blocks/blocksLightstone.png", 	NUM_BLOCK_TYPES, StateManager.isUsingTexRect());
		sheets[BLOCK_TREEWOOD  ] = new SpriteSheet("res/blocks/blocksTreewood.png", 	NUM_BLOCK_TYPES, StateManager.isUsingTexRect());
		sheets[BLOCK_LEAVES    ] = new SpriteSheet("res/blocks/blocksLeaves.png",       NUM_BLOCK_TYPES, StateManager.isUsingTexRect());
		sheets[BLOCK_WOOD      ] = new SpriteSheet("res/blocks/blocksWood.png", 		NUM_BLOCK_TYPES, StateManager.isUsingTexRect());
		splitUp(sheets[BLOCK_EARTH     ]);
		splitUp(sheets[BLOCK_GRASS     ]);
		splitUp(sheets[BLOCK_ROCK      ]);
		splitUp(sheets[BLOCK_LIGHTSTONE]);
		splitUp(sheets[BLOCK_TREEWOOD  ]);
		splitUp(sheets[BLOCK_LEAVES    ]);
		splitUp(sheets[BLOCK_WOOD      ]);
		
		loadBlockRenderers();
		
		sheets[PLAYER_SHEET] = new SpriteSheet("res/sprites/player/mike_ripped.png", 6, StateManager.isUsingTexRect());
		splitPlayerSprite(sheets[PLAYER_SHEET]);
		
		sheets[GUI_BORDER_BLUE] = new SpriteSheet("res/gui/border_blue.png", 9, StateManager.isUsingTexRect());
		sheets[GUI_BORDER_NORMAL] = new SpriteSheet("res/gui/border_normal.png", 9, StateManager.isUsingTexRect());
		splitUpGUIBorder(sheets[GUI_BORDER_BLUE]);
		splitUpGUIBorder(sheets[GUI_BORDER_NORMAL]);
		
		sheets[GUI_LOADBAR] = new SpriteSheet("res/gui/loadbar.png", 3, StateManager.isUsingTexRect());
		splitUpGUILoadBar(sheets[GUI_LOADBAR]);
		
		sheets[GUI_INV_SLOT] = new SpriteSheet("res/gui/inv_tile.png", 2, StateManager.isUsingTexRect());
		splitUpGUIInvSlots(sheets[GUI_INV_SLOT]);
		
		glEnable(GL_TEXTURE_2D);
		DecodePack pack = UniTextureLoader.loadImageBufferPNG(Thread.currentThread().getContextClassLoader().getResourceAsStream("res/gui/backgroundtile.png"));
		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, pack.width, pack.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pack.data);
		guiBackground = new UniTexture(id, pack.width, pack.height);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public static void loadTitle() {
		tsBackground = loadTex("res/WorldOfCubeTitlescreen.png", false);
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
		sheet.giveSprite(ARM, 			 0,  0,  7, 13);
		sheet.giveSprite(HEAD, 			 7,  0, 26, 29);
		sheet.giveSprite(BODY, 			33,  0, 14, 19);
		sheet.giveSprite(LEG_FRONT, 	47,  0,  9, 13);
		sheet.giveSprite(LEG_BACK, 		47, 13,  9, 13);
		sheet.giveSprite(HEAD_MINI, 	33, 19, 15, 15);
	}
	
	private static void splitUp(SpriteSheet sheet) {
		sheet.giveSprite(ALONE				,  0f,  0f, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(CORNER_TOPLEFT		, 16f,  0f, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(BORDER_TOP			, 32f,  0f, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(CORNER_TOPRIGHT	, 48f,  0f, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(TOP				,  0f, 16f, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(BORDER_LEFT		, 16f, 16f, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(FILLED				, 32f, 16f, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(BORDER_RIGHT		, 48f, 16f, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(BOTTOM				,  0f, 32f, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(CORNER_BOTTOMLEFT	, 16f, 32f, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(BORDER_BOTTOM		, 32f, 32f, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(CORNER_BOTTOMRIGHT	, 48f, 32f, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(LEFT				,  0f, 48f, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(RIGHT				, 16f, 48f, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(VPIPE				, 32f, 48f, BLOCK_SIZE, BLOCK_SIZE);
		sheet.giveSprite(HPIPE				, 48f, 48f, BLOCK_SIZE, BLOCK_SIZE);
	}
	
	private static void loadBlockRenderers() {
		for (int i = 0; i < NUM_BLOCK_TYPES; i++) {
			blocks[i] = new BlockVAO(i);
		}
	}
	
	private static void splitUpGUIBorder(SpriteSheet sheet) {
		sheet.giveSprite(GUI_BORDER_TL,	 0,  0,  GUI_BORDER_S, GUI_BORDER_S);
		sheet.giveSprite(GUI_BORDER_T,	16,  0,  GUI_BORDER_S, GUI_BORDER_S);
		sheet.giveSprite(GUI_BORDER_TR,	32,  0,  GUI_BORDER_S, GUI_BORDER_S);
		sheet.giveSprite(GUI_BORDER_L,	 0,  16, GUI_BORDER_S, GUI_BORDER_S);
		sheet.giveSprite(GUI_MID,		16,  16, GUI_BORDER_S, GUI_BORDER_S);
		sheet.giveSprite(GUI_BORDER_R,	32,  16, GUI_BORDER_S, GUI_BORDER_S);
		sheet.giveSprite(GUI_BORDER_BL,	 0,  32, GUI_BORDER_S, GUI_BORDER_S);
		sheet.giveSprite(GUI_BORDER_B,	16,  32, GUI_BORDER_S, GUI_BORDER_S);
		sheet.giveSprite(GUI_BORDER_BR,	32,  32, GUI_BORDER_S, GUI_BORDER_S);
	}
	
	private static void splitUpGUIInvSlots(SpriteSheet sheet) {
		sheet.giveSprite(GUI_INV_SLOT_UNSEL, 0, 0, GUI_INV_SLOT_SIZE, GUI_INV_SLOT_SIZE);
		sheet.giveSprite(GUI_INV_SLOT_SEL, 0, GUI_INV_SLOT_SIZE, GUI_INV_SLOT_SIZE, GUI_INV_SLOT_SIZE);
	}
	
	private static void splitUpGUILoadBar(SpriteSheet sheet) {
		sheet.giveSprite(GUI_LOADBAR_LEFT, 0, 0, 4, 8);
		sheet.giveSprite(GUI_LOADBAR_MID, 4, 0, 8, 8);
		sheet.giveSprite(GUI_LOADBAR_RIGHT, 12, 0, 4, 8);
	}
	
	public static UniTexture loadTex(String loadpath, boolean texRect) {
		DecodePack pack = UniTextureLoader.loadImageBufferPNG(
				Thread.currentThread().getContextClassLoader().getResourceAsStream(loadpath));
		
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
	
	public static BlockVAO getBlockRenderer(int borderID) {
		return blocks[borderID];
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
