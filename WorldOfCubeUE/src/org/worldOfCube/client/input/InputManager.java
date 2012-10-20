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
package org.worldOfCube.client.input;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;

public class InputManager {

	private static InputManager instance;

	private HashMap<String, int[]> keys = new HashMap<String, int[]>();

	private InputManager() {
		keys.put("up", new int[] { Keyboard.KEY_UP, Keyboard.KEY_W });
		keys.put("down", new int[] { Keyboard.KEY_DOWN, Keyboard.KEY_S });
		keys.put("left", new int[] { Keyboard.KEY_LEFT, Keyboard.KEY_A });
		keys.put("right", new int[] { Keyboard.KEY_RIGHT, Keyboard.KEY_D });
		keys.put("debug", new int[] { Keyboard.KEY_F3 });
		keys.put("screenshot", new int[] { Keyboard.KEY_F2 });
		keys.put("regen", new int[] { Keyboard.KEY_R });
		keys.put("change", new int[] { Keyboard.KEY_F });
		keys.put("godmode", new int[] { Keyboard.KEY_G });
		keys.put("lightput", new int[] { Keyboard.KEY_ADD });
		keys.put("lightsub", new int[] { Keyboard.KEY_MINUS });
		keys.put("lighteq", new int[] { Keyboard.KEY_EQUALS });
	}

	private boolean downInst(String key) {
		int[] possibleKeys = keys.get(key);
		for (int i = 0; i < possibleKeys.length; i++) {
			if (Keyboard.isKeyDown(possibleKeys[i])) {
				return true;
			}
		}
		return false;
	}

	private int[] getKeysInst(String name) {
		return keys.get(name);
	}

	private static InputManager inst() {
		if (instance == null) {
			return instance = new InputManager();
		}
		return instance;
	}

	public static boolean down(String key) {
		return inst().downInst(key);
	}

	public static int[] getKeys(String name) {
		return inst().getKeysInst(name);
	}

	public static boolean isOneOfKeys(String name, int key) {
		int[] arr = inst().getKeysInst(name);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == key) {
				return true;
			}
		}
		return false;
	}

}
