package org.worldOfCube.client.util;

import org.universeengine.opengl.texture.UniTexture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.ARBTextureRectangle.*;

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
