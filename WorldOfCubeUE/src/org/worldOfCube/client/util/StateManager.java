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
package org.worldOfCube.client.util;

import static org.lwjgl.opengl.ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import org.universeengine.opengl.texture.UniTexture;

public final class StateManager {

	private static final int NONE = 0;

	private static StateManager instance = null;

	private boolean useTexRect;
	private int lastBoundTex;

	private StateManager() {
		useTexRect = false;
		lastBoundTex = NONE;
	}

	private static StateManager instance() {
		if (instance == null) {
			return instance = new StateManager();
		}
		return instance;
	}

	private void useTexRectInst(boolean useit) {
		useTexRect = useit;
		if (useTexRect) {
			glDisable(GL_TEXTURE_2D);
			glEnable(GL_TEXTURE_RECTANGLE_ARB);
		} else {
			glDisable(GL_TEXTURE_RECTANGLE_ARB);
			glEnable(GL_TEXTURE_2D);
		}
	}

	private void bindTextureInst(UniTexture tex) {
		if (useTexRect) {
			if (tex == null) {
				if (lastBoundTex != NONE) {
					glBindTexture(GL_TEXTURE_RECTANGLE_ARB, NONE);
				}
				lastBoundTex = NONE;
				return;
			}
			if (lastBoundTex != tex.getTexID()) {
				glBindTexture(GL_TEXTURE_RECTANGLE_ARB, tex.getTexID());
			}
			lastBoundTex = tex.getTexID();
		} else {
			if (tex == null) {
				if (lastBoundTex != NONE) {
					glBindTexture(GL_TEXTURE_2D, NONE);
				}
				lastBoundTex = NONE;
				return;
			}
			if (lastBoundTex != tex.getTexID()) {
				glBindTexture(GL_TEXTURE_2D, tex.getTexID());
			}
			lastBoundTex = tex.getTexID();
		}
	}

	public static void useTexRect(boolean useit) {
		instance().useTexRectInst(useit);
	}

	public static boolean isUsingTexRect() {
		return instance().useTexRect;
	}

	public static void bindTexture(UniTexture tex) {
		instance().bindTextureInst(tex);
	}

}
