package org.worldOfCube.client.logic.chunks.light;

import org.worldOfCube.client.util.Distance;

/**
 * This class is for pre-rendering circlyc lights, so they
 * don't have to be generated at real-time for every light source.
 * @author matheusdev
 *
 */
public class RenderedLight {
	
	private float[][] vals;
	private int size;
	private int radius;
	private float strength;
	
	/**
	 * Constructor.
	 * Creates a new Light with a radius and a strength.
	 * @param radius
	 * @param strength
	 */
	public RenderedLight(int radius, float strength) {
		this.strength = strength;
		this.radius = radius;
		size = radius*2;
		vals = new float[radius*2][radius*2];
		gen(radius);
	}
	
	/**
	 * Calculates the values for each position in the float[][] matrix, "vals".
	 * It interpolates "strength" with 0 to radius, with the distance between the middle
	 * and the current x and y iteration position.
	 * @param radius
	 */
	public void gen(int radius) {
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				vals[x][y] = (1f-Math.min(1f, (Distance.get((float)radius, (float)radius, (float)x, (float)y) / (float)(radius))))*strength;
			}
		}
	}
	
	/**
	 * @param lx the relative light x position to get the light value for.
	 * @param ly the relative light y position to get the light value for.
	 * @return the light value relative to the middle of the light, or 0,
	 * if the wanted value is not in range anymore,
	 * so getMidRelative(0, 0) returns the light, set in the middle of this
	 * RenderedLight.
	 */
	public float getMidRelative(int lx, int ly) {
		int x = lx+size/2;
		int y = ly+size/2;
		if (isValid(x, y)) {
			return vals[x][y];
		}
		return 0f;
	}
	
	/**
	 * @param x the light x position.
	 * @param y the light y position.
	 * @return whether this position is inside the calculated values, or not.
	 */
	public boolean isValid(int x, int y) {
		return (x >= 0 && y >= 0 && x < size && y < size); 
	}
	
	/**
	 * This Method can cause a ArrayIndexOutOfBoundsException,
	 * if (x, y) are out of range. You can check the range
	 * with "isValid(int, int)".
	 * @param x the light x position.
	 * @param y the light y position.
	 * @return the pre-rendered Light value from the float[][]. at (x, y).
	 */
	public float get(int x, int y) {
		return vals[x][y];
	}
	
	/**
	 * @return the size of this RenderedLight,
	 * which is always the radius * 2.
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * @return the strength of this light.
	 */
	public float getStrength() {
		return strength;
	}
	
	/**
	 * @return the radius of this light.
	 */
	public int getRadius() {
		return radius;
	}
	
	/**
	 * This method calculates the Position in the array
	 * to get the light value from, with an absolute light
	 * position and absolute x and y positions.
	 * @param lightx the absolute light x position.
	 * @param lighty the absolute light y position.
	 * @param x the absolute x position to read relative from.
	 * @param y the absolute y position to read relative from.
	 * @return the light value from the calculated position.
	 */
	public float get(int lightx, int lighty, int x, int y) {
		int dx = Math.abs(x-lightx)+radius;
		int dy = Math.abs(y-lighty)+radius;
		if (dx < size && dy < size) {
			return get(dx, dy);
		} else {
			return 0f;
		}
	}
	
}
