package org.worldOfCube.client.screens.gui;

import org.worldOfCube.client.res.ResLoader;

public class BoxOnOff extends BoxLabel {
	
	private class OnOffListener implements BoxLabelListener {
		
		private BoxOnOff box;
		
		OnOffListener(BoxOnOff box) {
			this.box = box;
		}
		
		public void boxPressed(BoxLabel bl) {
		}
		
		public void boxReleased(BoxLabel bl) {
			box.on = !box.on;
			box.setText(box.on ? box.onString : box.offString);
		}
	}
	
	private String onString;
	private String offString;
	private boolean on;
	
	public BoxOnOff(String on, String off, boolean isOn) {
		super(0, 0, 1, 1, isOn ? on : off, ResLoader.GUI_BORDER_NORMAL);
		this.onString = on;
		this.offString = off;
		this.on = isOn;
		super.addBoxLabelListener(new OnOffListener(this));
	}
	
	public boolean isOn() {
		return on;
	}

}
