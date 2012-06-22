package org.worldOfCube.client.screens.gui;

import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.res.ResLoader;

public class BoxOptionCycle extends BoxLabel {
	
	private class CycleListener implements BoxLabelListener {
		private BoxOptionCycle box;
		
		public CycleListener(BoxOptionCycle box) {
			this.box = box;
		}

		public void boxPressed(BoxLabel bl) {
		}

		public void boxReleased(BoxLabel bl) {
			box.current = (box.current+1)%box.options.length;
		}
		
	}
	
	private String[] options;
	private int current;
	
	public BoxOptionCycle(int selected, String... options) {
		super(0, 0, 1, 1, options[selected], ResLoader.GUI_BORDER_NORMAL);
		this.current = selected;
		this.options = options;
		addBoxLabelListener(new CycleListener(this));
	}
	
	public String getSelectedOption() {
		return options[current];
	}
	
	public int getCurrentSelected() {
		return current;
	}
	
	public String[] getPossibleOptions() {
		return options;
	}
	
	public void tick(UniDisplay display) {
		super.tick(display);
		if (!getText().equals(options[current])) {
			setText(options[current]);
		}
	}
	
}
