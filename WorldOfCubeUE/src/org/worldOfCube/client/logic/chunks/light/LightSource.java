package org.worldOfCube.client.logic.chunks.light;


public interface LightSource {
	
	public float lightX();
	public float lightY();
	public int chunkX();
	public int chunkY();
	public RenderedLight getLight();

}
