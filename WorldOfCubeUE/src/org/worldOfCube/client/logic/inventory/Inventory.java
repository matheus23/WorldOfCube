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
		storage = new Storage(offsetx+ResLoader.INV_SLOT_SIZE+INVENTORY_OFFSET, offsety, HORIZ_SLOTS, SLOTS);
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
