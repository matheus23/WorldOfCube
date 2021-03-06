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

import static org.lwjgl.opengl.GL11.glColor4f;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.input.WrappedMouse;
import org.worldOfCube.client.res.GLFont;
import org.worldOfCube.client.res.ResLoader;

public class BoxLabel implements GUIElement {

	private Box box;
	private String text;
	private int size;
	private float r = 1f;
	private float g = 1f;
	private float b = 1f;
	private List<BoxLabelListener> listeners = new ArrayList<BoxLabelListener>();
	private boolean pressed = false;
	private String infoText;
	private boolean showInfo;

	public BoxLabel(String text) {
		this(0, 0, 1, 1, text, 20, ResLoader.Sheets.GUI_BORDER_BLUE.ordinal());
	}

	public BoxLabel(String text, int borderID, BoxLabelListener bll) {
		this(0, 0, 1, 1, text, 20, borderID);
		addBoxLabelListener(bll);
	}

	public BoxLabel(String text, BoxLabelListener bll) {
		this(0, 0, 1, 1, text, 20, ResLoader.Sheets.GUI_BORDER_BLUE.ordinal());
		addBoxLabelListener(bll);
	}

	public BoxLabel(int x, int y, int w, int h, String text, BoxLabelListener bll) {
		this(x, y, w, h, text, 20, ResLoader.Sheets.GUI_BORDER_BLUE.ordinal());
		addBoxLabelListener(bll);
	}

	public BoxLabel(int x, int y, int w, int h, String text, int borderID, BoxLabelListener bll) {
		this(x, y, w, h, text, 20, borderID);
		addBoxLabelListener(bll);
	}

	public BoxLabel(int x, int y, int w, int h, String text, int borderID) {
		this(x, y, w, h, text, 20, borderID);
	}

	public BoxLabel(int x, int y, int w, int h, String text) {
		this(x, y, w, h, text, 20, ResLoader.Sheets.GUI_BORDER_BLUE.ordinal());
	}

	public BoxLabel(int x, int y, int w, int h, String text, int size, int borderSprite) {
		this.text = text;
		this.size = size;
		box = new Box(x, y, w, h, borderSprite);
	}

	public void addBoxLabelListener(BoxLabelListener bll) {
		listeners.add(bll);
	}

	public void removeBoxLabelListener(BoxLabelListener bll) {
		listeners.remove(bll);
	}

	public BoxLabel withInfoText(String text) {
		String[] lines = text.split("\n");
		StringBuilder built = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			if (i == 0 || i == 1) {
				built.append(" " + lines[i] + "\n");
			} else {
				built.append(lines[i] + "\n");
			}
		}
		infoText = built.toString();
		return this;
	}

	@Override
	public void tick(UniDisplay display) {
		box.tick(display);
		if (box.getState() == Box.STATE_CLICKED && !pressed) {
			pressed = true;
			for (int i = 0; i < listeners.size(); i++) {
				listeners.get(i).boxPressed(this);
			}
		} else if (box.getState() == Box.STATE_HOVER && pressed) {
			pressed = false;
			for (int i = 0; i < listeners.size(); i++) {
				listeners.get(i).boxReleased(this);
			}
		} else if (box.getState() == Box.STATE_NONE) {
			pressed = false;
		}
		if (infoText != null && box.getRect().contains(WrappedMouse.getX(), WrappedMouse.getY())) {
			showInfo = true;
		} else {
			showInfo = false;
		}
	}

	public Box getBox() {
		return box;
	}

	@Override
	public Rectangle getRect() {
		return getBox().getRect();
	}

	public void setTextColor(float red, float green, float blue) {
		r = red;
		g = green;
		b = blue;
	}

	@Override
	public void render() {
		box.render();
		glColor4f(r, g, b, 1f);
		GLFont.render(box.getRect().x+box.getRect().width/2,
				box.getRect().y+box.getRect().height/2,
				GLFont.CENTER, text, size);
	}

	public void renderTwo() {
		if (showInfo) {
			int x = WrappedMouse.getX();
			int y = WrappedMouse.getY();
			int w = 0;
			int endw = 0;
			int h = 0;
			for (int i = 0; i < infoText.length(); i++) {
				char c = infoText.charAt(i);
				if (c == '\n') {
					h += 10;
					endw = Math.max(endw, w);
					w = 0;
				} else if (GLFont.isValidChar(c)) {
					w += 10;
				}
			}
			Box b = new Box(x-10, y-10, endw+20, h+20, ResLoader.Sheets.GUI_BORDER_NORMAL.ordinal());
			b.render();
			GLFont.render(x, y, GLFont.ALIGN_LEFT, infoText, 10);
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean showsInfoText() {
		return showInfo;
	}

}
