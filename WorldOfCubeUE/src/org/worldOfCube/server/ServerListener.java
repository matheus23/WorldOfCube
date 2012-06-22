package org.worldOfCube.server;

import java.io.IOException;

import org.worldOfCube.server.net.Registerer;
import org.worldOfCube.server.net.packages.StateMessage;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class ServerListener extends Listener implements Printer {
	
	private ServerHandler sh;
	private Server server;
	
	public ServerListener(ServerHandler sh) {
		this.sh = sh;
	}
	
	public void start(int port) {
		if (server == null) {
			server = new Server();
			Registerer.register(server.getKryo());
			server.start();
			try {
				server.bind(port);
			} catch (IOException e) {
				sh.println(this, "Unable to bind Server on port " + port);
				server.close();
				server.stop();
				server = null;
				return;
			}
			sh.println(this, "Started server on port " + port);
		} else {
			sh.println(this, "Server is already running.");
		}
	}
	
	public void stop() {
		if (server != null) {
			StateMessage sm = new StateMessage();
			sm.type = StateMessage.SHUTDOWN;
			server.sendToAllTCP(sm);
			server.close();
			server.stop();
			server = null;
			sh.println(this, "Stopped server.");
		} else {
			sh.println(this, "No server is currently running.");
		}
	}
	
	public void connected(Connection c) {
		sh.println(this, String.format("%s connected.", c.getRemoteAddressTCP().getAddress()));
	}
	
	public void disconnected(Connection c) {
		sh.println(this, String.format("%s disconnected.", c.getRemoteAddressTCP().getAddress()));
	}
	
	public void received(Connection c, Object o) {
	}
	
	public String getPrefix() {
		return "SERVER";
	}

}
