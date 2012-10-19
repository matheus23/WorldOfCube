package org.worldOfCube.server.gui.viewer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.blocks.Block;
import org.worldOfCube.client.logic.chunks.Chunk;
import org.worldOfCube.client.logic.chunks.ChunkManager;
import org.worldOfCube.client.logic.chunks.World;

public class WorldViewer extends Canvas implements KeyListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 7500069017332241382L;

	public static final int VIEWSIZE = 3;
	public static final int VIEWCHUNKS = 3;
	public static final int WORLDSIZE = 16;
	public static final int CHUNKSIZE = 64;
	public static int PIXSIZE = VIEWSIZE*CHUNKSIZE;

	private static final Color chunkSelectionColor = new Color(0xc60027);

	private World world;
	private BufferStrategy bs;
	private Graphics2D bg;
	private int chunkx;
	private int chunky;
	private int selx;
	private int sely;

	private ChunkSelectionListener listener;

	public WorldViewer() {
		int pixsize = PIXSIZE*VIEWCHUNKS;
		Dimension size = new Dimension(pixsize, pixsize);
		setSize(size);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);

		setBackground(new Color(ClientMain.BG_R, ClientMain.BG_G, ClientMain.BG_B));

		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		generate();
	}

	public void generate() {
		generate(System.currentTimeMillis());
	}

	public void generate(long seed) {
		// TODO: Use to-implement MultiWorld here!
		// world = new MutliWorld(...);
		if (isDisplayable()) {
			render();
		}
	}

	public void setChunkSelectionListener(ChunkSelectionListener csl) {
		listener = csl;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		render();
	}

	public void render() {
		bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			bs = getBufferStrategy();
		}
		bg = (Graphics2D) bs.getDrawGraphics();

		bg.clearRect(0, 0, getWidth(), getHeight());

		renderChunks(bg);

		bs.show();
	}

	public void renderChunks(Graphics2D g) {
		for (int x = 0; x < VIEWCHUNKS; x++) {
			for (int y = 0; y < VIEWCHUNKS; y++) {
				renderChunk(g, chunkx+x, chunky+y, x, y);
			}
		}
		g.setColor(chunkSelectionColor);
		g.drawRect(selx*PIXSIZE, sely*PIXSIZE,
				PIXSIZE-1, PIXSIZE-1);
	}

	public void renderChunk(Graphics2D g, int chunkx, int chunky, int offsetx, int offsety) {
		Chunk c = world.getChunkManager().getChunk(chunkx, chunky);
		int pixx = offsetx*PIXSIZE;
		int pixy = offsety*PIXSIZE;
		Block b;
		for (int x = 0; x < world.getChunkManager().getChunkSize(); x++) {
			for (int y = 0; y < world.getChunkManager().getChunkSize(); y++) {
				b = c.getLocalBlock((byte)x, (byte)y, true);
				if (b != null) {
					g.setColor(b.getAWTBackgroundColor());
					g.fillRect(pixx+x*VIEWSIZE, pixy+y*VIEWSIZE,
							VIEWSIZE, VIEWSIZE);
				} else {
					b = c.getLocalBlock((byte)x, (byte)y, false);
					if (b != null) {
						g.setColor(b.getAWTBackgroundColor().darker());
						g.fillRect(pixx+x*VIEWSIZE, pixy+y*VIEWSIZE,
								VIEWSIZE, VIEWSIZE);
					}
				}
			}
		}
	}

	public void setPos(int cx, int cy) {
		chunkx = cx;
		chunky = cy;
		chunkx = Math.max(0, chunkx);
		chunky = Math.max(0, chunky);
		chunkx = Math.min(world.getChunkManager().getSize()-VIEWCHUNKS, chunkx);
		chunky = Math.min(world.getChunkManager().getSize()-VIEWCHUNKS, chunky);
	}

	public void stop() {
	}

	public ChunkManager getChunkManager() {
		return world.getChunkManager();
	}

	public Chunk getSelectedChunk() {
		return world.getChunkManager().getChunk(chunkx+selx, chunky+sely);
	}

	private void selectChunk(MouseEvent e) {
		selx = e.getX()/PIXSIZE;
		sely = e.getY()/PIXSIZE;
		selx = Math.max(0, selx);
		sely = Math.max(0, sely);
		selx = Math.min(VIEWCHUNKS-1, selx);
		sely = Math.min(VIEWCHUNKS-1, sely);
		render();
		if (listener != null) {
			listener.selectionChanged(getSelectedChunk());
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_LEFT: setPos(chunkx-1, chunky); break;
		case KeyEvent.VK_RIGHT: setPos(chunkx+1, chunky); break;
		case KeyEvent.VK_UP: setPos(chunkx, chunky-1); break;
		case KeyEvent.VK_DOWN: setPos(chunkx, chunky+1); break;
		}
		render();
		if (listener != null) {
			listener.selectionChanged(getSelectedChunk());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		selectChunk(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		selectChunk(e);
	}

}
