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

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import org.worldOfCube.client.res.GLFont;

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
