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
package org.worldOfCube.client;

import static org.lwjgl.opengl.ARBTextureRectangle.GL_TEXTURE_RECTANGLE_ARB;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glIsEnabled;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Map;

import javax.swing.JOptionPane;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GLContext;
import org.universeengine.UniverseEngineEntryPoint;
import org.universeengine.display.UniAWTDisplay;
import org.universeengine.display.UniLoop;
import org.universeengine.util.UniPrint;
import org.universeengine.util.input.UniInput;
import org.universeengine.util.input.UniInputListener;
import org.worldOfCube.Log;
import org.worldOfCube.client.input.WrappedMouse;
import org.worldOfCube.client.res.GLFont;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.screens.Screen;
import org.worldOfCube.client.screens.ScreenMenu;
import org.worldOfCube.client.util.Config;
import org.worldOfCube.client.util.DisplayModeManager;
import org.worldOfCube.client.util.StateManager;
import org.worldOfCube.client.util.TimeUtil;
import org.worldOfCube.client.util.debug.PerfMonitor;

public class ClientMain implements UniverseEngineEntryPoint, UniInputListener {

	public static final String screendir = "screens";

	public static final float BG_R = 0.57f;
	public static final float BG_G = 0.73f;
	public static final float BG_B = 1f;

	public final boolean limitFPS = false;
	public boolean vsync = Config.get("vsync").equals("on");

	private UniAWTDisplay display;
	private UniLoop loop;
	private UniInput in;
	private Screen screen;
	private boolean fullscreen;

	public ClientMain() {
		display = new UniAWTDisplay(800, 600, "World of Cube");
		loop = new UniLoop(this, display);
		loop.start();
	}

	@Override
	public void keyPressed(int key) {
		if (key == Keyboard.KEY_F2) {
			long time = TimeUtil.ms();
			Log.out("Starting to create screenshot.");
			File screendirectory = new File(screendir);
			if (!screendirectory.exists()) {
				screendirectory.mkdir();
			}
			String screenname = screendir + "/screenshot";
			String imageformat = ".png";
			String screenpath = null;
			int i = 0;
			File f = new File(screenname + i + imageformat);

			do {
				f = new File(screenname + i + imageformat);
				screenpath = screenname + i + imageformat;
				i++;
			} while(f.exists());
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.out("Saving screenshot in " + screenpath);
			loop.saveScreenshot(screenpath);
			Log.out("Screenshot saving in progress... (" + (TimeUtil.ms()-time) + " ms)");
		}
		if (key == Keyboard.KEY_F11) {
			fullscreen = !fullscreen;
			setFullscreen(fullscreen);
		}
	}

	@Override
	public void keyReleased(int key) {
		if (key == Keyboard.KEY_H) {
			Log.out("Memory used: " + memoryInMB() + "." + (memoryInKB()%1000) +
					" MB (in bytes: " + memoryInBytes() + ")");
		}
	}

	private long memoryInMB() {
		return memoryInKB()/1024;
	}

	private long memoryInKB() {
		return (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024;
	}

	private long memoryInBytes() {
		return (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
	}

	@Override
	public void start() {
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

		// Check, whether to use GL_TEXTURE_2D or GL_TEXTURE_RECTANGLE_ARB
		if (!GLContext.getCapabilities().GL_ARB_texture_rectangle) {
			System.err.println("WARING: GL_ARB_texture_rectangle is a missing extension. Using GL_TEXTURE_2D now.");
			StateManager.useTexRect(false);
		} else {
			StateManager.useTexRect(true);
		}
		Log.out("GL_TEXTURE_2D enabled: " + glIsEnabled(GL_TEXTURE_2D));
		Log.out("GL_TEXTURE_RECTANGLE_ARB enabled: " + glIsEnabled(GL_TEXTURE_RECTANGLE_ARB));
		// Load all resources.
		ResLoader.load();
		GLFont.load();

		display.centerOnDefaultDisplay();
		display.getFrame().setMinimumSize(new Dimension(640, 480));
		display.setVisible(true);

		try {
			Mouse.setNativeCursor(new Cursor(16, 16, 0, 0, 1, BufferUtils.createIntBuffer(16*16), BufferUtils.createIntBuffer(1)));
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		Display.setVSyncEnabled(vsync);
		loop.setDelay(limitFPS);
		UniPrint.enabled = false;
		WrappedMouse.giveDisplay(display);

		in = new UniInput(this);
		screen = new ScreenMenu(display, this);
		setupViewport(display.getWidth(), display.getHeight());
		glClearColor(BG_R, BG_G, BG_B, 0f);
		glClearDepth(1f);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

		glEnable(GL_CULL_FACE);
		glCullFace(GL_FRONT);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glDisable(GL_DEPTH_TEST);

		glEnable(GL_TEXTURE_RECTANGLE_ARB);
		glEnable(GL_TEXTURE_2D);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
	}

	@Override
	public void tick() {
		PerfMonitor.startProfile("TICK");
		in.update();
		screen.handleEvents();
		screen.tick();
		PerfMonitor.stopProfile("TICK");
	}

	@Override
	public void render() {
		PerfMonitor.startProfile("RENDER");
		screen.render();
	}

	@Override
	public void displayUpdate() {
		display.update();
		PerfMonitor.stopProfile("RENDER");
	}

	public void setScreen(Screen newscreen) {
		if (screen != null) {
			screen.screenRemove();
		}
		newscreen.screenPopsUp();
		this.screen = newscreen;
	}

	public void setFullscreen(boolean fullscreen) {
		DisplayModeManager.inst().setDisplayMode(1280, 1024, fullscreen);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void onResize(int oldWidth, int oldHeight, int newWidth,
			int newHeight) {
		setupViewport(newWidth, newHeight);
		screen.resize(newWidth, newHeight);
	}

	@Override
	public void end() {
		ResLoader.unload();
	}

	@Override
	public boolean isCloseRequested() {
		return false;
	}

	public void setupViewport(int width, int height) {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glViewport(0, 0, width, height);
		glOrtho(0, width, height, 0, -1, 1);
		glMatrixMode(GL_MODELVIEW);
	}

	public UniLoop getLoop() {
		return loop;
	}

	public void setVSync(boolean on) {
		vsync = on;
		Display.setVSyncEnabled(vsync);
	}

	public static void main(String[] args) {
		// Before this gets executed, outer librarys and classes are loaded.
		// So in case anything fails while loading libs or classes, this won't be executed.
		System.out.println("Fine."); // To tell the Updater, that everything started as it should.
		Log.out("LWJGL Version: " + Sys.getVersion());
		boolean verbose = false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("--verbose") ||
					args[i].equals("-v") ||
					args[i].equals("--debug") ||
					args[i].equals("-d")) {
				verbose = true;
			}
		}
		if (verbose) {
			StringBuilder sb = new StringBuilder("System Properties:\n");

			for (Map.Entry<Object, Object> e : System.getProperties().entrySet()) {
				sb.append(e.getKey() + " : " + e.getValue() + "\n");
			}

			Log.out(sb.toString());
		}
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable e) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(baos);

				e.printStackTrace(ps);

				String msg = baos.toString();
				ps.close();

				System.err.println(msg);

				JOptionPane.showMessageDialog(null,
						"Exception occured in Thread " + thread.getName() + ":\n" + msg,
						"WorldOfCube: Exception occured.", JOptionPane.ERROR_MESSAGE);
			}
		});
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				Mouse.setGrabbed(false);
				Config.save();
			}
		}));
		new ClientMain();
	}

}
