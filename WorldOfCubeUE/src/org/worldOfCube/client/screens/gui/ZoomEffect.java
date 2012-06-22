package org.worldOfCube.client.screens.gui;

import org.universeengine.opengl.texture.UniTexture;

public class ZoomEffect {
	
	private class Zoom {
		public float x, y, w, h;
		
		public void toTexCoords(float totalW, float totalH, Zoom dest) {
			dest.x = x/totalW;
			dest.y = y/totalH;
			dest.w = w/totalW;
			dest.h = h/totalH;
		}
	}
	
	private UniTexture zoomTex;
	private float ratio;
	
	public ZoomEffect(UniTexture texture) {
		this.zoomTex = texture;
		ratio = (float)texture.getWidth()/(float)texture.getHeight();
	}

}
