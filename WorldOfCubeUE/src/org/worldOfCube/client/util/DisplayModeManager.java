package org.worldOfCube.client.util;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.worldOfCube.Log;

public class DisplayModeManager {

	private static DisplayModeManager instance;

	public static DisplayModeManager inst() {
		if (instance == null) {
			return instance = new DisplayModeManager();
		}
		return instance;
	}

	public void setDisplayMode(int width, int height, boolean fullscreen) {

		// return if requested DisplayMode is already set
		if ((Display.getDisplayMode().getWidth() == width)
				&& (Display.getDisplayMode().getHeight() == height)
				&& (Display.isFullscreen() == fullscreen)) {
			return;
		}

		try {
			DisplayMode targetDisplayMode = null;

			if (fullscreen) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;

				for (int i = 0; i < modes.length; i++) {
					DisplayMode current = modes[i];

					if ((current.getWidth() == width) && (current.getHeight() == height)) {
						if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
							if ((targetDisplayMode == null)
									|| (current.getBitsPerPixel() > targetDisplayMode
											.getBitsPerPixel())) {
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}

						// if we've found a match for bpp and frequence against
						// the
						// original display mode then it's probably best to go
						// for this one
						// since it's most likely compatible with the monitor
						if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode()
								.getBitsPerPixel())
								&& (current.getFrequency() == Display.getDesktopDisplayMode()
										.getFrequency())) {
							targetDisplayMode = current;
							break;
						}
					}
				}
			} else {
				targetDisplayMode = new DisplayMode(width, height);
			}

			if (targetDisplayMode == null) {
				Log.err(this, "Failed to find value mode: " + width + "x" + height + " fs="
						+ fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);

		} catch (LWJGLException e) {
			Log.err(this, "Unable to setup mode " + width + "x" + height + " fullscreen="
					+ fullscreen + e);
		}
	}
	
	public static void main(String[] args) {
		DisplayMode[] modes = null;
		try {
			modes = Display.getAvailableDisplayModes();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < modes.length; i++) {
			System.out.println("Found Display mode (" + i + "): w:" + modes[i].getWidth() + " h:" + modes[i].getHeight() + " bpp:" + modes[i].getBitsPerPixel() + ", " + modes[i].getFrequency() + " Hz.");
		}
		System.out.println();
		GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		for (int j = 0; j < devices.length; j++) {
			System.out.println("Awt-DisplayModes for GraphicsDevice (" + j + ")");
			java.awt.DisplayMode[] awtModes = devices[j].getDisplayModes();
			for (int i = 0; i < modes.length; i++) {
				System.out.println("Found Awt-Display mode (" + i + "): w:" + awtModes[i].getWidth() + " h:" + awtModes[i].getHeight() + " bpp:" + awtModes[i].getBitDepth() + ", " + awtModes[i].getRefreshRate() + " Hz.");
			}
		}
	}

}
