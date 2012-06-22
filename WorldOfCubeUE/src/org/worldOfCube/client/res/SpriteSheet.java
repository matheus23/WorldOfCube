package org.worldOfCube.client.res;

import static org.lwjgl.opengl.ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import org.universeengine.opengl.texture.UniTexture;
import org.universeengine.opengl.texture.UniTextureLoader;
import org.universeengine.opengl.texture.UniTextureLoader.DecodePack;
import org.worldOfCube.Log;
import org.worldOfCube.client.util.StateManager;

public class SpriteSheet {
	
	private UniTexture tex;
	private Sprite[] sprites;
	private boolean texRect;
	
	public SpriteSheet(String loadpath, int size, boolean texRect) {
		this.texRect = texRect;
		DecodePack pack = UniTextureLoader.loadImageBufferPNG(
				Thread.currentThread().getContextClassLoader().getResourceAsStream(loadpath));
		
		glEnable(GL_TEXTURE_2D);
		int id = glGenTextures();
		glBindTexture(texRect ? GL_TEXTURE_RECTANGLE_ARB : GL_TEXTURE_2D, id);
		if (texRect) {
			glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_RECTANGLE_ARB, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		} else {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		}
		glTexImage2D(texRect ? GL_TEXTURE_RECTANGLE_ARB : GL_TEXTURE_2D, 0, GL_RGBA, 
				pack.width, pack.height, 
				0, GL_RGBA, GL_UNSIGNED_BYTE, pack.data);
		glBindTexture(texRect ? GL_TEXTURE_RECTANGLE_ARB : GL_TEXTURE_2D, 0);
		
		tex = new UniTexture(id, pack.width, pack.height);
		
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
				Log.err(this, "Invalid values... TexCoords index " + index + " with texture index " + tex.getTexID());
			}
		}
		return sprites[index] = new Sprite(this, u, v, s, t);
	}
	
	public void delete() {
		tex.delete();
	}
	
}
