package org.worldOfCube.client.logic.collision;

import org.worldOfCube.client.logic.chunks.World;

public class SideManager {
	
	public static final int TOP = 0;
	public static final int RIGHT = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT = 3;
	
	public static int getSide(float x, float y, World world) {
		int size = world.totalPix;
		if (x > y) {
			if (x+y > size) {
				return RIGHT;
			} else {
				return TOP;
			}
		} else {
			if (x+y > size) {
				return BOTTOM;
			} else {
				return LEFT;
			}
		}
	}
	
}
