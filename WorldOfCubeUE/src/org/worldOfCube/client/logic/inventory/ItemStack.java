package org.worldOfCube.client.logic.inventory;

import org.worldOfCube.client.res.GLFont;

import static org.lwjgl.opengl.GL11.*;

public class ItemStack {
	
	private Item item;
	private int num;
	
	public ItemStack(Item item, int number) {
		this.item = item;
		this.num = number;
	}
	
	public int getItemID() {
		return item != null ? item.getID() : -1;
	}
	
	public void tick() {
	}
	
	public void render(float x, float y) {
		glPushMatrix();
		{
			item.render(x, y);
			GLFont.render(-12f, 2f, GLFont.ALIGN_LEFT, Integer.toString(num), 10);
		}
		glPopMatrix();
	}
	
	public ItemStack store(ItemStack s) {
		if (s == null) {
			return null;
		}
		if (s.getItemID() == item.getID()) {
			int stackable = Item.getStackable(item.getID());
			while (num < stackable) {
				num++;
				s.num--;
				if (s.num == 0) {
					return null;
				}
			}
			return s;
		} else {
			return s;
		}
	}

	
	public boolean take(ItemStack store, int ammount) {
		if (ammount <= 0) {
			return false;
		}
		int storemax = Item.getStackable(store.getItemID());
		int storecur = store.getNumber();
		
		if (storecur+ammount > storemax) {
			return take(store, ammount-1);
		}
		if (num < ammount) {
			return take(store, ammount-1);
		}
		num -= ammount;
		store.num += ammount;
		return true;
	}
	
	public Item getItem() {
		return item;
	}
	
	public int getNumber() {
		return num;
	}
	
	public void setNumber(int num) {
		this.num = num;
	}

}
