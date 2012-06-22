package org.worldOfCube.client.logic.chunks.generation.cave;

import java.util.Random;

import org.worldOfCube.client.util.Rand;

public class CavePathGenerator {
	
	private boolean[][] cave;
	private int size; 
	
	public CavePathGenerator(boolean[][] cave, int size, int startx, int starty, int dx, int dy, Random rand) {
		this.cave = cave;
		this.size = size;
		generate(rand, dx, dy, startx, starty);
	}
	
	private void generate(Random rand, int dx, int dy, int x, int y) {
		int repeat = (rand.nextInt()%50)+30;
		for (int i = 0; i < repeat; i++) {
			x += dx;
			y += dy;
			dx += Rand.plusMinusRangeInt(2, rand);
			dy += Rand.plusMinusRangeInt(2, rand);
			deletePart(x, y);
			System.out.println("Blah");
		}
	}
	
	private void deletePart(int x, int y) {
		delete(x-1, y-1);
		delete(x-1, y);
		delete(x-1, y+1);
		delete(x, y-1);
		delete(x, y);
		delete(x, y+1);
		delete(x+1, y-1);
		delete(x+1, y);
		delete(x+1, y+1);
	}
	
	private void delete(int x, int y) {
		if (x >= 0 && y >= 0 && x < size && y < size) {
			cave[x][y] = true;
		}
	}

}
