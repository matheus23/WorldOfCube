package org.worldOfCube.client.screens;

import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.screens.gui.BoxLabel;
import org.worldOfCube.client.screens.gui.Loadbar;

public class ScreenLoading extends Screen {
	
	private Loadable todo;
	private BoxLabel label;
	private Loadbar loadbar;
	private boolean drawBackground;

	public ScreenLoading(UniDisplay display, ClientMain mep, boolean drawBackground, Loadable todo) {
		super(display, mep, 0f, 0f, 0f, 0f);
		this.todo = todo;
		this.drawBackground = drawBackground;
		
		label = new BoxLabel(todo.getTitle());
		label.getBox().setAlpha(0f);
		
		loadbar = new Loadbar(0, 0, 1);
		
		recalcButtons(display.getWidth(), display.getHeight());
		
		new Thread(todo).start();
	}

	public void keyPressed(int key) {
	}

	public void keyReleased(int key) {
	}

	public void tick() {
		float progress = todo.getProgress();
		if (progress >= 100f) {
			todo.nextScreen(display, mep);
		}
		label.setText(todo.getTitle());
		label.tick(display);
		loadbar.tick(display);
		loadbar.setProgress(progress);
	}

	public void render() {
		if (drawBackground) {
			fillStandardBackground();
		}
		label.render();
		loadbar.render();
	}

	public void resize(int neww, int newh) {
		recalcButtons(neww, newh);
	}

	public void screenRemove() {
	}
	
	public void recalcButtons(int w, int h) {
		label.getBox().set((int)(0.3*w), (int)(0.45*h), (int)(0.4*w), (int)(0.1*h));
		loadbar.set((int)(0.5*w), (int)(0.55*h), (int)(0.3*w));
	}
	
	public String getCaption() {
		return "Loading Screen";
	}

}
