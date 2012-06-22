package org.worldOfCube.server;

public interface Command {
	
	public String getCommand();
	
	public int getArguments();
	
	public void handleCommand(String[] str);

}
