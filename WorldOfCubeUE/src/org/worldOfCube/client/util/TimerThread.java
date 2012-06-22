package org.worldOfCube.client.util;

public class TimerThread extends Thread {
	
	private long sleepTime;
	private Invokeable invokeable;
	
	public TimerThread(long sleepTime, Invokeable invokeable) {
		this.sleepTime = sleepTime;
		this.invokeable = invokeable;
	}
	
	public void run() {
		setPriority(MIN_PRIORITY);
		try {
			sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		invokeable.invoke();
	}

}
