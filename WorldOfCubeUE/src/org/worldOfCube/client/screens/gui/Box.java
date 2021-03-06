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
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2i;

import java.awt.Rectangle;

import org.lwjgl.input.Mouse;
import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.res.Sprite;
import org.worldOfCube.client.util.StateManager;

public class Box implements GUIElement {

	public static final byte STATE_NONE = 0;
	public static final byte STATE_HOVER = 1;
	public static final byte STATE_CLICKED = 2;

	public static final float COLOR_TOP = 0.2f;
	public static final float COLOR_S_NONE = 0.1f;
	public static final float COLOR_S_HOVER = 0.3f;
	public static final float COLOR_S_CLICKED = 0.5f;

	private static final int SIZE = ResLoader.BORDER_SIZE;
	private static final int HSIZE = SIZE/2;

	private Rectangle rect = new Rectangle();
	private byte state = STATE_NONE;
	private float color = COLOR_S_NONE;
	private float alpha = 1f;
	private int borderSpriteSheet;
	private boolean pressedMB;
	private boolean ignoreClick;

	public Box(int x, int y, int w, int h, int borderSpriteSheetID) {
		set(x, y, w, h);
		this.borderSpriteSheet = borderSpriteSheetID;
	}

	public void set(int x, int y, int w, int h) {
		rect.x = x;
		rect.y = y;
		rect.width = w;
		rect.height = h;
	}

	@Override
	public Rectangle getRect() {
		return rect;
	}

	public byte getState() {
		return state;
	}

	private Sprite getTopLeft() {
		return ResLoader.get(borderSpriteSheet, ResLoader.Borders.GUI_BORDER_TL.ordinal());
	}

	private Sprite getTopRight() {
		return ResLoader.get(borderSpriteSheet, ResLoader.Borders.GUI_BORDER_TR.ordinal());
	}

	private Sprite getBotLeft() {
		return ResLoader.get(borderSpriteSheet, ResLoader.Borders.GUI_BORDER_BL.ordinal());
	}

	private Sprite getBotRight() {
		return ResLoader.get(borderSpriteSheet, ResLoader.Borders.GUI_BORDER_BR.ordinal());
	}

	private Sprite getTop() {
		return ResLoader.get(borderSpriteSheet, ResLoader.Borders.GUI_BORDER_T.ordinal());
	}

	private Sprite getBot() {
		return ResLoader.get(borderSpriteSheet, ResLoader.Borders.GUI_BORDER_B.ordinal());
	}

	private Sprite getLeft() {
		return ResLoader.get(borderSpriteSheet, ResLoader.Borders.GUI_BORDER_L.ordinal());
	}

	private Sprite getRight() {
		return ResLoader.get(borderSpriteSheet, ResLoader.Borders.GUI_BORDER_R.ordinal());
	}

	public void setState(byte state) {
		if (state == STATE_NONE
				|| state == STATE_HOVER
				|| state == STATE_CLICKED) {
			this.state = state;
			return;
		}
		throw new IllegalArgumentException("Argument \"state\" has wrong value: " + state);
	}

	public void setAlpha(float alpha) {
		this.alpha = Math.max(0f, Math.min(1f, alpha));
	}

	public float getAlpha() {
		return alpha;
	}

	@Override
	public void tick(UniDisplay display) {
		int mx = Mouse.getX();
		int my = display.getHeight()-Mouse.getY();
		boolean containsClick = rect.contains(mx, my);
		if (Mouse.isButtonDown(0) && !pressedMB) {
			pressedMB = true;
			if (!containsClick) {
				ignoreClick = true;
			}
		} else if (!Mouse.isButtonDown(0) && pressedMB) {
			pressedMB = false;
			ignoreClick = false;
		}
		if (containsClick) {
			state = STATE_HOVER;
			if (!ignoreClick && Mouse.isButtonDown(0)) {
				state = STATE_CLICKED;
			}
			recalcColor();
		} else {
			state = STATE_NONE;
			recalcColor();
		}
	}

	public void recalcColor() {
		switch(state) {
		case STATE_NONE:
			color = COLOR_S_NONE;
			break;
		case STATE_HOVER:
			color = COLOR_S_HOVER;
			break;
		case STATE_CLICKED:
			color = COLOR_S_CLICKED;
			break;
		}
	}

	@Override
	public void render() {
		if (alpha != 0f) {
			StateManager.bindTexture(null);
			glBegin(GL_QUADS);
			{
				glColor4f(COLOR_TOP, COLOR_TOP, COLOR_TOP, alpha);
				glVertex2i(rect.x+HSIZE,            rect.y+HSIZE);
				glVertex2i(rect.x+rect.width-HSIZE, rect.y+HSIZE);
				glColor4f(color, color, color, alpha);
				glVertex2i(rect.x+rect.width-HSIZE, rect.y+rect.height-HSIZE);
				glVertex2i(rect.x+HSIZE,            rect.y+rect.height-HSIZE);
			}
			glEnd();

			glColor4f(1f, 1f, 1f, 1f);
			getTopLeft().bindAndRender(	rect.x, 				rect.y,						SIZE, SIZE);
			getTopRight().bindAndRender(rect.x+rect.width-SIZE, rect.y, 					SIZE, SIZE);
			getBotLeft().bindAndRender(	rect.x, 				rect.y+rect.height-SIZE, 	SIZE, SIZE);
			getBotRight().bindAndRender(rect.x+rect.width-SIZE, rect.y+rect.height-SIZE, 	SIZE, SIZE);

			getTop().bindAndRender(		rect.x+SIZE, 			rect.y, 					rect.width-SIZE-SIZE, 	SIZE);
			getLeft().bindAndRender(	rect.x, 				rect.y+SIZE, 				SIZE, 					rect.height-SIZE-SIZE);
			getRight().bindAndRender(	rect.x+rect.width-SIZE,	rect.y+SIZE, 				SIZE, 					rect.height-SIZE-SIZE);
			getBot().bindAndRender(		rect.x+SIZE, 			rect.y+rect.height-SIZE, 	rect.width-SIZE-SIZE, 	SIZE);
		}
	}

}
