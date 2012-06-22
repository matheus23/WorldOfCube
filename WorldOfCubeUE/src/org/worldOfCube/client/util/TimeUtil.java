package org.worldOfCube.client.util;

import org.lwjgl.Sys;

public class TimeUtil {
	
	public static long ms() {
		return ((Sys.getTime()*1000)/Sys.getTimerResolution());
	}

}
