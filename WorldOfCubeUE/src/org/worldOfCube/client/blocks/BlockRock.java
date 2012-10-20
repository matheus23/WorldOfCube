/*
 * Copyright (c) 2012 matheusdev
 *
 *import java.awt.Color;

import org.worldOfCube.client.logic.chunks.Chunk;
import org.worldOfCube.client.res.ResLoader;
ntation files (the "Software"), to
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
package org.worldOfCube.client.blocks;

import java.awt.Color;

import org.worldOfCube.client.logic.chunks.Chunk;
import org.worldOfCube.client.res.ResLoader;

public class BlockRock extends Block {

	public static final int BLOCK_TEX = ResLoader.BLOCK_ROCK;
	private static final Color awtCol = new Color(0x595d66);

	public BlockRock(byte x, byte y, Chunk c, boolean foreground) {
		super(x, y, c, foreground);
	}

	public BlockRock(boolean foreground) {
		super(foreground);
	}

	@Override
	public void init() {
	}

	@Override
	public void render() {
		super.renderIntern(ResLoader.get(BLOCK_TEX, borderID));
	}

	@Override
	public void renderBackground() {
		super.renderBackgroundIntern(ResLoader.get(BLOCK_TEX, borderID));
	}

	@Override
	public boolean isValidNeighbor(int x, int y) {
		return c.getBlock(x, y, foreground) instanceof BlockRock;
	}

	@Override
	public Color getAWTBackgroundColor() {
		return awtCol;
	}

	public char getBlockID() {
		return 3;
	}

}
