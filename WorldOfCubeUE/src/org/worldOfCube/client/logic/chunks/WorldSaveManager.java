package org.worldOfCube.client.logic.chunks;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.universeengine.display.UniDisplay;
import org.worldOfCube.Log;
import org.worldOfCube.client.blocks.Block;
import org.worldOfCube.client.blocks.BlockID;
import org.worldOfCube.client.logic.entity.EntityPlayer;
import org.worldOfCube.client.logic.inventory.Inventory;
import org.worldOfCube.client.logic.inventory.InventorySelector;
import org.worldOfCube.client.logic.inventory.Item;
import org.worldOfCube.client.logic.inventory.ItemSlot;
import org.worldOfCube.client.logic.inventory.ItemStack;
import org.worldOfCube.client.logic.inventory.Storage;
import org.worldOfCube.client.util.TimeUtil;

public class WorldSaveManager {
	
	private class Saver implements Runnable {
		
		private World world;
		
		public Saver(World world) {
			this.world = world;
		}
		
		public void run() {
			runningSaver = this;
			try {
				saveWorld(world);
			} catch (IOException e) {
				e.printStackTrace();
			}
			runningSaver = null;
		}
	}
	
	private class WorldLoadPack {
		public int wsize;
		public int csize;
		public float playerx;
		public float playery;
		public Inventory inv;
	}
	
	public static final String worldDirStr = "worlds";
	public static final File worldDir = new File(worldDirStr);
	public static final String headerName = "header.wld";
	public static final String dataName = "chunkData.chd";
	
	public static final int ONE_KB = 1024;
	public static final int ONE_MB = ONE_KB*1024;
	
	public static final byte WORLD_SIZE_FLAG = (byte)0xAA;
	public static final byte PLAYER_FLAG = (byte)0xBB;
	public static final byte INV_SLOT_FlAG = (byte)0xCC;
	public static final byte INV_FLAG = (byte)0xDD;
	
	private static WorldSaveManager instance;
	
	private Saver runningSaver;
	
	private long loaded = 1;
	private long toLoad = 1;
	
	private WorldSaveManager() {
		if (!worldDir.exists()) {
			worldDir.mkdir();
		}
	}
	
	private File[] listWorldsInst() {
		return worldDir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});
	}
	
	private boolean saveWorldThreadInst(World world) {
		if (runningSaver == null) {
			new Thread(new Saver(world)).start();
			return true;
		}
		return false;
	}
	
	private void saveWorldInst(World world) throws IOException {
		long time = TimeUtil.ms();
		Log.out(this, "Starting World save...");
		DataOutputStream dosHeader = null;
		DataOutputStream dosCData = null;
		try {
			String worldDir = worldDirStr + "/" + world.getName();
			File dir = new File(worldDir);
			dir.mkdir();
			
			File header = new File(worldDir + "/" + headerName);
			dosHeader = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(header), ONE_KB));
			saveHeader(world, dosHeader);
			
			File chunkData = new File(worldDir + "/" + dataName);
			dosCData = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(chunkData), ONE_MB));
			saveChunks(world, dosCData);
			
			Log.out(this, "World saving finished (" + (TimeUtil.ms()-time) + " ms)");
		} catch (IOException e) {
			throw e;
		} finally {
			if (dosHeader != null) dosHeader.close();
			if (dosCData != null) dosCData.close();
		}
	}
	
	private void saveHeader(World world, DataOutputStream dos) throws IOException {
		dos.writeByte(WORLD_SIZE_FLAG);
		dos.writeInt(world.getChunkManager().size);
		dos.writeInt(world.getChunkManager().csize);
		
		EntityPlayer ep = world.getLocalPlayer();
		if (ep != null) {
			dos.writeByte(PLAYER_FLAG);
			dos.writeFloat((float)ep.getRect().x);
			dos.writeFloat((float)ep.getRect().y);
		}
		
		Inventory inv = world.getInventory();
		if (inv != null) {
			dos.writeByte(INV_SLOT_FlAG);
			InventorySelector is = inv.getSelector();
			for (int i = 0; i < Inventory.SLOTS; i++) {
				saveItem(is.getSlot(i).getStack(), dos);
			}
			
			dos.writeByte(INV_FLAG);
			Storage sto = inv.getStorage();
			for (int x = 0; x < Inventory.HORIZ_SLOTS; x++) {
				for (int y = 0; y < Inventory.SLOTS; y++) {
					saveItem(sto.getSlot(x, y), dos);
				}
			}
		}
	}
	
	private void saveItem(ItemStack is, DataOutputStream dos) throws IOException {
		if (is != null) {
			dos.writeInt(is.getItemID());
			dos.writeInt(is.getNumber());
		} else {
			dos.writeInt(-1);
		}
	}
	
	private void saveChunks(World world, DataOutputStream dos) throws IOException {
		for (int x = 0; x < world.getChunkManager().size; x++) {
			for (int y = 0; y < world.getChunkManager().size; y++) {
				saveChunk(world.getChunkManager().getChunk(x, y), dos);
			}
		}
	}
	
	private void saveChunk(Chunk c, DataOutputStream dos) throws IOException {
		dos.writeInt(c.getX());
		dos.writeInt(c.getY());
		Block b;
		for (int x = 0; x < c.getSize(); x++) {
			for (int y = 0; y < c.getSize(); y++) {
				b = c.getLocalBlock(x, y, true);
				dos.writeChar(b == null ? 0 : BlockID.blockToId(b));
				b = c.getLocalBlock(x, y, false);
				dos.writeChar(b == null ? 0 : BlockID.blockToId(b));
			}
		}
	}
	
	private World loadWorldInst(String name, UniDisplay display) throws IOException {
		long time = TimeUtil.ms();
		DataInputStream disHeader = null;
		DataInputStream disCData = null;
		try {
			Log.out(this, "Starting world loading...");
			if (runningSaver != null) {
				Log.out(this, "Another saving Thread is currently running... Waiting for him.");
				while (runningSaver != null) {
					try {
						Thread.sleep(20);
					} catch (InterruptedException ie) {}
				}
				Log.out(this, "The other saving Thread is finished.");
			}
			
			File header = new File(worldDirStr + "/" + name + "/" + headerName);
			disHeader = new DataInputStream(new BufferedInputStream(new FileInputStream(header), ONE_KB));
			WorldLoadPack wlp = loadHeader(disHeader);
			
			File chunkData = new File(worldDir + "/" + name + "/" + dataName);
			toLoad = chunkData.length();
			disCData = new DataInputStream(new BufferedInputStream(new FileInputStream(chunkData), ONE_MB));
			ChunkManager cManager = loadChunks(wlp, disCData);
			loaded = 1;
			toLoad = 1;
			
			World world = new World(display, cManager, name);
			world.setLocalPlayer(new EntityPlayer(wlp.playerx, wlp.playery, world, false));
			world.setInventory(wlp.inv);
			Log.out(this, "World loaded (" + (TimeUtil.ms()-time) + " ms)");
			return world;
		} catch (IOException e) {
			throw e;
		} finally {
			if (disHeader != null) disHeader.close();
			if (disCData != null) disCData.close();
		}
	}
	
	private WorldLoadPack loadHeader(DataInputStream dis) throws IOException {
		WorldLoadPack pack = new WorldLoadPack();
		Inventory inv = new Inventory();
		while(dis.available() > 1) {
			byte b = dis.readByte();
			switch (b) {
			case WORLD_SIZE_FLAG:
				pack.wsize = dis.readInt();
				pack.csize = dis.readInt();
				break;
			case PLAYER_FLAG:
				pack.playerx = dis.readFloat();
				pack.playery = dis.readFloat();
				break;
			case INV_SLOT_FlAG:
				InventorySelector is = inv.getSelector();
				for (int i = 0; i < Inventory.SLOTS; i++) {
					is.setSlot(i, new ItemSlot(loadItem(dis)));
				}
				break;
			case INV_FLAG:
				Storage sto = inv.getStorage();
				for (int x = 0; x < Inventory.HORIZ_SLOTS; x++) {
					for (int y = 0; y < Inventory.SLOTS; y++) {
						sto.setSlot(x, y, loadItem(dis));
					}
				}
				break;
			}
		}
		pack.inv = inv;
		return pack;
	}
	
	private ItemStack loadItem(DataInputStream dis) throws IOException {
		int id = dis.readInt();
		if (id == -1) {
			return null;
		}
		int num = dis.readInt();
		return new ItemStack(new Item(id), num);
	}
	
	private ChunkManager loadChunks(WorldLoadPack wlp, DataInputStream dis) throws IOException {
		ChunkManager cm = new ChunkManager(wlp.wsize, wlp.csize);
		cm.initChunks();
		while (dis.available() > 1) {
			loadChunk(cm, dis);
		}
		return cm;
	}
	
	private void loadChunk(ChunkManager cm, DataInputStream dis) throws IOException {
		int cx = dis.readInt();
		int cy = dis.readInt();
		loaded += 8;
		Chunk c = cm.getChunk(cx, cy);
		Block b;
		char ch;
		for (byte x = 0; x < cm.csize; x++) {
			for (byte y = 0; y < cm.csize; y++) {
				// Block in foreground:
				ch = dis.readChar();
				b = BlockID.idToBlock(ch, true);
				c.setLocalBlock(x, y, b, true);
				// Block in background:
				ch = dis.readChar();
				b = BlockID.idToBlock(ch, false);
				c.setLocalBlock(x, y, b, false);
				loaded += 4;
			}
		}
	}
	
	private float getLoadedInst() {
		return ((float)loaded/(float)toLoad)*100f;
	}
	
	private static WorldSaveManager inst() {
		if (instance == null) {
			return instance = new WorldSaveManager();
		}
		return instance;
	}
	
	public static File[] listWorlds() {
		return inst().listWorldsInst();
	}
	
	public static void saveWorld(World world) throws IOException {
		inst().saveWorldInst(world);
	}
	
	public static World loadWorld(String name, UniDisplay display) throws IOException {
		return inst().loadWorldInst(name, display);
	}
	
	public static boolean saveWorldThread(World world) {
		return inst().saveWorldThreadInst(world);
	}
	
	public static float getLoaded() {
		return inst().getLoadedInst();
	}
	
}
