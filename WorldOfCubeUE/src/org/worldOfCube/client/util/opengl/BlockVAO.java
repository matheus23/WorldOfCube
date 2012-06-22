package org.worldOfCube.client.util.opengl;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.worldOfCube.client.res.ResLoader;

import static org.lwjgl.opengl.GL11.*;

public class BlockVAO {
	
	private FloatBuffer vert;
	private FloatBuffer tex;
	private FloatBuffer col;
	
	public BlockVAO(int borderID) {
		int bs = ResLoader.BLOCK_SIZE;

		vert = BufferUtils.createFloatBuffer(8);
		vert.order();
		vert.put(0f).put(0f); // Vertex
		vert.put(bs).put(0f); // Vertex
		vert.put(bs).put(bs); // Vertex
		vert.put(0f).put(bs); // Vertex
		vert.flip();
		
		tex = BufferUtils.createFloatBuffer(8);
		tex.order();
		tex.put(ResLoader.get(ResLoader.BLOCK_EARTH, borderID).getCoord(0, true)).put(ResLoader.get(ResLoader.BLOCK_EARTH, borderID).getCoord(0, false));
		tex.put(ResLoader.get(ResLoader.BLOCK_EARTH, borderID).getCoord(1, true)).put(ResLoader.get(ResLoader.BLOCK_EARTH, borderID).getCoord(1, false));
		tex.put(ResLoader.get(ResLoader.BLOCK_EARTH, borderID).getCoord(2, true)).put(ResLoader.get(ResLoader.BLOCK_EARTH, borderID).getCoord(2, false));
		tex.put(ResLoader.get(ResLoader.BLOCK_EARTH, borderID).getCoord(3, true)).put(ResLoader.get(ResLoader.BLOCK_EARTH, borderID).getCoord(3, false));
		tex.flip();
		
		col = BufferUtils.createFloatBuffer(12);
		col.order();
		col.put(1f).put(1f).put(1f); // Color
		col.put(1f).put(1f).put(1f); // Color
		col.put(1f).put(1f).put(1f); // Color
		col.put(1f).put(1f).put(1f); // Color
		col.flip();
	}
	
	public void setColor(float col0, float col1, float col2, float col3) {
		col.rewind();
		col.put(col0).put(col0).put(col0);
		col.put(col1).put(col1).put(col1);
		col.put(col2).put(col2).put(col2);
		col.put(col3).put(col3).put(col3);
		col.flip();
	}
	
	public void render() {
		glVertexPointer(2, 0, vert);
		glTexCoordPointer(2, 0, tex);
		glColorPointer(3, 0, col);
		
		glDrawArrays(GL_QUADS, 0, 28);
	}
	
	public void renderWithoutCol() {
		glVertexPointer(2, 0, vert);
		glTexCoordPointer(2, 0, tex);

		glDrawArrays(GL_QUADS, 0, 28);
	}

}
