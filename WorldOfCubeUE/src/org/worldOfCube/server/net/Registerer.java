package org.worldOfCube.server.net;

import org.worldOfCube.server.net.packages.StateMessage;

import com.esotericsoftware.kryo.Kryo;

public final class Registerer {
	
	private Registerer() {}
	
	public static void register(Kryo kryo) {
		kryo.register(StateMessage.class);
	}

}
