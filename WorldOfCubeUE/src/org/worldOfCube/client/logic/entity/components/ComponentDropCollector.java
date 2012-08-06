package org.worldOfCube.client.logic.entity.components;

import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.logic.entity.Entity;

/**
 * @author matheusdev
 *
 */
public class ComponentDropCollector {
	
	protected World world;
	protected Entity entity;
	
	public ComponentDropCollector(Entity entity, World world) {
		this.world = world;
		this.entity = entity;
	}
	
	public void tick(double delta) {
	}

}
