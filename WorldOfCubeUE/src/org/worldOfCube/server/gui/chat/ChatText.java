package org.worldOfCube.server.gui.chat;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.worldOfCube.client.res.Fonts;

public class ChatText extends JScrollPane {
	private static final long serialVersionUID = -7593125994965562495L;
	
	private JTextArea tarea;
	
	public ChatText() {
		Dimension minsize = new Dimension(200, 150);
		setPreferredSize(minsize);
		setMinimumSize(minsize);
		tarea = new JTextArea();
		tarea.setEditable(false);
		tarea.setCursor(null);
		tarea.setOpaque(true);
		tarea.setFocusable(false);
		tarea.setWrapStyleWord(true);
		tarea.setLineWrap(true);
		tarea.setFont(Fonts.dejaVuSansMono);
		tarea.setCaretColor(Color.BLACK);
		setViewportView(tarea);
	}
	
	public void append(String str) {
		final String add = str;
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					tarea.append(add);
					tarea.setCaretPosition(tarea.getDocument().getLength());
				}
			});
		} else {
			tarea.append(add);
			tarea.setCaretPosition(tarea.getDocument().getLength());
		}
	}

}
