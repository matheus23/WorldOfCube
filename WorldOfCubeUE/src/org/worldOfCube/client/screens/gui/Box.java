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
	
	private static final int SIZE = ResLoader.GUI_BORDER_S;
	private static final int HSIZE = SIZE/2;
	
	private Rectangle rect = new Rectangle();
	private byte state = STATE_NONE;
	private float color = COLOR_S_NONE;
	private float alpha = 1f;
	private int borderSpriteSheet;
	
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
	
	public Rectangle getRect() {
		return rect;
	}
	
	public byte getState() {
		return state;
	}
	
	private Sprite getTopLeft() {
		return ResLoader.get(borderSpriteSheet, ResLoader.GUI_BORDER_TL);
	}
	
	private Sprite getTopRight() {
		return ResLoader.get(borderSpriteSheet, ResLoader.GUI_BORDER_TR);
	}
	
	private Sprite getBotLeft() {
		return ResLoader.get(borderSpriteSheet, ResLoader.GUI_BORDER_BL);
	}
	
	private Sprite getBotRight() {
		return ResLoader.get(borderSpriteSheet, ResLoader.GUI_BORDER_BR);
	}
	
	private Sprite getTop() {
		return ResLoader.get(borderSpriteSheet, ResLoader.GUI_BORDER_T);
	}
	
	private Sprite getBot() {
		return ResLoader.get(borderSpriteSheet, ResLoader.GUI_BORDER_B);
	}
	
	private Sprite getLeft() {
		return ResLoader.get(borderSpriteSheet, ResLoader.GUI_BORDER_L);
	}
	
	private Sprite getRight() {
		return ResLoader.get(borderSpriteSheet, ResLoader.GUI_BORDER_R);
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
	
	public void tick(UniDisplay display) {
		int mx = Mouse.getX();
		int my = display.getHeight()-Mouse.getY();
		if (rect.contains(mx, my)) {
			state = STATE_HOVER;
			if (Mouse.isButtonDown(0)) {
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
