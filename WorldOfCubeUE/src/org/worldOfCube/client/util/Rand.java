package org.worldOfCube.client.util;

import java.util.Random;

public class Rand {
	
	public static int rangeInt(int range, Random rand) {
		return rand.nextInt()%range;
	}
	
	public static int plusMinusRangeInt(int range, Random rand) {
		return rand.nextBoolean() ? -(rand.nextInt()%range) : rand.nextInt()%range;
	}
	
	public static float rangeFloat(float range, Random rand) {
		return rand.nextBoolean() ? rand.nextFloat()*range : -rand.nextFloat()*range;
	}
	
}
