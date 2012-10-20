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

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

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
