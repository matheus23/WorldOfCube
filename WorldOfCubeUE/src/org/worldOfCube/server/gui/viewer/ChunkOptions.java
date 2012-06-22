package org.worldOfCube.server.gui.viewer;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

import org.worldOfCube.server.gui.viewer.chunkoptions.ChunkOptionPane;

public class ChunkOptions extends JTabbedPane {
	private static final long serialVersionUID = -1763158765671151001L;
	
	private WorldViewer wv;
	private ChunkOptionPane cop;
	
	public ChunkOptions(WorldViewer wv) {
		this.wv = wv;
		cop = new ChunkOptionPane(this.wv);
		setBorder(BorderFactory.createTitledBorder("Info"));
		Dimension size = new Dimension(200, 200);
		setSize(size);
		setMinimumSize(size);
		setPreferredSize(size);
		addTab("Chunk", cop);
	}
	
	public ChunkOptionPane getCOP() {
		return cop;
	}

}
