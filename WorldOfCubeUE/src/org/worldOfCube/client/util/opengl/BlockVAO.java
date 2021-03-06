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
package org.worldOfCube.client.util.opengl;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.worldOfCube.client.res.ResLoader;

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
		tex.put(ResLoader.get(ResLoader.Blocks.EARTH, borderID).getCoord(0, true)).put(ResLoader.get(ResLoader.Blocks.EARTH, borderID).getCoord(0, false));
		tex.put(ResLoader.get(ResLoader.Blocks.EARTH, borderID).getCoord(1, true)).put(ResLoader.get(ResLoader.Blocks.EARTH, borderID).getCoord(1, false));
		tex.put(ResLoader.get(ResLoader.Blocks.EARTH, borderID).getCoord(2, true)).put(ResLoader.get(ResLoader.Blocks.EARTH, borderID).getCoord(2, false));
		tex.put(ResLoader.get(ResLoader.Blocks.EARTH, borderID).getCoord(3, true)).put(ResLoader.get(ResLoader.Blocks.EARTH, borderID).getCoord(3, false));
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
