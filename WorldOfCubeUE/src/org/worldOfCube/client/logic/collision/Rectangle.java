package org.worldOfCube.client.logic.collision;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Rectangle {

	public double x;
	public double y;
	public double w;
	public double h;

	public Rectangle(double x, double y, double w, double h) {
		set(x, y, w, h);
	}
	
	/**
	 * @param other the other Rectangle to copy this one from.
	 */
	public Rectangle(Rectangle other) {
		set(other.x, other.y, other.w, other.h);
	}

	public boolean intersects(Rectangle r) {
		if ((x >= (r.x + r.w)) || ((x + w) <= r.x)) {
			return false;
		}
		if ((y >= (r.y + r.h)) || ((y + h) <= r.y)) {
			return false;
		}
		return true;
	}

	public boolean intersects(double rx, double ry, double rw, double rh) {
		if ((x >= (rx + rw)) || ((x + w) <= rx)) {
			return false;
		}
		if ((y >= (ry + rh)) || ((y + h) <= ry)) {
			return false;
		}
		return true;
	}

	public boolean contains(double px, double py) {
		return (px >= x && py >= y && px < (x + w) && py < (y + h));
	}

	public boolean contains(Rectangle r) {
		return (contains(r.x, r.y) && contains(r.x + r.w, r.y + r.h));
	}
	
	public void glVertex(int id) {
		switch (id) {
		case 0: glVertex2f((float)x, (float)y); break;
		case 1: glVertex2f((float)(x+w), (float)y); break;
		case 2: glVertex2f((float)(x+w), (float)(y+h)); break;
		case 3: glVertex2f((float)x, (float)(y+h)); break;
		default: throw new IllegalArgumentException("Argument \"id\" has wrong value (not 0-3): " + id);
		}
	}
	
	public double area() {
		return w*h;
	}
	
	public void render(boolean asline) {
		glBegin(asline ? GL_LINE_LOOP : GL_QUADS);
		{
			glVertex2f((float)x, (float)y);
			glVertex2f((float)(x+w), (float)y);
			glVertex2f((float)(x+w), (float)(y+h));
			glVertex2f((float)x, (float)(y+h));
		}
		glEnd();
	}
	
	public void set(double x, double y, double w, double h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public String toString() {
		return String.format("[Rectangle: (%G, %G | %G, %G)", x, y, w, h);
	}
	
}
