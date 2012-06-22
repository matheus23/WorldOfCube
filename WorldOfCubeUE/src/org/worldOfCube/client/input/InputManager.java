package org.worldOfCube.client.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.input.Keyboard;

public class InputManager {
	
	private class Key {
		private final String name;
		private final int[] keys;
		public Key(String name, int... keys) {
			this.name = name;
			this.keys = keys;
		}
	}
	
	private static InputManager instance;
	
	private List<Key> keylist = new ArrayList<Key>();
	
	private HashMap<String, Boolean> keys = new HashMap<String, Boolean>();
	
	private InputManager() {
		keylist.add(new Key("up", 			Keyboard.KEY_UP, Keyboard.KEY_W));
		keylist.add(new Key("down", 		Keyboard.KEY_DOWN, Keyboard.KEY_S));
		keylist.add(new Key("left", 		Keyboard.KEY_LEFT, Keyboard.KEY_A));
		keylist.add(new Key("right", 		Keyboard.KEY_RIGHT, Keyboard.KEY_D));
		keylist.add(new Key("debug", 		Keyboard.KEY_F3));
		keylist.add(new Key("screenshot", 	Keyboard.KEY_F2));
		keylist.add(new Key("regen", 		Keyboard.KEY_R));
		keylist.add(new Key("change", 		Keyboard.KEY_F));
		keylist.add(new Key("godmode", 		Keyboard.KEY_G));
		keylist.add(new Key("lightadd", 	Keyboard.KEY_ADD));
		keylist.add(new Key("lightsub", 	Keyboard.KEY_MINUS));
		keylist.add(new Key("lighteq", 		Keyboard.KEY_EQUALS));
	}
	
	private void updateInst() {
		Key k;
		for (int i = 0; i < keylist.size(); i++) {
			k = keylist.get(i);
			keys.put(k.name, oneDown(k.keys));
		}
	}
	
	private boolean oneDown(int... keys) {
		for (int i : keys) {
			if (Keyboard.isKeyDown(i)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean downInst(String key) {
		return keys.get(key);
	}
	
	private static InputManager inst() {
		if (instance == null) {
			return instance = new InputManager();
		}
		return instance;
	}
	
	public static void update() {
		inst().updateInst();
	}
	
	public static boolean down(String key) {
		return inst().downInst(key);
	}
	
}
