package org.worldOfCube.client.res;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

public class Fonts {
	
	public static Font dejaVuSansMono;
	
	public static void load() {
		try {
			dejaVuSansMono = 
					Font.createFont(
							Font.PLAIN, 
							Thread.currentThread().getContextClassLoader()
							.getResourceAsStream(ResLoader.res + "fonts/DejaVuSansMono.ttf"))
					.deriveFont(14f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
