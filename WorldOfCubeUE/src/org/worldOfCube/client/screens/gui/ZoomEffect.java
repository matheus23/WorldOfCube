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
package org.worldOfCube.client.screens.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.Random;

import org.lwjgl.input.Mouse;
import org.universeengine.display.UniDisplay;
import org.universeengine.opengl.texture.UniTexture;
import org.worldOfCube.client.logic.collision.Rectangle;
import org.worldOfCube.client.util.DragAction;
import org.worldOfCube.client.util.StateManager;
import org.worldOfCube.client.util.interpolation.PointInterpolation;

public class ZoomEffect {

	private class Zoom {
		public float x, y, w, h;

		public void toTexCoords(float totalW, float totalH, Zoom dest) {
			dest.x = x/totalW;
			dest.y = y/totalH;
			dest.w = w/totalW;
			dest.h = h/totalH;
		}

		@Override
		public String toString() {
			return String.format("[Zoom: x%G y%G w%G h%G]", x, y, w, h);
		}

		public Zoom move(float dx, float dy) {
			x += dx;
			y += dy;
			return this;
		}
	}

	private class ZoomInterpolation {

		private PointInterpolation p1;
		private PointInterpolation p2;
		private Zoom z;

		public ZoomInterpolation(double secs, Zoom z1, Zoom z2) {
			z = new Zoom();
			z.x = z1.x;
			z.y = z1.y;
			z.w = z1.w;
			z.h = z1.h;
			p1 = new PointInterpolation(secs, z1.x, z1.y, z2.x, z2.y);
			p2 = new PointInterpolation(secs, z1.w, z1.h, z2.w, z2.h);
		}

		public boolean finished() {
			return p1.finished() && p2.finished();
		}

		public void tick(double delta) {
			p1.tick(delta);
			p2.tick(delta);
		}

		public Zoom get() {
			z.x = p1.getX();
			z.y = p1.getY();
			z.w = p2.getX();
			z.h = p2.getY();
			return z;
		}

	}

	private static final Random rand = new Random();
	private static final double DEFAULT_TIME = 10.0;

	private Rectangle store = new Rectangle(0, 0, 1, 1);
	private UniTexture zoomTex;
	private float ratio;
	private Zoom zoom1;
	private Zoom zoom2;
	private ZoomInterpolation interpol;
	private Zoom render;
	private Rectangle[] nogo;
	private float minsize;
	private float maxsize;
	private DragAction drag = new DragAction();
	private float xoff;
	private float yoff;
	private float xvel;
	private float yvel;

	public ZoomEffect(UniTexture texture, float divisor, Rectangle... nogo) {
		this.zoomTex = texture;
		this.minsize = texture.getWidth()/divisor;
		this.maxsize = texture.getWidth();
		this.nogo = nogo;
		ratio = (float)600/(float)800;
		render = new Zoom();
		zoom1 = new Zoom();
		randomize(zoom1, ratio, minsize, maxsize, zoomTex.getWidth(), zoomTex.getHeight());
		zoom2 = new Zoom();
		randomize(zoom2, ratio, minsize, maxsize, zoomTex.getWidth(), zoomTex.getHeight());
		interpol = new ZoomInterpolation(DEFAULT_TIME, zoom1, zoom2);
	}

	public static void randomize(Zoom z, float ratio, float minsize, float maxsize, float totalw, float totalh) {
		float deltasize = maxsize-minsize;
		float width = minsize + (rand.nextFloat()*deltasize);
		float height = width*ratio;
		float x = rand.nextFloat()*(totalw-width);
		float y = rand.nextFloat()*(totalh-height);
		z.x = x;
		z.y = y;
		z.w = width;
		z.h = height;
	}

	public boolean isNogo(Zoom z) {
		store.set(z.x, z.y, z.w, z.h);
		for (int i = 0; i < nogo.length; i++) {
			if (nogo[i].intersects(store)) {
				return true;
			}
		}
		return false;
	}

	public void tick(double delta) {
		drag.tick();

		int dx = drag.getVX();
		int dy = drag.getVY();
		if (dx != 0) xvel = Math.min(10f, dx);
		if (dy != 0) yvel = Math.min(10f, dy);
		xoff += xvel;
		yoff += yvel;
		if (!Mouse.isButtonDown(0)) {
			xvel /= 1.2f;
			xoff /= 1.05f;
			yvel /= 1.2f;
			yoff /= 1.05f;
		}

		if (interpol.finished()) {
			zoom1 = zoom2;
			do {
				randomize((zoom2 = new Zoom()),
						ratio,
						minsize,
						maxsize,
						zoomTex.getWidth(),
						zoomTex.getHeight());
			} while (!isNogo(zoom2));
			interpol = new ZoomInterpolation(DEFAULT_TIME, zoom1, zoom2);
		}
		interpol.get().move(xoff, yoff).toTexCoords(zoomTex.getWidth(), zoomTex.getHeight(), render);
		interpol.tick(Mouse.isButtonDown(0) ? 0.0 : delta);
	}

	public void render(UniDisplay display) {
		StateManager.useTexRect(false);
		StateManager.bindTexture(zoomTex);

		glBegin(GL_QUADS);
		{
			glTexCoord2f(clamp(render.x), clamp(render.y));
			glVertex2f(0f, 0f);

			glTexCoord2f(clamp(render.x+render.w), clamp(render.y));
			glVertex2f(display.getWidth(), 0f);

			glTexCoord2f(clamp(render.x+render.w), clamp(render.y+render.h));
			glVertex2f(display.getWidth(), display.getHeight());

			glTexCoord2f(clamp(render.x), clamp(render.y+render.h));
			glVertex2f(0f, display.getHeight());
		}
		glEnd();
	}

	private float clamp(float f) {
		return Math.min(1f, Math.max(0f, f));
	}

}
