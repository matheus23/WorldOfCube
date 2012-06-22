package org.worldOfCube.server.gui.viewer.chunkoptions;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.worldOfCube.client.logic.chunks.Chunk;
import org.worldOfCube.server.gui.viewer.ChunkSelectionListener;
import org.worldOfCube.server.gui.viewer.WorldViewer;

public class ChunkOptionPane extends JPanel implements ChunkSelectionListener {
	private static final long serialVersionUID = -1379072286586660466L;
	
	private PositionPane pos;
	private BlockCountPane bcp;
	
	public ChunkOptionPane(WorldViewer wv) {
		Chunk c = wv.getSelectedChunk();
		pos = new PositionPane(c);
		bcp = new BlockCountPane(c);
		setLayout(new GridLayout(0, 1, 5, 5));
		add(pos);
		add(bcp);
	}
	
	public void update(Chunk c) {
		pos.update(c);
		bcp.update(c);
	}
	
	public void selectionChanged(Chunk c) {
		update(c);
	}
	
	private class PositionPane extends JPanel {
		private static final long serialVersionUID = -92671383681680384L;
		
		private JLabel xpos;
		private JLabel ypos;
		
		public PositionPane(Chunk c) {
			Dimension labelsize = new Dimension(80, 20);
			
			setBorder(BorderFactory.createTitledBorder("Chunk's Position x/y:"));
			
			xpos = new JLabel(Integer.toString(c.getX()), JLabel.CENTER);
			ypos = new JLabel(Integer.toString(c.getY()), JLabel.CENTER);
			
			xpos.setPreferredSize(labelsize);
			ypos.setPreferredSize(labelsize);
			
			xpos.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			ypos.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			
			setLayout(new BorderLayout(5, 5));
			add(xpos, BorderLayout.LINE_START);
			add(ypos, BorderLayout.LINE_END);
		}
		
		public void update(Chunk c) {
			xpos.setText(Integer.toString(c.getX()));
			ypos.setText(Integer.toString(c.getY()));
		}
	}
	
	private class BlockCountPane extends JPanel implements ActionListener {
		private static final long serialVersionUID = -1094432783591202885L;
		
		private JLabel count;
		private JButton button;
		private Chunk c;
		
		public BlockCountPane(Chunk c) {
			this.c = c;
			Dimension labelsize = new Dimension(80, 20);

			setBorder(BorderFactory.createTitledBorder("Number of Blocks:"));
			
			count = new JLabel(Integer.toString(countBlocks(c)), JLabel.CENTER);
			count.setPreferredSize(labelsize);
			count.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			
			button = new JButton("recalc");
			button.addActionListener(this);
			
			setLayout(new BorderLayout(5, 5));
			add(count, BorderLayout.LINE_START);
			add(button, BorderLayout.LINE_END);
		}
		
		private int countBlocks(Chunk c) {
			int count = 0;
			for (int x = 0; x < c.getSize(); x++) {
				for (int y = 0; y < c.getSize(); y++) {
					if (c.getLocalBlock(x, y, true) != null) {
						count++;
					}
				}
			}
			return count;
		}
		
		public void update(Chunk c) {
			this.c = c;
			count.setText(Integer.toString(countBlocks(c)));
		}
		
		public void actionPerformed(ActionEvent e) {
			update(c);
		}
		
	}

}
