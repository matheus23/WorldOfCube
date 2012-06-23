package org.worldOfCube.client.util.interpolation;

public class Interpolation {
	
	private double secs;
	private double time;
	private float delta;
	private float start;
	
	public Interpolation(double secs, float start, float end) {
		this.secs = secs;
		this.start = start;
		this.delta = end-start;
	}
	
	public void setDelta(double time) {
		this.time = time;
	}
	
	public void tick(double delta) {
		setDelta(time+delta);
	}
	
	public float get() {
		return (float) (start + (delta * (time/secs)));
	}
	
	public boolean finished() {
		return time >= secs;
	}

}
