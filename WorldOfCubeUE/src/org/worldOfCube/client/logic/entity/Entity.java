package org.worldOfCube.client.logic.entity;

import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.logic.collision.Rectangle;

public abstract class Entity {
	
	protected Rectangle rect;
	protected double dx;
	protected double dy;
	protected World world;
	protected double time;
	
	public Entity(float x, float y, float w, float h, World world) {
		rect = new Rectangle(x, y, w, h);
		this.world = world;
	}
	
	public abstract void tick(double d);
	
	public abstract void render();
	
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
	
	public void move(double dx2, double dy2) {
		if (Double.isNaN(dx2) || Double.isNaN(dy2) || Double.isInfinite(dx2) || Double.isInfinite(dy2)) {
			return;
		}
		if (moveAxis(dx2, true)) {
			dx = 0.0;
		}
		if (moveAxis(dy2, false)) {
			dy = 0.0;
		}
	}
	
	public boolean positionEmpty(float deltax, float deltay) {
		rect.x += deltax;
		rect.y += deltay;
		boolean empty = !colliding();
		rect.x -= deltax;
		rect.y -= deltay;
		return empty;
	}
	
	private boolean moveAxis(double delta, boolean xaxis) {
		double move = 0.0;
		while (delta != 0.0) {
			move = Math.max(-1.0, Math.min(1.0, delta));
			delta -= move;
			if (xaxis) {
				rect.x += move;
			} else {
				rect.y += move;
			}
			if (colliding()) {
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
	
	public boolean colliding() {
		return !world.isFieldFree(rect)/* || !world.bounds.contains(rect)*/;
	}

}
