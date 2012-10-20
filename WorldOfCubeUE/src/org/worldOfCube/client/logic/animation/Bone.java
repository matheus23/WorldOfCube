/*
 * Copyright (c) 2012 matheusdev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.worldOfCube.client.logic.animation;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

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
