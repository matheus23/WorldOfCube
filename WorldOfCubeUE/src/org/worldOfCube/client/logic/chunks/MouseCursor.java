package org.worldOfCube.client.logic.chunks;

import org.worldOfCube.Log;
import org.worldOfCube.client.blocks.Block;
import org.worldOfCube.client.blocks.BlockID;
import org.worldOfCube.client.input.InputManager;
import org.worldOfCube.client.input.WorldInputListener;
import org.worldOfCube.client.logic.collision.Rectangle;
import org.worldOfCube.client.logic.entity.EntityDrop;
import org.worldOfCube.client.logic.entity.EntityPlayer;
import org.worldOfCube.client.logic.inventory.Item;
import org.worldOfCube.client.logic.inventory.ItemStack;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.util.Distance;

public class MouseCursor implements WorldInputListener {

	public static enum State {
		NONE,
		DESTROYING,
		BUILDING
	}

	/** Only calculate mouse actions every second frame */
	public static final int DELAY = 2;
	/** The distance to be able to place blocks */
	public static final int BLOCK_PLACE_DIST = 5;

	public static final int BLOCK_REMOVE = 1;
	public static final int BLOCK_ADD = 2;

	protected final String playerName;

	protected boolean inforeground = true;
	protected boolean pressedF;
	protected ItemStack stack; // TODO: Implement this "stack"-feature.
	protected int count = 0;
	protected State state;

	public MouseCursor(String playerName) {
		this.playerName = playerName;
		this.state = State.NONE;
	}

	/**
	 * Updates the MouseCursor, calculating whether to place
	 * blocks or not. If true, it calls {@link #blockAction(int, int, int, EntityPlayer)},
	 * using {@link #BLOCK_ADD}, if the user right-clicked and
	 * {@link #BLOCK_REMOVE}, if the user left-clicked.
	 * @param viewport the viewport from the world.
	 * @param world the world instance to operate on.
	 */
	public void tick(Rectangle viewport, SingleWorld world) {
		if (count > 0) {
			count--;
		}
	}

	public void render() {
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
	public void blockAction(int action, int x, int y, EntityPlayer ep, World world) {
		Chunk c = world.getChunkManager().getChunkFromBlockCoords(x, y);
		byte bx = (byte)(x % world.getChunkManager().getChunkSize());
		byte by = (byte)(y % world.getChunkManager().getChunkSize());
		switch (action) {
		case BLOCK_ADD:
			ItemStack s = ep.getInventory().getSelector().getSelected().getStack();
			if (s != null && s.getNumber() > 0) {
				if (c.getLocalBlock(bx, by, inforeground) == null
						&& (c.hasSurrounding(bx, by, true)
								|| c.hasSurrounding(bx, by, false))) {
					c.setLocalBlock(bx, by, BlockID.itemToBlock(ep.getInventory().getSelectionID(), inforeground), inforeground);
					c.updateDiamond(bx, by);
					if (ep.colliding(world)) {
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
				world.addEntity(new EntityDrop(
						new Item(BlockID.blockToItem(b)),
						x*ResLoader.BLOCK_SIZE,
						y*ResLoader.BLOCK_SIZE));
				c.updateDiamond(bx, by);
			}
			break;
		default: throw new IllegalArgumentException("Wrong argument \"action\": " + action);
		}
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.input.WorldInputListener#handleKeyEvent(int, char, boolean, org.worldOfCube.client.logic.chunks.World)
	 */
	@Override
	public void handleKeyEvent(int keyCode, char keyChar, boolean down, World world) {
		if (down && InputManager.isOneOfKeys("change", keyCode)) {
			inforeground = !inforeground;
			Log.out("Editing in " + (inforeground ? "foreground" : "background"));
		}
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.input.WorldInputListener#handleMouseEvent(int, int, int, boolean, org.worldOfCube.client.logic.chunks.World)
	 */
	@Override
	public void handleMouseEvent(int mousex, int mousey, int button, boolean down, World world) {
		if (down) {
			switch (button) {
			case 0:
				state = State.DESTROYING;
				break;
			case 1:
				state = State.BUILDING;
				break;
			}
		} else {
			if (button == 0 || button == 1) {
				state = State.NONE;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.input.WorldInputListener#handleMousePosition(int, int, org.worldOfCube.client.logic.chunks.World)
	 */
	@Override
	public void handleMousePosition(int mousex, int mousey, World world) {
		if (state != State.NONE) {
			int x = ((int) world.convertXToWorld(mousex)) / ResLoader.BLOCK_SIZE;
			int y = ((int) world.convertYToWorld(mousey)) / ResLoader.BLOCK_SIZE;
			EntityPlayer ep = world.getPlayer(playerName);
			int bx = (int) (ep.midx()/ResLoader.BLOCK_SIZE);
			int by = (int) (ep.midy()/ResLoader.BLOCK_SIZE);
			int dist = Distance.get(x, y, bx, by);

			if (dist < BLOCK_PLACE_DIST) {
				switch (state) {
				case DESTROYING:
					blockAction(BLOCK_REMOVE, x, y, ep, world);
					count = DELAY;
					break;
				case BUILDING:
					blockAction(BLOCK_ADD, x, y, ep, world);
					count = DELAY;
					break;
				case NONE:
					break;
				}
			}
		}
	}

}
