package org.worldOfCube.client.screens.gui;

import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.res.ResLoader;

public class BoxOptions extends BoxLabel {
	
	private BoxLabel label;
	private BoxOptionCycle option;
	private float division;
	
	public BoxOptions(String label, BoxOptionCycle option, float division) {
		super(0, 0, 1, 1, "", ResLoader.GUI_BORDER_BLUE);
		this.label = new BoxLabel(label);
		this.label.getBox().setAlpha(0f);
		this.option = option;
		this.division = division;
	}
	
	public void set(int x, int y, int w, int h) {
		getBox().set(x-5, y-5, w+10, h+10);
		int leftw = (int)(w*division);
		int rightx = x+leftw;
		int rightw = w-leftw;
		label.getBox().set(x, y, leftw, h);
		option.getBox().set(rightx, y, rightw, h);
	}
	
	public BoxOptionCycle getOptionBox() {
		return option;
	}
	
	public void render() {
		super.render();
		option.render();
		label.render();
	}
	
	public void tick(UniDisplay display) {
		super.tick(display);
		if (getBox().getState() == Box.STATE_CLICKED) {
			getBox().setState(Box.STATE_HOVER);
		}
		option.tick(display);
		label.tick(display);
		if (option.getBox().getState() == Box.STATE_HOVER
				|| option.getBox().getState() == Box.STATE_CLICKED) {
			getBox().setState(Box.STATE_NONE);
		}
		getBox().recalcColor();
	}
	
	public void renderTwo() {
		if (!option.showsInfoText()) {
			super.renderTwo();
		}
		// This actually doesn't matter, cause label does never have an info text.
		label.renderTwo();
		option.renderTwo();
	}

}
