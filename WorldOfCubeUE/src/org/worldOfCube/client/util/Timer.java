package org.worldOfCube.client.util;

import org.lwjgl.Sys;

public class Timer {
	
	private long t1;
	
	public Timer() {
	}
	
	public Timer start() {
		t1 = getMS();
		return this;
	}
	
	public long stop() {
		return getMS()-t1;
	}
	
	public static long getMS() {
		return (Sys.getTime()*1000)/Sys.getTimerResolution();
	}

}
