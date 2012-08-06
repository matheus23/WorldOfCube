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
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_QUAD_STRIP;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glIsEnabled;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JOptionPane;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
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
import org.worldOfCube.client.input.InputManager;
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
	
	public void keyPressed(int key) {
		if (key == Keyboard.KEY_F2) {
			long time = TimeUtil.ms();
			Log.out(this, "Starting to create screenshot.");
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
			Log.out(this, "Saving screenshot in " + screenpath);
			loop.saveScreenshot(screenpath);
			Log.out(this, "Screenshot saving in progress... (" + (TimeUtil.ms()-time) + " ms)");
		}
		if (key == Keyboard.KEY_F11) {
			fullscreen = !fullscreen;
			setFullscreen(fullscreen);
		}
		screen.keyPressed(key);
	}
	
	public void keyReleased(int key) {
		if (key == Keyboard.KEY_H) {
			Log.out(this, "Memory used: " + memoryInMB() + "." + (memoryInKB()%1000) + 
					" MB (in bytes: " + memoryInBytes() + ")");
		}
		screen.keyReleased(key);
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

	public void start() {
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		
		// Check, whether to use GL_TEXTURE_2D or GL_TEXTURE_RECTANGLE_ARB 
		if (!GLContext.getCapabilities().GL_ARB_texture_rectangle) {
			System.err.println("WARING: GL_ARB_texture_rectangle is a missing extension. Using GL_TEXTURE_2D now.");
			StateManager.useTexRect(false);
		} else {
			StateManager.useTexRect(true);
		}
		Log.out(this, "GL_TEXTURE_2D enabled: " + glIsEnabled(GL_TEXTURE_2D));
		Log.out(this, "GL_TEXTURE_RECTANGLE_ARB enabled: " + glIsEnabled(GL_TEXTURE_RECTANGLE_ARB));
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
	
	public void tick() {
		PerfMonitor.startProfile("INPUT");
		InputManager.update();
		PerfMonitor.stopProfile("INPUT");
		PerfMonitor.startProfile("TICK");
		in.update();
		screen.tick();
		PerfMonitor.stopProfile("TICK");
	}

	public void render() {
		PerfMonitor.startProfile("RENDER");
		screen.render();
		
		boolean save = StateManager.isUsingTexRect();
		StateManager.useTexRect(false);
		StateManager.bindTexture(ResLoader.cursor);
		StateManager.useTexRect(save);
		
		glBegin(GL_QUADS);
		{
			glVertex2f(WrappedMouse.getX(), WrappedMouse.getY());
			glTexCoord2f(0f, 0f);
			glVertex2f(WrappedMouse.getX()+ResLoader.cursor.getWidth(), WrappedMouse.getY());
			glTexCoord2f(1f, 0f);
			glVertex2f(WrappedMouse.getX()+ResLoader.cursor.getWidth(), WrappedMouse.getY()+ResLoader.cursor.getHeight());
			glTexCoord2f(1f, 1f);
			glVertex2f(WrappedMouse.getX(), WrappedMouse.getY()+ResLoader.cursor.getHeight());
			glTexCoord2f(0f, 1f);
		}
		glEnd();
	}
	
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

	public void pause() {
	}

	public void resume() {
	}

	public void onResize(int oldWidth, int oldHeight, int newWidth,
			int newHeight) {
		setupViewport(newWidth, newHeight);
		screen.resize(newWidth, newHeight);
	}

	public void end() {
		ResLoader.unload();
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
		System.out.println("Fine.");
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
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
			public void run() {
				Mouse.setGrabbed(false);
				Config.save();
			}
		}));
		new ClientMain();
	}

}
