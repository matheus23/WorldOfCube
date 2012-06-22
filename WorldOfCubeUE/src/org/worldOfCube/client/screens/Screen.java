package org.worldOfCube.client.screens;

import static org.lwjgl.opengl.GL11.*;

import org.universeengine.display.UniDisplay;
import org.universeengine.util.input.UniInputListener;
import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.util.StateManager;

public abstract class Screen implements UniInputListener {
	
	protected UniDisplay display;
	protected ClientMain mep;
	protected float red;
	protected float green;
	protected float blue;
	protected float alpha;
	
	public Screen(UniDisplay display, ClientMain mep, float red, float green, float blue, float alpha) {
		this.display = display;
		this.mep = mep;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
		display.setCaption("WorldOfCube - " + getCaption());
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
	
}
