package org.worldOfCube.client.logic.entity;

import org.worldOfCube.client.blocks.BlockLightstone;
import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.logic.entity.components.ComponentMovingLight;

/**
 * @author matheusdev
 *
 */
public class EntityMouselight extends Entity {
	
	private ComponentMovingLight light;

	public EntityMouselight(float x, float y, float w, float h) {
		super(x, y, w, h);
		light = new ComponentMovingLight(BlockLightstone.light);
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.logic.entity.Entity#tick(double)
	 */
	@Override
	public void tick(double d, World world) {
		light.tick((int)rect.x, (int)rect.y, world);
	}
	
	/* (non-Javadoc)
	 * @see org.worldOfCube.client.logic.entity.Entity#handleMousePosition(int, int, org.worldOfCube.client.logic.chunks.World)
	 */
	@Override
	public void handleMousePosition(int mousex, int mousey, World world) {
		rect.x = world.convertXPosToWorldPos(mousex);
		rect.y = world.convertXPosToWorldPos(mousey);
	}

	/* (non-Javadoc)
	 * @see org.worldOfCube.client.logic.entity.Entity#render(org.worldOfCube.client.logic.chunks.World)
	 */
	@Override
	public void render(World world) {
	}

}
