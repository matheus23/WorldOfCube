package org.worldOfCube.server.gui.viewer;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class ServerPane extends JPanel {
	private static final long serialVersionUID = -7113172033422996755L;
	
	private WorldViewer wv;
	private ChunkOptions co;
	
	public ServerPane() {
		wv = new WorldViewer();
		co = new ChunkOptions(wv);
		wv.setChunkSelectionListener(co.getCOP());
		setLayout(new BorderLayout(5, 5));
		add(wv, BorderLayout.LINE_END);
		add(co, BorderLayout.CENTER);
	}
	
	public WorldViewer getWorldViewer() {
		return wv;
	}
	
	public ChunkOptions getChunkOptions() {
		return co;
	}
	
	public void stop() {
		wv.stop();
	}
	
}
