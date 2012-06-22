package org.worldOfCube.client.logic.chunks.generation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class PerlinNoise2D {
	
	private float[][] values;
	private float roughness;
	private float varyation;
	private Random rand;

	public PerlinNoise2D(int size, float roughness, float varyation, float seedmid, Random rand) {
		this.roughness = roughness;
		this.varyation = varyation;
		this.rand = rand;
		
		int realsize = 1;
		while(realsize < size) {
			realsize *= 2;
		}
		realsize += 1;
		values = new float[realsize][realsize];
		
		int firststep = realsize/2;
		values[0][0] = 0f;
		values[realsize-1][0] = 0f;
		values[0][realsize-1] = 0f;
		values[realsize-1][realsize-1] = 0f;
		values[0][firststep] = 0f;
		values[firststep][0] = 0f;
		values[firststep][firststep] = seedmid;
		values[firststep][realsize-1] = 0f;
		values[realsize-1][firststep] = 0f;
		
		noise(0, 0, realsize-1);
	}
	
	private void noise(int x, int y, int step) {
		if (step > 1) {
			int halfStep = step/2;
			float vary = (step*varyation)*roughness;
			
			values[x+halfStep][y] = values[x+halfStep][y] == 0f ? 
					vary(mid(values[x][y], values[x+step][y]), vary) : values[x+halfStep][y];
					
			values[x][y+halfStep] = values[x][y+halfStep] == 0f ? 
					vary(mid(values[x][y], values[x][y+step]), vary) : values[x][y+halfStep];
			
			values[x+step][y+halfStep] = values[x+step][y+halfStep] == 0f ? 
					vary(mid(values[x+step][y], values[x+step][y+step]), vary) : values[x+step][y+halfStep];
					
			values[x+halfStep][y+step] = values[x+halfStep][y+step] == 0f ? 
					vary(mid(values[x][y+step], values[x+step][y+step]), vary) : values[x+halfStep][y+step];
			
			values[x+halfStep][y+halfStep] = vary(mid(
					values[x][y],
					values[x+halfStep][y],
					values[x+step][y],
					values[x][y+halfStep],
					values[x+step][y+halfStep],
					values[x][y+step],
					values[x+halfStep][y+step],
					values[x+step][y+step]), vary);
			
			noise(x, y, halfStep);
			noise(x+halfStep, y, halfStep);
			noise(x, y+halfStep, halfStep);
			noise(x+halfStep, y+halfStep, halfStep);
		}
	}
	
	public float randRange(float range, Random rand) {
		return rand.nextBoolean() ? -rand.nextFloat()*range : rand.nextFloat()*range;
	}
	
	public float vary(float val, float vary) {
		return val+randRange(vary, rand);
	}
	
	public float mid(float... vals) {
		float sum = 0;
		for (int i = 0; i < vals.length; i++) {
			sum += vals[i];
		}
		return sum / vals.length;
	}
	
	public float[][] get() {
		return values;
	}
	
	public static void main(String[] args) {
		System.out.println("Started.");
		int size = 512;
		PerlinNoise2D pn2d = new PerlinNoise2D(size, 0.4f, 1, 20000f, new Random());
		float[][] vals = pn2d.get();
		BufferedImage img = new BufferedImage(size+1, size+1, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < vals.length; x++) {
			for (int y = 0; y < vals[x].length; y++) {
				img.setRGB(x, y, ((int)vals[x][y]) | 0xFF000000);
			}
		}
		try {
			saveImg(new File("Heightmap.png"), img);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished.");
	}
	
	public static void saveImg(File f, BufferedImage img) throws IOException {
		f.createNewFile();
		ImageIO.write(img, "PNG", f);
	}
	
}
