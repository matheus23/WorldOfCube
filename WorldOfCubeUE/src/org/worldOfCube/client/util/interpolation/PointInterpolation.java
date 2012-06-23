package org.worldOfCube.client.util.interpolation;

public class PointInterpolation {
	
	private Interpolation x;
	private Interpolation y;
	
	public PointInterpolation(double secs, float startx, float starty, float endx, float endy) {
		x = new Interpolation(secs, startx, endx);
		y = new Interpolation(secs, starty, endy);
	}
	
	public float getX() {
		return x.get();
	}
	
	public float getY() {
		return y.get();
	}
	
	public void tick(double delta) {
		x.tick(delta);
		y.tick(delta);
	}
	
	public boolean finished() {
		return x.finished() && y.finished();
	}
	
}
