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

import org.universeengine.opengl.texture.UniTexture;
import org.worldOfCube.Log;
import org.worldOfCube.client.util.StateManager;

public class SpriteSheet {

	private UniTexture tex;
	private Sprite[] sprites;
	private boolean texRect;

	public SpriteSheet(String loadpath, int size, boolean texRect) {
		this.texRect = texRect;
		tex = ResLoader.loadTex(loadpath, texRect);

		sprites = new Sprite[size];
	}

	public UniTexture getTexture() {
		return tex;
	}

	public SpriteSheet(UniTexture tex, int size) {
		this.tex = tex;
		sprites = new Sprite[size];
	}

	public void bind() {
		StateManager.useTexRect(texRect);
		StateManager.bindTexture(tex);
	}

	public void render(int spritenum, float x, float y, float w, float h) {
		bind();
		sprites[spritenum].render(x, y, w, h);
	}

	public Sprite getSprite(int spritenum) {
		return sprites[spritenum];
	}

	public Sprite giveSprite(int index, float x, float y, float w, float h) {
		float u = x;
		float v = y;
		float s = u+w;
		float t = v+h;
		if (!texRect) {
			u /= tex.getWidth();
			v /= tex.getHeight();
			s /= tex.getWidth();
			t /= tex.getHeight();
			if (u > 1f || s > 1f || t > 1f || v > 1f) {
				Log.err("Invalid values... TexCoords index " + index + " with texture index " + tex.getTexID());
			}
		}
		return sprites[index] = new Sprite(this, u, v, s, t);
	}

	public void delete() {
		tex.delete();
	}

}
