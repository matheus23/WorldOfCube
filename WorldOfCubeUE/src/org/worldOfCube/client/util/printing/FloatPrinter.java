package org.worldOfCube.client.util.printing;

import java.io.PrintStream;

/**
 * @author matheusdev
 *
 */
public class FloatPrinter {

	protected int width;
	protected PrintStream out;

	public FloatPrinter(int width) {
		out = System.out;
		changeOptions(width);
	}

	public void changeOptions(int width) {
		this.width = width;
	}

	public void setPrintStream(PrintStream out) {
		this.out = out;
	}

	public void print(float f) {
		out.printf("% 8f ", f);

		int barWidth = Math.round(getValidWidthFor(f));
		for (int i = 0; i < width; i++) {
			if (i < barWidth) {
				out.print("#");
			} else {
				out.print("-");
			}
		}
		out.println();
	}

	protected float getValidWidthFor(float f) {
		return f * width;
	}

}
