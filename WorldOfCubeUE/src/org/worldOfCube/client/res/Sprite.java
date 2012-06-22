package org.worldOfCube.client.res;

import static org.lwjgl.opengl.GL11.*;

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
