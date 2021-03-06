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
package org.worldOfCube.client.screens;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.input.InputListener;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.util.StateManager;

public abstract class Screen implements InputListener {

	protected UniDisplay display;
	protected ClientMain mep;
	protected float red;
	protected float green;
	protected float blue;
	protected float alpha;
	private long lastTime;

	public Screen(UniDisplay display, ClientMain mep, float red, float green, float blue, float alpha) {
		this.display = display;
		this.mep = mep;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
		display.setCaption("WorldOfCube - " + getCaption());
		lastTime = Sys.getTime();
	}

	public abstract void tick();

	public abstract void render();

	public abstract void resize(int neww, int newh);

	public abstract void screenRemove();

	public abstract String getCaption();

	public void clearScreen() {
		glClearColor(red, green, blue, alpha);
		glClear(GL_COLOR_BUFFER_BIT);
		glLoadIdentity();
	}

	public void screenPopsUp() {
		display.setCaption("WorldOfCube - " + getCaption());
	}

	public void fillStandardBackground() {
		boolean save = StateManager.isUsingTexRect();
		StateManager.useTexRect(false);
		StateManager.bindTexture(ResLoader.guiBackground);
		int w = display.getWidth()  + (display.getWidth()  % ResLoader.guiBackground.getWidth());
		int h = display.getHeight() + (display.getHeight() % ResLoader.guiBackground.getHeight());
		int s = w/ResLoader.guiBackground.getWidth();
		int t = h/ResLoader.guiBackground.getHeight();
		glColor3f(1f, 1f, 1f);
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0f, 0f);
			glVertex2f(0f, 0f);

			glTexCoord2f(s, 0f);
			glVertex2f(w, 0f);

			glTexCoord2f(s, t);
			glVertex2f(w, h);

			glTexCoord2f(0f, t);
			glVertex2f(0f, h);
		}
		glEnd();
		StateManager.useTexRect(save);
	}

	public void renderCursor() {
		int mx = Mouse.getX();
		int my = display.getHeight()-Mouse.getY();

		StateManager.bindTexture(null);
		glColor3f(1f, 1f, 1f);
		glBegin(GL_TRIANGLES);
		{
			glColor3f(1f, 0.5f, 0f);
			glVertex2f(mx, my);
			glColor3f(0.7f, 0f, 0f);
			glVertex2f(mx + 10, my + 10);
			glColor3f(0.5f, 0.5f, 0.5f);
			glVertex2f(mx, my + 15);
		}
		glEnd();
	}

	public void handleEvents() {
		while (Keyboard.next()) {
			handleKeyEvent(
					Keyboard.getEventKey(),
					Keyboard.getEventCharacter(),
					Keyboard.getEventKeyState());
		}
		while (Mouse.next()) {
			handleMouseEvent(
					Mouse.getEventX(),
					// Wrap the mouse position on the y axis, because in our game
					// we have the origin at the top-left corner.
					// But LWJGL assumes the Origin is at the bottom-left corner.
					(display.getHeight()-1)-Mouse.getEventY(),
					Mouse.getEventButton(),
					Mouse.getEventButtonState());
		}
		handleMousePosition(Mouse.getX(), (display.getHeight()-1) - Mouse.getY());
	}

	@Override
	public abstract void handleKeyEvent(int keyCode, char keyChar, boolean down);

	@Override
	public abstract void handleMouseEvent(int mousex, int mousey, int button, boolean down);

	@Override
	public abstract void handleMousePosition(int mousex, int mousey);

	public double getDelta() {
		long time = Sys.getTime();
		double delta = (double) (time - lastTime) / Sys.getTimerResolution();
		lastTime = time;
		return delta;
	}

}
