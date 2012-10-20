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
package org.worldOfCube.client.res;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Sprite {

	private SpriteSheet sheet;
	private float u, v, s, t;

	public Sprite(SpriteSheet sheet, float u, float v, float s, float t) {
		this.sheet = sheet;
		this.u = u;
		this.v = v;
		this.s = s;
		this.t = t;
	}

	public void glTexCoord(int edgenum) {
		switch(edgenum) {
		case 0: glTexCoord2f(u, v); break;
		case 1: glTexCoord2f(s, v); break;
		case 2: glTexCoord2f(s, t); break;
		case 3: glTexCoord2f(u, t); break;
		default: throw new IllegalArgumentException("Unknown given \"edgenum\": " + edgenum);
		}
	}

	public float getCoord(int edgenum, boolean x) {
		if (x) {
			switch(edgenum) {
			case 0: return u;
			case 1: return s;
			case 2: return s;
			case 3: return u;
			default: throw new IllegalArgumentException("Unknown given \"edgenum\": " + edgenum);
			}
		} else {
			switch(edgenum) {
			case 0: return v;
			case 1: return v;
			case 2: return t;
			case 3: return t;
			default: throw new IllegalArgumentException("Unknown given \"edgenum\": " + edgenum);
			}
		}
	}

	public SpriteSheet getSheet() {
		return sheet;
	}

	public void bind() {
		sheet.bind();
	}

	public void bindAndRender(float x, float y, float w, float h) {
		sheet.bind();
		render(x, y, w, h);
	}

	public void render(float x, float y, float w, float h) {
		glBegin(GL_QUADS);
		{
			glTexCoord(0);
			glVertex2f(x, y);

			glTexCoord(1);
			glVertex2f(x + w, y);

			glTexCoord(2);
			glVertex2f(x + w, y + h);

			glTexCoord(3);
			glVertex2f(x, y + h);
		}
		glEnd();
	}

}
