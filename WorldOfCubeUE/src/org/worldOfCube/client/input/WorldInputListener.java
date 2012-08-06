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
