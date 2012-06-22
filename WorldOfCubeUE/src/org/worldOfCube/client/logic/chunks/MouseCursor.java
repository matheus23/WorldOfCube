package org.worldOfCube.client.logic.chunks;

import org.lwjgl.input.Mouse;
import org.worldOfCube.Log;
import org.worldOfCube.client.blocks.Block;
import org.worldOfCube.client.blocks.BlockID;
import org.worldOfCube.client.input.InputManager;
import org.worldOfCube.client.input.WrappedMouse;
import org.worldOfCube.client.logic.chunks.light.LightUpdater;
import org.worldOfCube.client.logic.entity.EntityDrop;
import org.worldOfCube.client.logic.entity.EntityPlayer;
import org.worldOfCube.client.logic.inventory.Item;
import org.worldOfCube.client.logic.inventory.ItemStack;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.util.Distance;

public class MouseCursor {
	
	public static final int DELAY = 2;
	public static final int BLOCK_PLACE_DIST = 5;
	public static final int BLOCK_REMOVE = 1;
	public static final int BLOCK_ADD = 2;

	private boolean inforeground = true;
	private boolean pressedF;
	private World world;
	private LightUpdater light;
	private int count = 0;
	
	public MouseCursor(World world, LightUpdater light) {
		this.world = world;
		this.light = light;
	}
	
	public void tick(float wx, float wy, float ww, float wh) {
		if (InputManager.down("change") && !pressedF) {
			pressedF = true;
			inforeground = !inforeground;
			Log.out(this, "Editing in " + (inforeground ? "foreground" : "background"));
		}
		if (!InputManager.down("change") && pressedF) {
			pressedF = false;
		}
		if (count == 0) {
			int x = ((int) world.getGameMouseX()) / ResLoader.BLOCK_SIZE;
			int y = ((int) world.getGameMouseY()) / ResLoader.BLOCK_SIZE;
			EntityPlayer ep = world.getLocalPlayer();
			int bx = (int) (ep.midx()/ResLoader.BLOCK_SIZE);
			int by = (int) (ep.midy()/ResLoader.BLOCK_SIZE);
			int dist = Distance.get(x, y, bx, by);
			
			if (dist < BLOCK_PLACE_DIST) {
				if (Mouse.isButtonDown(0) && !WrappedMouse.isEventHappened()) {
					blockAction(BLOCK_REMOVE, x, y, ep);
					count = DELAY;
				}
				if (Mouse.isButtonDown(1) && !WrappedMouse.isEventHappened()) {
					blockAction(BLOCK_ADD, x, y, ep);
					count = DELAY;
				}
				if (count > 0) {
					light.update((int)wx, (int)wy, (int)ww, (int)wh, true);
				}
			}
		}
		if (count > 0) {
			count--;
		}
	}
	
	public void blockAction(int action, int x, int y, EntityPlayer ep) {
		Chunk c = world.getChunkManager().getChunkFromBlockCoords(x, y);
		byte bx = (byte)(x % world.getChunkManager().csize);
		byte by = (byte)(y % world.getChunkManager().csize);
		switch (action) {
		case BLOCK_ADD:
			ItemStack s = world.getInventory().getSelector().getSelected().getStack();
			if (s != null && s.getNumber() > 0) {
				if (c.getLocalBlock(bx, by, inforeground) == null
						&& (hasSurrounding(c, bx, by, inforeground)
								|| c.getLocalBlock(bx, by, false) != null)) {
					c.setLocalBlock(bx, by, BlockID.itemToBlock(world.getInventory().getSelectionID(), inforeground), inforeground);
					c.updateDiamond(bx, by);
					if (ep.colliding()) {
						c.setLocalBlock(bx, by, null, inforeground);
					}
					s.setNumber(s.getNumber()-1);
				}
			}
			break;
		case BLOCK_REMOVE:
			Block b = c.getLocalBlock(bx, by, inforeground); 
			if (b != null) {
				c.setLocalBlock(bx, by, null, inforeground);
				world.addEntity(new EntityDrop(new Item(BlockID.blockToItem(b)),
						x*ResLoader.BLOCK_SIZE, 
						y*ResLoader.BLOCK_SIZE, world, world.getLocalPlayer()));
				c.updateDiamond(bx, by);
			}
			break;
		default: throw new IllegalArgumentException("Wrong argument \"action\": " + action);
		}
	}
	
	public boolean hasSurrounding(Chunk c, byte bx, byte by, boolean fg) {
		return c.getBlock(bx-1, by, fg) != null ||
				c.getBlock(bx+1, by, fg) != null ||
				c.getBlock(bx, by-1, fg) != null ||
				c.getBlock(bx, by+1, fg) != null;
	}

}
