package org.worldOfCube.client.util.vecmath;

public class Vec {
	
	public double x;
	public double y;
	
	public Vec(double x, double y) {
		set(x, y);
	}
	
	public Vec(double x0, double y0, double x1, double y1) {
		set(x1-x0, y1-y0);
	}
	
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double length() {
		return Math.sqrt(x*x+y*y);
	}
	
	public void normalize() {
		div(length());
	}
	
	public void add(double f) {
		x += f;
		y += f;
	}
	
	public void sub(double f) {
		x -= f;
		y -= f;
	}
	
	public void mul(double f) {
		x *= f;
		y *= f;
	}
	
	public void div(double f) {
		x /= f;
		y /= f;
	}
	
}
