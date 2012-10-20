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
package org.worldOfCube.server.gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.worldOfCube.server.ServerHandler;
import org.worldOfCube.server.gui.chat.ChatPane;
import org.worldOfCube.server.gui.viewer.ServerPane;

public class ServerFrame extends JFrame implements WindowListener, ComponentListener {
	private static final long serialVersionUID = -1087934929299860273L;

	public static final String screendir = "server_screens/";

	private ServerHandler sh;

	public ServerFrame() {
		super("WorldOfCube - Server");
		addWindowListener(this);
		addComponentListener(this);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		ChatPane cp = new ChatPane();
		ServerPane sp = new ServerPane();
		sh = new ServerHandler(cp, sp, this);

		Container c = getContentPane();
		c.setLayout(new BorderLayout(10, 10));
		c.add(cp, BorderLayout.CENTER);
		c.add(sp, BorderLayout.PAGE_START);

		pack();
		Dimension size = getSize();
		setMinimumSize(size);
		setLocationRelativeTo(null);

		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					setVisible(true);
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public void screenshot() {
		File screendirectory = new File(screendir);
		if (!screendirectory.exists()) {
			screendirectory.mkdirs();
		}
		String screenname = screendir + "screenshot";
		String format = ".png";
		int i = 0;

		File file = null;

		do {
			file = new File(screenname + i + format);
			i++;
		} while(file.exists());
		try {
			file.createNewFile();
		} catch(IOException e) {
			e.printStackTrace();
		}
		saveScreenshot(file);
	}

	public void saveScreenshot(File file) {
		System.out.println("Saving screenshot in " + file.getAbsolutePath());
		Rectangle window = new Rectangle(getX(), getY(), getWidth(), getHeight());
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		BufferedImage img = robot.createScreenCapture(window);
		try {
			ImageIO.write(img, "PNG", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		sh.stop();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

}
