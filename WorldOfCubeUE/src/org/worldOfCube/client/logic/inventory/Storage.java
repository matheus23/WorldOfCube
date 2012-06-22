package org.worldOfCube.client.logic.inventory;

import org.lwjgl.input.Mouse;
import org.worldOfCube.client.input.WrappedMouse;
import org.worldOfCube.client.logic.collision.Rectangle;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.res.Sprite;

public class Storage {
	
	private Sprite slotSprite = ResLoader.get(ResLoader.GUI_INV_SLOT, ResLoader.GUI_INV_SLOT_UNSEL);
	private Sprite slotSelectedSprite = ResLoader.get(ResLoader.GUI_INV_SLOT, ResLoader.GUI_INV_SLOT_SEL);
	
	private ItemStack[][] slots;
	private float offsetx;
	private float offsety;
	private Rectangle rect;
	private int selx;
	private int sely;
	
	public Storage(float offsetx, float offsety, int width, int height) {
		slots = new ItemStack[width][height];
		this.offsetx = offsetx;
		this.offsety = offsety;
		rect = new Rectangle(offsetx, offsety, width*ResLoader.GUI_INV_SLOT_SIZE, height*ResLoader.GUI_INV_SLOT_SIZE);
	}
	
	public void tick(boolean enfolded) {
		if (enfolded) {
			int mx = Mouse.getX();
			int my = WrappedMouse.getY();
			if (rect.contains(mx, my)) {
				WrappedMouse.eventHappened();
				selectSlot((int)((mx-offsetx)/ResLoader.GUI_INV_SLOT_SIZE),
						(int)((my-offsety)/ResLoader.GUI_INV_SLOT_SIZE));
			}
		}
	}
	
	private void selectSlot(int slotx, int sloty) {
		selx = Math.max(0, Math.min(slots.length, slotx));
		sely = Math.max(0, Math.min(slots[0].length, sloty));
	}
	
	public void render() {
		for (int x = 0; x < slots.length; x++) {
			for (int y = 0; y < slots[x].length; y++) {
				if (x == selx && y == sely) {
					slotSelectedSprite.bindAndRender(
							offsetx+x*ResLoader.GUI_INV_SLOT_SIZE, 
							offsety+y*ResLoader.GUI_INV_SLOT_SIZE, 
							ResLoader.GUI_INV_SLOT_SIZE, 
							ResLoader.GUI_INV_SLOT_SIZE);
				} else {
					slotSprite.bindAndRender(
							offsetx+x*ResLoader.GUI_INV_SLOT_SIZE, 
							offsety+y*ResLoader.GUI_INV_SLOT_SIZE, 
							ResLoader.GUI_INV_SLOT_SIZE, 
							ResLoader.GUI_INV_SLOT_SIZE);
				}
				if (slots[x][y] != null) {
					slots[x][y].render(
						offsetx+x*ResLoader.GUI_INV_SLOT_SIZE + ResLoader.GUI_INV_SLOT_SIZE/2, 
						offsety+y*ResLoader.GUI_INV_SLOT_SIZE + ResLoader.GUI_INV_SLOT_SIZE/2);
				}
			}
		}
	}
	
	public ItemStack store(ItemStack s) {
		for (int x = 0; x < slots.length; x++) {
			for (int y = 0; y < slots[x].length; y++) {
				if (slots[x][y] != null) {
					s = slots[x][y].store(s);
				} else {
					slots[x][y] = s;
					return null;
				}
			}
		}
		return s;
	}
	
	public ItemStack getSlot(int x, int y) {
		return slots[x][y];
	}
	
	public void setSlot(int x, int y, ItemStack is) {
		slots[x][y] = is;
	}

}
