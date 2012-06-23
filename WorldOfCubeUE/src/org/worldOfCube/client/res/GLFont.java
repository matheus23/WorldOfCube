package org.worldOfCube.client.res;

import java.util.HashMap;

import org.worldOfCube.client.util.StateManager;

public class GLFont {
	
	public static final int ALIGN_LEFT = 0;
	public static final int CENTER = 1;
	
	private static final int SIZE = 10; 
	
	private static HashMap<Character, Sprite> chars = new HashMap<Character, Sprite>();
	
	public static void load() {
		SpriteSheet img = new SpriteSheet(ResLoader.res + "fonts/font.png", 95, StateManager.isUsingTexRect());
		int i = 0;
		chars.put('a', img.giveSprite(i++,   0,  0, SIZE, SIZE));
		chars.put('b', img.giveSprite(i++,  10,  0, SIZE, SIZE));
		chars.put('c', img.giveSprite(i++,  20,  0, SIZE, SIZE));
		chars.put('d', img.giveSprite(i++,  30,  0, SIZE, SIZE));
		chars.put('e', img.giveSprite(i++,  40,  0, SIZE, SIZE));
		chars.put('f', img.giveSprite(i++,  50,  0, SIZE, SIZE));
		chars.put('g', img.giveSprite(i++,  60,  0, SIZE, SIZE));
		chars.put('h', img.giveSprite(i++,  70,  0, SIZE, SIZE));
		chars.put('i', img.giveSprite(i++,  80,  0, SIZE, SIZE));
		chars.put('j', img.giveSprite(i++,  90,  0, SIZE, SIZE));
		chars.put('k', img.giveSprite(i++, 100,  0, SIZE, SIZE));
		chars.put('l', img.giveSprite(i++, 110,  0, SIZE, SIZE));
		chars.put('m', img.giveSprite(i++, 120,  0, SIZE, SIZE));
		chars.put('n', img.giveSprite(i++, 130,  0, SIZE, SIZE));
		chars.put('o', img.giveSprite(i++, 140,  0, SIZE, SIZE));
		chars.put('p', img.giveSprite(i++,   0, 10, SIZE, SIZE));
		chars.put('q', img.giveSprite(i++,  10, 10, SIZE, SIZE));
		chars.put('r', img.giveSprite(i++,  20, 10, SIZE, SIZE));
		chars.put('s', img.giveSprite(i++,  30, 10, SIZE, SIZE));
		chars.put('t', img.giveSprite(i++,  40, 10, SIZE, SIZE));
		chars.put('u', img.giveSprite(i++,  50, 10, SIZE, SIZE));
		chars.put('v', img.giveSprite(i++,  60, 10, SIZE, SIZE));
		chars.put('w', img.giveSprite(i++,  70, 10, SIZE, SIZE));
		chars.put('x', img.giveSprite(i++,  80, 10, SIZE, SIZE));
		chars.put('y', img.giveSprite(i++,  90, 10, SIZE, SIZE));
		chars.put('z', img.giveSprite(i++, 100, 10, SIZE, SIZE));
		
		chars.put('A', img.giveSprite(i++,   0, 20, SIZE, SIZE));
		chars.put('B', img.giveSprite(i++,  10, 20, SIZE, SIZE));
		chars.put('C', img.giveSprite(i++,  20, 20, SIZE, SIZE));
		chars.put('D', img.giveSprite(i++,  30, 20, SIZE, SIZE));
		chars.put('E', img.giveSprite(i++,  40, 20, SIZE, SIZE));
		chars.put('F', img.giveSprite(i++,  50, 20, SIZE, SIZE));
		chars.put('G', img.giveSprite(i++,  60, 20, SIZE, SIZE));
		chars.put('H', img.giveSprite(i++,  70, 20, SIZE, SIZE));
		chars.put('I', img.giveSprite(i++,  80, 20, SIZE, SIZE));
		chars.put('J', img.giveSprite(i++,  90, 20, SIZE, SIZE));
		chars.put('K', img.giveSprite(i++, 100, 20, SIZE, SIZE));
		chars.put('L', img.giveSprite(i++, 110, 20, SIZE, SIZE));
		chars.put('M', img.giveSprite(i++, 120, 20, SIZE, SIZE));
		chars.put('N', img.giveSprite(i++, 130, 20, SIZE, SIZE));
		chars.put('O', img.giveSprite(i++, 140, 20, SIZE, SIZE));
		chars.put('P', img.giveSprite(i++,   0, 30, SIZE, SIZE));
		chars.put('Q', img.giveSprite(i++,  10, 30, SIZE, SIZE));
		chars.put('R', img.giveSprite(i++,  20, 30, SIZE, SIZE));
		chars.put('S', img.giveSprite(i++,  30, 30, SIZE, SIZE));
		chars.put('T', img.giveSprite(i++,  40, 30, SIZE, SIZE));
		chars.put('U', img.giveSprite(i++,  50, 30, SIZE, SIZE));
		chars.put('V', img.giveSprite(i++,  60, 30, SIZE, SIZE));
		chars.put('W', img.giveSprite(i++,  70, 30, SIZE, SIZE));
		chars.put('X', img.giveSprite(i++,  80, 30, SIZE, SIZE));
		chars.put('Y', img.giveSprite(i++,  90, 30, SIZE, SIZE));
		chars.put('Z', img.giveSprite(i++, 100, 30, SIZE, SIZE));
		
		chars.put('0', img.giveSprite(i++,   0, 40, SIZE, SIZE));
		chars.put('1', img.giveSprite(i++,  10, 40, SIZE, SIZE));
		chars.put('2', img.giveSprite(i++,  20, 40, SIZE, SIZE));
		chars.put('3', img.giveSprite(i++,  30, 40, SIZE, SIZE));
		chars.put('4', img.giveSprite(i++,  40, 40, SIZE, SIZE));
		chars.put('5', img.giveSprite(i++,  50, 40, SIZE, SIZE));
		chars.put('6', img.giveSprite(i++,  60, 40, SIZE, SIZE));
		chars.put('7', img.giveSprite(i++,  70, 40, SIZE, SIZE));
		chars.put('8', img.giveSprite(i++,  80, 40, SIZE, SIZE));
		chars.put('9', img.giveSprite(i++,  90, 40, SIZE, SIZE));
		
		chars.put('ä', img.giveSprite(i++, 100, 40, SIZE, SIZE));
		chars.put('ü', img.giveSprite(i++, 110, 40, SIZE, SIZE));
		chars.put('ö', img.giveSprite(i++, 120, 40, SIZE, SIZE));
		chars.put('ß', img.giveSprite(i++, 130, 40, SIZE, SIZE));
		
		chars.put('+', img.giveSprite(i++,   0, 50, SIZE, SIZE));
		chars.put('-', img.giveSprite(i++,  10, 50, SIZE, SIZE));
		chars.put('*', img.giveSprite(i++,  20, 50, SIZE, SIZE));
		chars.put('/', img.giveSprite(i++,  30, 50, SIZE, SIZE));
		chars.put('=', img.giveSprite(i++,  40, 50, SIZE, SIZE));
		chars.put('#', img.giveSprite(i++,  50, 50, SIZE, SIZE));
		chars.put('{', img.giveSprite(i++,  60, 50, SIZE, SIZE));
		chars.put('}', img.giveSprite(i++,  70, 50, SIZE, SIZE));
		chars.put('[', img.giveSprite(i++,  80, 50, SIZE, SIZE));
		chars.put(']', img.giveSprite(i++,  90, 50, SIZE, SIZE));
		chars.put('(', img.giveSprite(i++, 100, 50, SIZE, SIZE));
		chars.put(')', img.giveSprite(i++, 110, 50, SIZE, SIZE));
		chars.put('<', img.giveSprite(i++, 120, 50, SIZE, SIZE));
		chars.put('>', img.giveSprite(i++, 130, 50, SIZE, SIZE));
		chars.put('&', img.giveSprite(i++, 140, 50, SIZE, SIZE));

		chars.put('.', img.giveSprite(i++,   0, 60, SIZE, SIZE));
		chars.put(',', img.giveSprite(i++,  10, 60, SIZE, SIZE));
		chars.put('!', img.giveSprite(i++,  20, 60, SIZE, SIZE));
		chars.put('?', img.giveSprite(i++,  30, 60, SIZE, SIZE));
		chars.put(':', img.giveSprite(i++,  40, 60, SIZE, SIZE));
		chars.put(';', img.giveSprite(i++,  50, 60, SIZE, SIZE));
		chars.put('_', img.giveSprite(i++,  60, 60, SIZE, SIZE));
		chars.put('\'',img.giveSprite(i++,  70, 60, SIZE, SIZE));
		chars.put('\\',img.giveSprite(i++,  80, 60, SIZE, SIZE));
		chars.put('^', img.giveSprite(i++,  90, 60, SIZE, SIZE));
		chars.put('~', img.giveSprite(i++, 100, 60, SIZE, SIZE));
		chars.put('|', img.giveSprite(i++, 110, 60, SIZE, SIZE));
		chars.put('$', img.giveSprite(i++, 120, 60, SIZE, SIZE));
		chars.put('§', img.giveSprite(i++, 130, 60, SIZE, SIZE));
	}
	
	public static boolean isValidChar(Character c) {
		return chars.get(c) != null || c == ' ';
	}
	
	public static Sprite get(Character c) {
		return chars.get(c);
	}
	
	public static void render(float x, float y, int type, CharSequence chs, float size, boolean append, char appendchar) {
		Sprite sprite;
		if (type == ALIGN_LEFT) {
			Character c;
			float px = x;
			float py = y;
			for (int i = 0; i < chs.length(); i++) {
				c = new Character(chs.charAt(i));
				if (c.equals(' ')) {
					px += size;
					continue;
				} else if (c.equals('\n')) {
					py += size;
					px = x;
					continue;
				}
				sprite = get(c);
				if (sprite != null) {
					sprite.bindAndRender(px, py, size, size);
					px += size;
					continue;
				}
			}
			if (append) {
				sprite = get(appendchar);
				if (sprite != null) {
					sprite.bindAndRender(px, py, size, size);
				}
			}
		} else if (type == CENTER) {
			float dx = x-chs.length()*size/2;
			float dy = y-size/2;
			render(dx, dy, ALIGN_LEFT, chs, size, append, appendchar);
		} else {
			throw new IllegalArgumentException("Wrong value for argument \"type\": " + type);
		}
	}

	public static void render(float x, float y, int type, CharSequence chs, float size) {
		render(x, y, type, chs, size, false, ' ');
	}

}
