package org.worldOfCube.client.util.bresenhamline;

import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.util.Distance;


public final class Placer {

	private Placer() {
	}

	public static void line(int x1, int y1, int x2, int y2, PlaceAction la) {
		if ((x1 - x2) > 0) {
			line(x2, y2, x1, y1, la);
			return;
		}
		if (Math.abs(y2 - y1) > Math.abs(x2 - x1)) {
			line2(y1, x1, y2, x2, la);
			return;
		}
		int x = x1, y = y1, sum = x2 - x1, dx = 2 * (x2 - x1), dy = Math.abs(2 * (y2 - y1));
		int prirastokDy = ((y2 - y1) > 0) ? 1 : -1;

		for (int i = 0; i <= x2 - x1; i++) {
			la.action(x, y);
			x++;
			sum -= dy;
			if (sum < 0) {
				y = y + prirastokDy;
				sum += dx;
			}
		}
	}

	private static void line2(int x3, int y3, int x4, int y4, PlaceAction la) {
		if ((x3 - x4) > 0) {
			line2(x4, y4, x3, y3, la);
			return;
		}

		int x = x3, y = y3, sum = x4 - x3, Dx = 2 * (x4 - x3), Dy = Math.abs(2 * (y4 - y3));
		int prirastokDy = ((y4 - y3) > 0) ? 1 : -1;

		for (int i = 0; i <= x4 - x3; i++) {
			la.action(y, x);
			x++;
			sum -= Dy;
			if (sum < 0) {
				y = y + prirastokDy;
				sum += Dx;
			}
		}
	}
	
	public static void circle(int rx, int ry, double dist, PlaceAction pa, World world) {
		int beginx = rx-(int)dist;
		int beginy = ry-(int)dist;
		int endx = rx+(int)dist;
		int endy = ry+(int)dist;
		beginx = Math.max(0, beginx);
		beginy = Math.max(0, beginy);
		endx = Math.min(world.totalBlocks, endx);
		endy = Math.min(world.totalBlocks, endy);
		double drx = (double)rx;
		double dry = (double)ry;
		for (int x = beginx; x < endx; x++) {
			for (int y = beginy; y < endy; y++) {
				if (Distance.get(drx, dry, (double)x, (double)y) <= dist) {
					pa.action(x, y);
				}
			}
		}
	}

}
