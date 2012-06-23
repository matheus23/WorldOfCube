package org.worldOfCube.client.logic.chunks;

import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.universeengine.display.UniDisplay;
import org.worldOfCube.Log;
import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.blocks.Block;
import org.worldOfCube.client.input.WrappedMouse;
import org.worldOfCube.client.logic.chunks.generation.Generator;
import org.worldOfCube.client.logic.chunks.light.LightUpdater;
import org.worldOfCube.client.logic.collision.Rectangle;
import org.worldOfCube.client.logic.entity.Entity;
import org.worldOfCube.client.logic.entity.EntityPlayer;
import org.worldOfCube.client.logic.inventory.Inventory;
import org.worldOfCube.client.logic.inventory.Item;
import org.worldOfCube.client.logic.inventory.ItemStack;
import org.worldOfCube.client.res.GLFont;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.util.Config;
import org.worldOfCube.client.util.debug.PerfMonitor;

public class World {

	public static final double gravity = 2.0*64.0*9.81;

	public int totalPix;
	public int totalBlocks;

	protected ChunkManager cManager;
	protected float worldx = 0;
	protected float worldy = 0;
	protected UniDisplay display;
	protected EntityPlayer localPlayer;
	protected List<Entity> entitys;
	public Rectangle bounds = new Rectangle(0, 0, totalPix, totalPix);
	public Rectangle screen = new Rectangle(0, 0, 800, 600);
	private LightUpdater light;
	private Inventory inv;
	private boolean keyboardLock = false;
	public Random rand;
	private String name;
	public int loadType = 0;
	public int treesLoaded;
	public int treesToLoad = 1;
	private MouseCursor mc;
	
	public World(UniDisplay display, ChunkManager cm, String name) {
		this.cManager = cm;
		this.name = name;
		this.display = display;
		rand = new Random();
		inv = new Inventory();
		totalPix = cManager.size * cManager.csize * ResLoader.BLOCK_SIZE;
		totalBlocks = cManager.size * cManager.csize;
		postGen(null);
	}

	public World(UniDisplay display, int size, int csize, String name) {
		this(display, System.currentTimeMillis(), size, csize, name, true);
	}
	
	public World(UniDisplay display, long seed, int size, int csize, String name, boolean gen) {
		this.display = display;
		this.name = name;
		rand = new Random(seed);
		inv = new Inventory();
		totalPix = size * csize * ResLoader.BLOCK_SIZE;
		totalBlocks = size * csize;
		if (gen) {
			generate(size, csize);
		} else {
			postGen(null);
		}
	}
	
	public void generate(int size, int csize) {
		long time = System.currentTimeMillis();
		Log.out(this, "Starting World Generation.");
		Generator g;
		cManager = new ChunkManager(size, csize);
		cManager.create(g = new Generator(0.0f, 4f, rand, this), this);
		loadType = 3;
		g.generateTrees();
		postGen(new EntityPlayer(totalPix/2, 48f, this, true));
		inv.store(new ItemStack(new Item(Item.LIGHTSTONE), 500));
		Log.out(this, "World Generation finished (" + (System.currentTimeMillis()-time) + " ms).");
	}
	
	protected void postGen(EntityPlayer ep) {
		cManager.updateAll();
		entitys = new ArrayList<Entity>();
		if (ep != null) {
			entitys.add(localPlayer = ep);
		}
		light = new LightUpdater(cManager);
		light.recalcLight();
		bounds = new Rectangle(0, 0, totalPix, totalPix);
		mc = new MouseCursor(this, light);
	}

	public void tick(double d) {
		PerfMonitor.startProfile("ENTITY TICK");
		for (int i = 0; i < entitys.size(); i++) {
			entitys.get(i).tick(d);
		}
		inv.tick();
		PerfMonitor.stopProfile("ENTITY TICK");
		
		worldx = (float) (localPlayer.getRect().x - display.getWidth() / 2);
		worldy = (float) (localPlayer.getRect().y - display.getHeight() / 2);
		worldx = Math.max(0, Math.min(totalPix - display.getWidth(), worldx));
		worldy = Math.max(0, Math.min(totalPix - display.getHeight(), worldy));
		screen.set(worldx, worldy, display.getWidth(), display.getHeight());
		
		mc.tick(worldx, worldy, display.getWidth(), display.getHeight());
		
		light.tick((int)worldx, (int)worldy, display.getWidth(), display.getHeight());
		light.update((int)worldx, (int)worldy, display.getWidth(), display.getHeight(), false);
		
		WrappedMouse.update();
	}
	
	public void addEntity(Entity e) {
		entitys.add(e);
	}
	
	public void removeEntity(Entity e) {
		entitys.remove(e);
	}
	
	public float getClearRed() {
		return ClientMain.BG_R*LightUpdater.sunlight.getStrength();
	}
	
	public float getClearGreen() {
		return ClientMain.BG_G*LightUpdater.sunlight.getStrength();
	}
	
	public float getClearBlue() {
		return ClientMain.BG_B*LightUpdater.sunlight.getStrength();
	}

	public float getGameMouseX() {
		return Mouse.getX() + worldx;
	}

	public float getGameMouseY() {
		return (display.getHeight() - Mouse.getY()) + worldy;
	}
	
	public void render() {
		glPushMatrix();
		{
			glTranslatef(-worldx, -worldy, 0f);
			glColor3f(1f, 1f, 1f);
			
			boolean vaorend = Config.get("block_rendering").equals("vao");
			if (vaorend) {
				glEnableClientState(GL_VERTEX_ARRAY);
				glEnableClientState(GL_TEXTURE_COORD_ARRAY);
				glEnableClientState(GL_COLOR_ARRAY);
			}
			
			cManager.renderChunks(worldx, worldy, display.getWidth(), display.getHeight());

			glDisableClientState(GL_VERTEX_ARRAY);
			glDisableClientState(GL_TEXTURE_COORD_ARRAY);
			glDisableClientState(GL_COLOR_ARRAY);
			
			for (int i = 0; i < entitys.size(); i++) {
				entitys.get(i).render();
			}
		}
		glPopMatrix();
		glColor3f(1f, 1f, 1f);
		if (Config.get("debug").equals("on")) {
			GLFont.render(10f, 30f, GLFont.ALIGN_LEFT, entitys.size() + " entitys.", 10);
		}
		inv.render();
	}
	
	public boolean isFieldFree(Rectangle rect) {
		int beginx = (int) (rect.x / ResLoader.BLOCK_SIZE);
		int beginy = (int) (rect.y / ResLoader.BLOCK_SIZE);
		int endx = (int) ((rect.x + rect.w) / ResLoader.BLOCK_SIZE) + 1;
		int endy = (int) ((rect.y + rect.h) / ResLoader.BLOCK_SIZE) + 1;
		Block b;
		for (int x = beginx; x < endx; x++) {
			for (int y = beginy; y < endy; y++) {
				b = cManager.getBlock(x, y, true);
				if (b != null) {
					Rectangle[] rects = b.getCollisionRects();
					for (int i = 0; i < rects.length; i++) {
						if (rect.intersects(rects[i])) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	public void saveWorldImg() {
		new WorldMapGenerator(this);
	}
	
	public ChunkManager getChunkManager() {
		return cManager;
	}
	
	public void setKeyboardLock(boolean lock) {
		keyboardLock = lock;
	}
	
	public boolean hasKeyboardLock() {
		return keyboardLock;
	}
	
	public String getName() {
		return name;
	}
	
	public EntityPlayer getLocalPlayer() {
		return localPlayer;
	}
	
	public Inventory getInventory() {
		return inv;
	}
	
	public void setLocalPlayer(EntityPlayer lp) {
		localPlayer = lp;
		if (!entitys.contains(lp)) {
			entitys.add(lp);
		}
	}
	
	public void setInventory(Inventory inv) {
		this.inv = inv;
	}
	
	public String getLoadingTitle() {
		switch(loadType) {
		case 1: return "Creating chunks.";
		case 2: return "Creating blocks on chunks.";
		case 3: return "Creating trees.";
		default: return "Creating world.";
		}
	}
	
	public float getLoaded() {
		switch (loadType) {
		case 1: case 2: return cManager == null ? 0f : cManager.getLoadProgress();
		case 3: return treesLoaded/treesToLoad;
		default: return 0f;
		}
	}
	
	public void destroy() {
		light.destroy();
	}
	
}
