package org.worldOfCube.client.logic.animation;

import static org.lwjgl.opengl.GL11.*;

public class Skeleton {
	
	private Bone rootBone;
	private double x;
	private double y;
	private boolean debug = false;
	
	public Skeleton(float absx, float absy, Bone rootBone) {
		this.rootBone = rootBone;
		this.x = absx;
		this.y = absy;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public Bone getRoot() {
		return rootBone;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void tick(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void render(boolean mirrorx, float light) {
		glDisable(GL_CULL_FACE);
		glColor3f(light, light, light);
		glPushMatrix(); {
			glTranslatef((float)x, (float)y, 0f);
			glScalef((mirrorx ? -1f : 1f), 1f, 1f);
			rootBone.render(debug);
		} glPopMatrix();
		glEnable(GL_CULL_FACE);
	}

}
