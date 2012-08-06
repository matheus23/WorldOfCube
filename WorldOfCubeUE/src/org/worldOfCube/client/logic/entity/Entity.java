package org.worldOfCube.client.logic.entity;

import org.worldOfCube.client.input.WorldInputListener;
import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.logic.collision.Rectangle;

public abstract class Entity implements WorldInputListener {
	
	protected Rectangle rect;
	protected double dx;
	protected double dy;
	protected double time;
	
	public Entity(double x, double y, double w, double h) {
		rect = new Rectangle(x, y, w, h);
	}
	
	public abstract void tick(double d, World world);
	
	public abstract void render(World world);
	
	public void afterTick(double delta) {
		time += delta;
	}
	
	public double midx() {
		return rect.x+(rect.w/2);
	}
	
	public double midy() {
		return rect.y+(rect.h/2);
	}
	
	public Rectangle getRect() {
		return rect;
	}
	
	public void move(double dx2, double dy2, World world) {
		if (Double.isNaN(dx2) || Double.isNaN(dy2) || Double.isInfinite(dx2) || Double.isInfinite(dy2)) {
			return;
		}
		if (moveAxis(dx2, true, world)) {
			dx = 0.0;
		}
		if (moveAxis(dy2, false, world)) {
			dy = 0.0;
		}
	}
	
	public boolean positionEmpty(float deltax, float deltay, World world) {
		rect.x += deltax;
		rect.y += deltay;
		boolean empty = !colliding(world);
		rect.x -= deltax;
		rect.y -= deltay;
		return empty;
	}
	
	public void handleKeyEvent(int keyCode, char keyChar, boolean down, World world) {}
	
	public void handleMouseEvent(int mousex, int mousey, int button, boolean down, World worl) {}
	
	public void handleMousePosition(int mousex, int mousey, World world) {}
	
	private boolean moveAxis(double delta, boolean xaxis, World world) {
		double move = 0.0;
		while (delta != 0.0) {
			move = Math.max(-1.0, Math.min(1.0, delta));
			delta -= move;
			if (xaxis) {
				rect.x += move;
			} else {
				rect.y += move;
			}
			if (colliding(world)) {
				if (xaxis) {
					rect.x -= move;
				} else {
					rect.y -= move;
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean colliding(World world) {
		return !world.isFieldFree(rect)/* || !world.bounds.contains(rect)*/;
	}

}
