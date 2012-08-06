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
import java.util.ArrayList;
import java.util.List;

import org.universeengine.display.UniDisplay;
import org.worldOfCube.Log;
import org.worldOfCube.client.blocks.Block;
import org.worldOfCube.client.blocks.BlockID;
import org.worldOfCube.client.logic.entity.Entity;
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
	
	private class PlayerInfo {
		PlayerInfo() { inv = new Inventory(); }
		float playerx;
		float playery;
		Inventory inv;
	}
	
	private class WorldLoadPack {
		public int wsize;
		public int csize;
		public List<PlayerInfo> players;
	}
	
	public static final String worldDirStr = "worlds";
	public static final File worldDir = new File(worldDirStr);
	public static final String headerName = "header.wld";
	public static final String dataName = "chunkData.chd";
	public static final String renameSuffix = ".wocsave";
	
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

			// Create both a file with a save-suffix and without:
			File header = new File(worldDir + "/" + headerName + renameSuffix);
			File finalHeader = new File(worldDir + "/" + headerName);
			// Save the stuff to the File with the suffix:
			dosHeader = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(header), ONE_KB));
			saveHeader(world, dosHeader);
			// Rename the file to the File without the suffix:
			header.renameTo(finalHeader);

			// Create both a file with a save-suffix and without:
			File chunkData = new File(worldDir + "/" + dataName + renameSuffix);
			File finalChunkData = new File(worldDir + "/" + dataName);
			// Save the stuff to the File with the suffix:
			dosCData = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(chunkData), ONE_MB));
			saveChunks(world, dosCData);
			// Rename the file to the File without the suffix:
			chunkData.renameTo(finalChunkData);
			
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
		
		for (Entity e : world.getEntitys()) {
			EntityPlayer ep = (EntityPlayer) e;
			
			dos.writeByte(PLAYER_FLAG);
			dos.writeBytes(ep.getName());
			dos.writeFloat((float)ep.getRect().x);
			dos.writeFloat((float)ep.getRect().y);
			
			Inventory inv = ep.getInventory();
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
	
	private SingleWorld loadSingleplayerWorldInst(String name) throws IOException {
		long time = TimeUtil.ms();
		DataInputStream disHeader = null;
		DataInputStream disChunkData = null;
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
			
			String worldDir = worldDirStr + "/" + name;
			
			File header = new File(worldDir + "/" + headerName);
			disHeader = new DataInputStream(new BufferedInputStream(new FileInputStream(header), ONE_KB));
			WorldLoadPack wlp = loadHeader(disHeader);
			if (wlp.players.size() < 1) throw new RuntimeException("The header didn't include any player information.");
			
			File chunkData = new File(worldDir + "/" + dataName);
			toLoad = chunkData.length();
			disChunkData = new DataInputStream(new BufferedInputStream(new FileInputStream(chunkData), ONE_MB));
			ChunkManager cManager = loadChunks(wlp, disChunkData);
			loaded = 1;
			toLoad = 1;
			
			PlayerInfo playerInfo = wlp.players.get(0);
			SingleWorld world = new SingleWorld(new EntityPlayer(playerInfo.playerx, playerInfo.playery, "Player"), cManager, name);
			Log.out(this, "World loaded (" + (TimeUtil.ms()-time) + " ms)");
			return world;
		} catch (IOException e) {
			throw e;
		} finally {
			if (disHeader != null) disHeader.close();
			if (disChunkData != null) disChunkData.close();
		}
	}
	
	private WorldLoadPack loadHeader(DataInputStream dis) throws IOException {
		WorldLoadPack pack = new WorldLoadPack();
		pack.players = new ArrayList<PlayerInfo>();
		PlayerInfo currentPlayer = new PlayerInfo();
		while(dis.available() > 1) {
			byte b = dis.readByte();
			switch (b) {
			case WORLD_SIZE_FLAG:
				pack.wsize = dis.readInt();
				pack.csize = dis.readInt();
				break;
			case PLAYER_FLAG:
				if (currentPlayer != null) pack.players.add(currentPlayer);
				currentPlayer = new PlayerInfo(); // Creates new Inventory instance in currentPlayer.inv
				currentPlayer.playerx = dis.readFloat();
				currentPlayer.playery = dis.readFloat();
				break;
			case INV_SLOT_FlAG:
				if (currentPlayer == null) throw new NullPointerException("\"currentPlayer == null\", the PLAYER_FLAG was missing!");
				InventorySelector is = currentPlayer.inv.getSelector();
				for (int i = 0; i < Inventory.SLOTS; i++) {
					is.setSlot(i, new ItemSlot(loadItem(dis)));
				}
				break;
			case INV_FLAG:
				if (currentPlayer == null) throw new NullPointerException("\"currentPlayer == null\", the PLAYER_FLAG was missing!");
				Storage sto = currentPlayer.inv.getStorage();
				for (int x = 0; x < Inventory.HORIZ_SLOTS; x++) {
					for (int y = 0; y < Inventory.SLOTS; y++) {
						sto.setSlot(x, y, loadItem(dis));
					}
				}
				break;
			}
		}
		if (currentPlayer != null) pack.players.add(currentPlayer);
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
	
	// TODO: MultiWorld-implementation: Implement loading and saving!
	public static World loadSingleplayerWorld(String name) throws IOException {
		return inst().loadSingleplayerWorldInst(name);
	}
	
	public static boolean saveWorldThread(World world) {
		return inst().saveWorldThreadInst(world);
	}
	
	public static float getLoaded() {
		return inst().getLoadedInst();
	}
	
}
