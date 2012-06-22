package org.worldOfCube.client.util;

public final class Distance {
	
	public static double squared(double x1, double y1, double x2, double y2) {
		double dx = (x2-x1);
		double dy = (y2-y1);
		return dx*dx + dy*dy ;
	}
	
	public static float squared(float x1, float y1, float x2, float y2) {
		float dx = (x2-x1);
		float dy = (y2-y1);
		return dx*dx + dy*dy ;
	}

	public static int squared(int x1, int y1, int x2, int y2) {
		int dx = (x2-x1);
		int dy = (y2-y1);
		return dx*dx + dy*dy ;
	}
	
	public static double get(double x1, double y1, double x2, double y2) {
		return Math.sqrt(squared(x1, y1, x2, y2));
	}
	
	public static float get(float x1, float y1, float x2, float y2) {
		return (float)Math.sqrt(squared(x1, y1, x2, y2));
	}
	
	public static int get(int x1, int y1, int x2, int y2) {
		return (int)Math.sqrt(squared(x1, y1, x2, y2));
	}
	
	public static int getLinear(int x1, int y1, int x2, int y2) {
		int dx = x2-x1;
		int dy = y2-y1;
		return (Math.abs(dx)+Math.abs(dy))/2;
	}
	
	public static float getLinear(float x1, float y1, float x2, float y2) {
		float dx = x2-x1;
		float dy = y2-y1;
		return (Math.abs(dx)+Math.abs(dy))/2;
	}
	
	public static double getLinear(double x1, double y1, double x2, double y2) {
		double dx = x2-x1;
		double dy = y2-y1;
		return (Math.abs(dx)+Math.abs(dy))/2;
	}

}
