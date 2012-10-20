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
package org.worldOfCube.client.input;

import org.worldOfCube.client.logic.chunks.World;

/**
 * @author matheusdev
 *
 */
public interface WorldInputListener {

	/**
	 * <p>Handles the given Keyboard Key event.</p>
	 * @param keyCode the <tt>LWJGL</tt> {@link org.lwjgl.input.Keyboard} key Code,
	 * gotten from {@link org.lwjgl.input.Keyboard#getEventKey()}.
	 * @param keyChar the <tt>LWJGL</tt> {@link org.lwjgl.input.Keyboard} key Char,
	 * gotten from {@link org.lwjgl.input.Keyboard#getEventCharacter()}.
	 * @param down whether the key was pressed or released, usuall gotten from
	 * {@link org.lwjgl.input.Keyboard#getEventKeyState()}.
	 */
	public void handleKeyEvent(int keyCode, char keyChar, boolean down, World world);

	/**
	 * <p>Handles the given Mouse Input event.</p>
	 * @param mousex the current Display-Space x position.
	 * @param mousey the current Display-Space y position.
	 * @param button the button
	 * @param down whether the Button was pressed or released.
	 * @param world the world instance calling this method.
	 */
	public void handleMouseEvent(int mousex, int mousey, int button, boolean down, World world);

	/**
	 * <p>Handles the position the mouse is currently at.</p>
	 * <p>The difference to {@link #handleMouseEvent(int, int, int, boolean)} is,
	 * that this method is always called once per frame, even if the user
	 * did not press any mouse button.</p>
	 * @param mousex the current Display-Space x position.
	 * @param mousey the current Display-Space y position.
	 * @param world the world instance calling this method.
	 */
	public void handleMousePosition(int mousex, int mousey, World world);

}
