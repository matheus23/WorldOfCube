package org.worldOfCube.server;

import org.worldOfCube.client.res.Fonts;
import org.worldOfCube.server.gui.ServerFrame;

public class ServerMain {
	
	public static void main(String[] args) {
		Fonts.load();
		new ServerFrame();
	}

}
