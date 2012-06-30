package org.worldOfCube.client.logic.chunks;

import org.lwjgl.input.Mouse;
import org.worldOfCube.Log;
import org.worldOfCube.client.blocks.Block;
import org.worldOfCube.client.blocks.BlockID;
import org.worldOfCube.client.input.InputManager;
import org.worldOfCube.client.input.WrappedMouse;
import org.worldOfCube.client.logic.entity.EntityDrop;
import org.worldOfCube.client.logic.entity.EntityPlayer;
import org.worldOfCube.client.logic.inventory.Item;
import org.worldOfCube.client.logic.inventory.ItemStack;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.util.Distance;

public class MouseCursor {
	
	/** Only calculate mouse actions every second frame */
	public static final int DELAY = 2;
	/** The distance to be able to place blocks */
	public static final int BLOCK_PLACE_DIST = 5;
	
	public static final int BLOCK_REMOVE = 1;
	public static final int BLOCK_ADD = 2;

	private boolean inforeground = true;
	private boolean pressedF;
	private World world;
	private int count = 0;
	
	/**
	 * Constructor.
	 * Creates a new Cursor, being able to delete blocks and add
	 * blocks at the mouse position under various conditions.
	 * If "world" does not have a "localPlayer", then this class
	 * is useless, and could throw a NullPointerException at any
	 * time.
	 * @param world used for placing, removing blocks and for getting the local Player Entity.
	 */
	public MouseCursor(World world) {
		this.world = world;
	}
	
	/**
	 * Updates the MouseCursor, calculating whether to place
	 * blocks or not. If true, it calls {@link #blockAction(int, int, int, EntityPlayer)},
	 * using {@link #BLOCK_ADD}, if the user right-clicked and
	 * {@link #BLOCK_REMOVE}, if the user left-clicked.
	 * @param wx pixel-space viewport x position
	 * @param wy pixel-space viewport y position
	 * @param ww viewport width
	 * @param wh viewport height
	 */
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
			}
		}
		if (count > 0) {
			count--;
		}
	}
	
	/**
	 * Cause an action on a world-space block x and y position.
	 * "action" has to be either {@link #BLOCK_ADD} or {@link #BLOCK_REMOVE}.
	 * If its none of them, it throws an IllegalArgumentException.
	 * @param action the action to perform on (x, y).
	 * @param x the world-space block x position.
	 * @param y the world-space block y position.
	 * @param ep the player to get the item from, etc.
	 */
	public void blockAction(int action, int x, int y, EntityPlayer ep) {
		Chunk c = world.getChunkManager().getChunkFromBlockCoords(x, y);
		byte bx = (byte)(x % world.getChunkManager().csize);
		byte by = (byte)(y % world.getChunkManager().csize);
		switch (action) {
		case BLOCK_ADD:
			ItemStack s = world.getInventory().getSelector().getSelected().getStack();
			if (s != null && s.getNumber() > 0) {
				if (c.getLocalBlock(bx, by, inforeground) == null
						&& (c.hasSurrounding(bx, by, true)
								|| c.hasSurrounding(bx, by, false))) {
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
	
}
