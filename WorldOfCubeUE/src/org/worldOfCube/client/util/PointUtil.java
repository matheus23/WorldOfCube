package org.worldOfCube.client.util;

import org.worldOfCube.client.logic.chunks.generation.trees.Pos;

public final class PointUtil {
	
	private PointUtil() {}
	
	public static Pos moveDirection(Pos p, double rot, double dist) {
		double rad = Math.toRadians(rot);
		Pos p2 = new Pos(p.x, p.y);
		p2.x += (int)(Math.sin(rad)*dist);
		p2.y += (int)(Math.cos(rad)*dist);
		return p2;
	}

}
