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
	
	public void actionPerformed(ActionEvent e) {
		final String t = getText();
		new Thread(new Runnable() {
			public void run() {
				al.actionPerformed(new ActionEvent(this, 0, t));
			}
		}).start();
		above.push(t);
		setText("");
	}

	public void keyPressed(KeyEvent e) {
	}

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

	public void keyTyped(KeyEvent e) {
	}

}
