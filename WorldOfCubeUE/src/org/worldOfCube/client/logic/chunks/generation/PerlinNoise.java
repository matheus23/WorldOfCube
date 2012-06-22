package org.worldOfCube.client.logic.chunks.generation;

import java.util.Random;

import org.worldOfCube.client.util.Rand;

public class PerlinNoise {

	private float[] values;
	private Random rand;
	private float minchange = 1f;
	private float smoothness;

	public PerlinNoise(int size, float seed1, float seed2, float minchange, float smoothness, Random rand) {
		this.rand = rand;
		this.minchange = minchange;
		this.smoothness = smoothness;
		int realsize = 2;
		while (realsize < size) {
			realsize *= 2;
		}
		values = new float[realsize + 1];
		values[0] = seed1;
		values[realsize] = seed2;
		noise(values.length / 2, values.length / 2);
	}

	private void noise(int pos, int step) {
		int hstep = step / 2;
		gen(pos, step);
		if (step != 1) {
			noise(pos - hstep, hstep);
			noise(pos + hstep, hstep);
		}
	}

	private void gen(int pos, int step) {
		values[pos] = createValue(values[pos - step], values[pos + step], step / smoothness);
	}

	private float createValue(float val1, float val2, float change) {
		return ((val1 + val2) / 2f) + Rand.rangeFloat(change + minchange, rand);
	}
	
	public float[] get() {
		return values;
	}

}
