package org.worldOfCube.client.logic.inventory;

import org.lwjgl.input.Keyboard;
import org.worldOfCube.client.res.ResLoader;

public class Inventory {
	
	public static final int INVENTORY_OFFSET = 6;
	public static final int HORIZ_SLOTS = 5;
	public static final int SLOTS = 10;
	
	private InventorySelector is;
	private Storage storage;
	private boolean enfolded = false;
	private boolean pressedE = false;
	
	public Inventory() {
		float offsetx = 10;
		float offsety = 50;
		is = new InventorySelector(offsetx, offsety);
		storage = new Storage(offsetx+ResLoader.GUI_INV_SLOT_SIZE+INVENTORY_OFFSET, offsety, HORIZ_SLOTS, SLOTS);
	}
	
	public int getSelectionID() {
		return is.getSelectionID();
	}
	
	public void tick() {
		is.tick();
		if (Keyboard.isKeyDown(Keyboard.KEY_E) && !pressedE) {
			pressedE = true;
			enfolded = !enfolded;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_E) && pressedE) {
			pressedE = false;
		}
		storage.tick(enfolded);
	}
	
	public void render() {
		is.render();
		if (enfolded) {
			storage.render();
		}
	}
	
	public ItemStack store(ItemStack s) {
		s = is.store(s);
		s = storage.store(s);
		return s;
	}
	
	public Storage getStorage() {
		return storage;
	}
	
	public InventorySelector getSelector() {
		return is;
	}

}
