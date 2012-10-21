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

import org.worldOfCube.client.input.WrappedMouse;
import org.worldOfCube.client.logic.collision.Rectangle;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.res.Sprite;

public class Storage {

	private Sprite slotSprite = ResLoader.get(ResLoader.Sheets.GUI_INV_SLOT, ResLoader.Slots.UNSELECTED.ordinal());
	private Sprite slotSelectedSprite = ResLoader.get(ResLoader.Sheets.GUI_INV_SLOT, ResLoader.Slots.SELECTED.ordinal());

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
		rect = new Rectangle(offsetx, offsety, width*ResLoader.INV_SLOT_SIZE, height*ResLoader.INV_SLOT_SIZE);
	}

	public void tick(boolean enfolded) {
		if (enfolded) {
			int mx = WrappedMouse.getX();
			int my = WrappedMouse.getY();
			if (rect.contains(mx, my)) {
				selectSlot((int)((mx-offsetx)/ResLoader.INV_SLOT_SIZE),
						(int)((my-offsety)/ResLoader.INV_SLOT_SIZE));
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
							offsetx+x*ResLoader.INV_SLOT_SIZE,
							offsety+y*ResLoader.INV_SLOT_SIZE,
							ResLoader.INV_SLOT_SIZE,
							ResLoader.INV_SLOT_SIZE);
				} else {
					slotSprite.bindAndRender(
							offsetx+x*ResLoader.INV_SLOT_SIZE,
							offsety+y*ResLoader.INV_SLOT_SIZE,
							ResLoader.INV_SLOT_SIZE,
							ResLoader.INV_SLOT_SIZE);
				}
				if (slots[x][y] != null) {
					slots[x][y].render(
						offsetx+x*ResLoader.INV_SLOT_SIZE + ResLoader.INV_SLOT_SIZE/2,
						offsety+y*ResLoader.INV_SLOT_SIZE + ResLoader.INV_SLOT_SIZE/2);
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
