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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;

import javax.swing.JTextField;

import org.worldOfCube.client.res.Fonts;

public class ChatInput extends JTextField implements ActionListener, KeyListener {
	private static final long serialVersionUID = 2403950055015350442L;

	private Stack<String> above = new Stack<String>();
	private Stack<String> below = new Stack<String>();
	private ActionListener al;

	public ChatInput() {
		Dimension size = new Dimension(200, 20);
		setSize(size);
		setPreferredSize(size);
		setMinimumSize(size);
		addActionListener(this);
		requestFocusInWindow();
		setFont(Fonts.dejaVuSansMono);
		addKeyListener(this);
	}

	public void setActionListener(ActionListener al) {
		this.al = al;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final String t = getText();
		new Thread(new Runnable() {
			@Override
			public void run() {
				al.actionPerformed(new ActionEvent(this, 0, t));
			}
		}).start();
		above.push(t);
		setText("");
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (!above.isEmpty()) {
				below.push(getText());
				setText(above.pop());
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (!below.isEmpty()) {
				above.push(getText());
				setText(below.pop());
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
