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
		ResLoader.get(ResLoader.Sheets.GUI_INV_SLOT, selected ?
				ResLoader.Slots.SELECTED.ordinal() :
				ResLoader.Slots.UNSELECTED.ordinal())
			.bindAndRender(x, y,
					ResLoader.INV_SLOT_SIZE,
					ResLoader.INV_SLOT_SIZE);
		if (stack != null) {
			stack.render(x+ResLoader.INV_SLOT_SIZE/2,
					y+ResLoader.INV_SLOT_SIZE/2);
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
