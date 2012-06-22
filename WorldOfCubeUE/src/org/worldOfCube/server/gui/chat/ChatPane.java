package org.worldOfCube.server.gui.chat;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class ChatPane extends JPanel {
	private static final long serialVersionUID = 7533250777526684643L;
	
	private ChatText ct;
	private ChatInput ci;
	
	public ChatPane() {
		ct = new ChatText();
		ci = new ChatInput();
		setLayout(new BorderLayout());
		add(ct, BorderLayout.CENTER);
		add(ci, BorderLayout.PAGE_END);
	}
	
	public ChatText getChatText() {
		return ct;
	}
	
	public ChatInput getChatInput() {
		return ci;
	}
	
	public void stop() {
	}

}
