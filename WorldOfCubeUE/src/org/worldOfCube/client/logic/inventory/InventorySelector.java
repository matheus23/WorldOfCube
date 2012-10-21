/*
 * Copyright (c) 2012 matheusdev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.worldOfCube.client.logic.inventory;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.worldOfCube.client.input.WrappedMouse;
import org.worldOfCube.client.logic.collision.Rectangle;
import org.worldOfCube.client.res.ResLoader;

public class InventorySelector {

	private int selection;
	private ItemSlot[] slots = new ItemSlot[Inventory.SLOTS];
	private float offsetx;
	private float offsety;
	private Rectangle rect;

	public InventorySelector(float offsetx, float offsety) {
		this.offsetx = offsetx;
		this.offsety = offsety;
		for (int i = 0; i < slots.length; i++) {
			slots[i] = new ItemSlot(null);
		}
		rect = new Rectangle(offsetx, offsety,
				ResLoader.INV_SLOT_SIZE,
				slots.length*ResLoader.INV_SLOT_SIZE);
		setSelection(0);
	}

	public ItemSlot getSelected() {
		return slots[selection];
	}

	public int getSelectionID() {
		return slots[selection].getItemID();
	}

	public void setSelection(int id) {
		id = Math.max(0, Math.min(slots.length-1, id));
		for (int i = 0; i < slots.length; i++) {
			slots[i].setSelected(i == id);
		}
		selection = id;
	}

	public void tick() {
		if (Mouse.isButtonDown(0)) {
			int mx = WrappedMouse.getX();
			int my = WrappedMouse.getY();
			if (rect.contains(mx, my)) {
				setSelection((int)(my-offsety)/ResLoader.INV_SLOT_SIZE);
			}
		}
		int dwheel = Mouse.getDWheel();
		if (dwheel != 0) {
			if (dwheel > 0) {
				setSelection(selection-1);
			} else {
				setSelection(selection+1);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
			setSelection(0);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
			setSelection(1);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
			setSelection(2);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
			setSelection(3);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_5)) {
			setSelection(4);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_6)) {
			setSelection(5);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_7)) {
			setSelection(6);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_8)) {
			setSelection(7);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_9)) {
			setSelection(8);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_0)) {
			setSelection(9);
		}
	}

	public void render() {
		for (int i = 0; i < slots.length; i++) {
			slots[i].render(offsetx, offsety+i*ResLoader.INV_SLOT_SIZE);
		}
	}

	public ItemStack store(ItemStack s) {
		if (s == null) {
			return null;
		}
		for (int i = 0; i < slots.length; i++) {
			s = slots[i].store(s);
		}
		return s;
	}

	public ItemSlot getSlot(int i) {
		return slots[i];
	}

	public void setSlot(int i, ItemSlot is) {
		slots[i] = is;
	}

}
