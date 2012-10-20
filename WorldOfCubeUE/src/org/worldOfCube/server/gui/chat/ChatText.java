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
				@Override
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
