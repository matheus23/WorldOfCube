package org.worldOfCube.client.logic.entity.components;

import org.worldOfCube.client.logic.inventory.Inventory;

/**
 * @author matheusdev
 *
 */
public class ComponentInventory {
	
	protected Inventory inv;
	
	public ComponentInventory(Inventory inv) {
		this.inv = inv;
	}
	
	public Inventory getInv() {
		return inv;
	}
	
	public void tick(double delta) {
		inv.tick();
	}
	
	public void render() {
		inv.render();
	}

}
