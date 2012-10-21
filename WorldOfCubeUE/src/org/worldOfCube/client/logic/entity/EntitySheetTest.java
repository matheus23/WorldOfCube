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
package org.worldOfCube.client.logic.entity;

import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.res.Sprite;
import org.worldOfCube.client.res.SpriteSheet;

/**
 * @author matheusdev
 *
 */
public class EntitySheetTest extends Entity {

	protected final SpriteSheet sheet;

	/**
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public EntitySheetTest(SpriteSheet sheet) {
		super(0, 0, 1, 1);
		this.sheet = sheet;
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.logic.entity.Entity#tick(double, org.worldOfCube.client.logic.chunks.World)
	 */
	@Override
	public void tick(double d, World world) {
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.logic.entity.Entity#render(org.worldOfCube.client.logic.chunks.World)
	 */
	@Override
	public void render(World world) {
		int x = (int) rect.x;
		int y = (int) rect.y;
		for (int i = 0; i < sheet.getSpriteNum(); i++) {
			Sprite sprite = sheet.getSprite(i);
			if (sprite != null) {
				sprite.bindAndRender(x, y, 16, 16);
			}
			x += 16;
		}
	}

	@Override
	public void handleMousePosition(int mousex, int mousey, World world) {
		rect.x = world.convertXToWorld(mousex);
		rect.y = world.convertYToWorld(mousey);
	}

}
