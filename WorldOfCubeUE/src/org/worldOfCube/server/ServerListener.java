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

	@Override
	public void connected(Connection c) {
		sh.println(this, String.format("%s connected.", c.getRemoteAddressTCP().getAddress()));
	}

	@Override
	public void disconnected(Connection c) {
		sh.println(this, String.format("%s disconnected.", c.getRemoteAddressTCP().getAddress()));
	}

	@Override
	public void received(Connection c, Object o) {
	}

	@Override
	public String getPrefix() {
		return "SERVER";
	}

}
