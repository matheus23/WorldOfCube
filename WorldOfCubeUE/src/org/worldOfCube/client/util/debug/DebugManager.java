package org.worldOfCube.client.util.debug;

import org.worldOfCube.client.input.InputManager;

public class DebugManager {
	
	private static DebugManager instance;
	
	private boolean debug = false;
	private boolean pressedDebug = false;
	
	private void updateInst()  {
		if (InputManager.down("debug") && !pressedDebug) {
			pressedDebug = true;
		}
		if (!InputManager.down("debug") && pressedDebug) {
			debug = !debug;
			pressedDebug = false;
		}
	}
	
	private boolean isDebugInst() {
		return debug;
	}
	
	private static DebugManager inst() {
		if (instance == null) {
			return instance = new DebugManager();
		}
		return instance;
	}
	
	public static void update() {
		inst().updateInst();
	}
	
	public static boolean isDebug() {
		return inst().isDebugInst();
	}
	
}
