package org.worldOfCube.client.logic.entity.components;

import org.worldOfCube.client.logic.inventory.Inventory;
import org.worldOfCube.client.logic.inventory.Item;
import org.worldOfCube.client.logic.inventory.ItemStack;

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

	public boolean add(Item item) {
		ItemStack stack = new ItemStack(item, 1);
		stack = inv.getSelector().store(stack);
		stack = inv.getStorage().store(stack);
		return stack == null;
	}

}
