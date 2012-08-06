package org.worldOfCube.client.logic.inventory;

import org.worldOfCube.client.res.ResLoader;

public class ItemSlot {
	
	private ItemStack stack;
	private boolean selected;
	
	public ItemSlot(ItemStack stack) {
		this.stack = stack;
	}
	
	public void setStack(ItemStack stack) {
		this.stack = stack;
	}
	
	public void setSelected(boolean sel) {
		selected = sel;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public int getItemID() {
		return stack != null ? stack.getItemID() : -1;
	}
	
	public void tick() {
	}
	
	public void render(float x, float y) {
		ResLoader.get(ResLoader.GUI_INV_SLOT, selected ? ResLoader.GUI_INV_SLOT_SEL : ResLoader.GUI_INV_SLOT_UNSEL)
			.bindAndRender(x, y, 
					ResLoader.GUI_INV_SLOT_SIZE, 
					ResLoader.GUI_INV_SLOT_SIZE);
		if (stack != null) {
			stack.render(x+ResLoader.GUI_INV_SLOT_SIZE/2, 
					y+ResLoader.GUI_INV_SLOT_SIZE/2);
		}
	}
	
	public ItemStack store(ItemStack s) {
		if (s == null) {
			return null;
		}
		if (stack == null) {
			stack = s;
			return null;
		}
		return stack.store(s);
	}
	
	public boolean take(ItemStack store, int ammount) {
		if (stack == null) {
			return false;
		}
		return stack.take(store, ammount);
	}
	
	public ItemStack getStack() {
		return stack;
	}

}
