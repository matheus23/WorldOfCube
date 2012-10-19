package org.worldOfCube.client.screens.gui;

import static org.lwjgl.opengl.GL11.glColor4f;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.input.InputListener;
import org.worldOfCube.client.input.WrappedMouse;
import org.worldOfCube.client.res.GLFont;
import org.worldOfCube.client.res.ResLoader;

public class BoxInputLabel implements GUIElement, InputListener {

	private Box box;
	private StringBuilder text;
	private int size;
	private float r = 1f;
	private float g = 1f;
	private float b = 1f;
	private boolean selected;
	private List<BoxInputLabelListener> listeners = new ArrayList<BoxInputLabelListener>();
	private long time = 0;
	private int fontalign;
	private int maxchars;
	private String infoText;
	private boolean showInfo;

	public BoxInputLabel(int fontalign, int maxchars, BoxInputLabelListener bill) {
		this(0, 0, 1, 1, 20, ResLoader.GUI_BORDER_NORMAL, fontalign, maxchars, bill);
	}

	public BoxInputLabel(int x, int y, int width, int height, int size, int borderID, int fontalign, int maxchars, BoxInputLabelListener bill) {
		box = new Box(x, y, width, height, borderID);
		text = new StringBuilder();
		this.size = size;
		this.fontalign = fontalign;
		this.maxchars = maxchars;
		addBoxInputLabelListener(bill);
	}

	public void addBoxInputLabelListener(BoxInputLabelListener bill) {
		listeners.add(bill);
	}

	public void removeBoxInputLabelListener(BoxInputLabelListener bill) {
		listeners.remove(bill);
	}

	@Override
	public void tick(UniDisplay display) {
		box.tick(display);
		if (Mouse.isButtonDown(0)) {
			selected = (box.getState() == Box.STATE_CLICKED);
			if (selected) {
				while(Keyboard.next()) {}
			}
		}
		if (selected) {
			box.setState(Box.STATE_HOVER);
			box.recalcColor();
			time++;
		} else {
			time = 0;
		}
		if (infoText != null && box.getRect().contains(WrappedMouse.getX(), WrappedMouse.getY())) {
			showInfo = true;
		} else {
			showInfo = false;
		}
	}

	public Box getBox() {
		return box;
	}

	public StringBuilder getText() {
		return text;
	}

	@Override
	public Rectangle getRect() {
		return getBox().getRect();
	}

	public void setTextColor(float red, float green, float blue) {
		r = red;
		g = green;
		b = blue;
	}

	public void setInfoText(String text) {
		String[] lines = text.split("\n");
		StringBuilder built = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			if (i == 0 || i == 1) {
				built.append(" " + lines[i] + "\n");
			} else {
				built.append(lines[i] + "\n");
			}
		}
		infoText = built.toString();
	}

	@Override
	public void render() {
		box.render();
		glColor4f(r, g, b, 1f);
		GLFont.render(box.getRect().x+box.getRect().width/2,
				box.getRect().y+box.getRect().height/2,
				fontalign, text, size, time % 60 > 29, '_');
	}

	public void renderTwo() {
		if (showInfo) {
			int x = WrappedMouse.getX();
			int y = WrappedMouse.getY();
			int w = 0;
			int endw = 0;
			int h = 0;
			for (int i = 0; i < infoText.length(); i++) {
				char c = infoText.charAt(i);
				if (c == '\n') {
					h += 10;
					endw = Math.max(endw, w);
					w = 0;
				} else if (GLFont.isValidChar(c)) {
					w += 10;
				}
			}
			Box b = new Box(x-10, y-10, endw+20, h+20, ResLoader.GUI_BORDER_NORMAL);
			b.render();
			GLFont.render(x, y, GLFont.ALIGN_LEFT, infoText, 10);
		}
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.input.InputListener#handleKeyEvent(int, char, boolean)
	 */
	@Override
	public void handleKeyEvent(int keyCode, char keyChar, boolean down) {
		if (selected && down) {
			if (keyCode == Keyboard.KEY_RETURN
					&& Keyboard.getEventKeyState() == true) {
				for (int i = 0; i < listeners.size(); i++) {
					listeners.get(i).enterPressed(this);
				}
			} else if (keyCode == Keyboard.KEY_BACK && text.length() > 0) {
				text.deleteCharAt(text.length()-1);
			} else if (text.length() < maxchars) {
				if (GLFont.isValidChar(keyChar)) {
					text.append(keyChar);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.input.InputListener#handleMouseEvent(int, int, int, boolean)
	 */
	@Override
	public void handleMouseEvent(int mousex, int mousey, int button, boolean down) {
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.input.InputListener#handleMousePosition(int, int)
	 */
	@Override
	public void handleMousePosition(int mousex, int mousey) {
	}

}
