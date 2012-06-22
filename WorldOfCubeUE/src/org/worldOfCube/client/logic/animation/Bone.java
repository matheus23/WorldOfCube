package org.worldOfCube.client.logic.animation;

import static org.lwjgl.opengl.GL11.*;

import org.worldOfCube.client.res.Sprite;
import org.worldOfCube.client.util.StateManager;

public class Bone {
	
	private double length;
	private Bone[] childs;
	private double rotation;
	private double origx;
	private double origy;
	private double w;
	private double h;
	private Sprite sprite;
	
	public Bone(Sprite sprite, double length, double origx, double origy, double w, double h, Bone... bones) {
		this.sprite = sprite;
		this.childs = bones;
		this.length = length;
		this.origx = origx;
		this.origy = origy;
		this.w = w;
		this.h = h;
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public void setRotation(double rot) {
		rotation = rot;
	}
	
	public double getRotation() {
		return rotation;
	}
	
	public void render(boolean debug) {
		glPushMatrix(); {
			
			glRotatef((float)rotation, 0f, 0f, 1f);
			
			if (sprite != null) {
				renderIntern();
			}
			
			if (debug) {
				StateManager.bindTexture(null);
				glBegin(GL_LINES);
				{
					glColor3f(1f, 0f, 0f);
					glVertex2f(0f, 0f);
					glColor3f(0f, 1f, 0f);
					glVertex2f(0f, (float)length);
				}
				glEnd();
			}
			
			glTranslatef(0f, (float)length, 0f);
			
			if (childs != null) {
				for (int i = 0; i < childs.length; i++) {
					childs[i].render(debug);
				}
			}
		} glPopMatrix();
	}
	
	private void renderIntern() {
		sprite.bind();
		glBegin(GL_QUADS);
		{
			sprite.glTexCoord(0);
			glVertex2f((float)(0f-origx), (float)(0f-origy));
			sprite.glTexCoord(1);
			glVertex2f((float)(w-origx), (float)(0f-origy));
			sprite.glTexCoord(2);
			glVertex2f((float)(w-origx), (float)(h-origy));
			sprite.glTexCoord(3);
			glVertex2f((float)(0f-origx), (float)(h-origy));
		}
		glEnd();
	}

}
