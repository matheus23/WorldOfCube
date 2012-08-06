package org.worldOfCube.client.logic.entity;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;

import java.util.Random;

import org.worldOfCube.client.blocks.BlockID;
import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.logic.inventory.Item;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.res.Sprite;
import org.worldOfCube.client.util.Rand;
import org.worldOfCube.client.util.Var;

public class EntityDrop extends Entity {
	
	public static final int PIX_SIZE = 8;
	public static final char LIFE_TIME = 180;
	
	private Item item;
	private char invulTime = 0;
	private boolean collectable = false;

	public EntityDrop(Item item, float x, float y) {
		super(x, y, PIX_SIZE, PIX_SIZE);
		this.item = item;
		Random r = new Random();
		dx = Rand.rangeFloat(4f, r);
		dy = -(r.nextFloat()*4f);
	}
	
	public EntityDrop(Item item, float x, float y, char invulTime) {
		super(x, y, PIX_SIZE, PIX_SIZE);
		this.item = item;
		this.invulTime = invulTime;
		Random r = new Random();
		dx = Rand.rangeFloat(4f, r);
		dy = -(r.nextFloat()*4f);
	}
	
	public Item getItem() {
		return item;
	}
	
	public boolean isCollectable() {
		return collectable;
	}

	public void tick(double delta, World world) {
		if (time > LIFE_TIME) {
			world.removeEntity(this);
		}
		if (!rect.intersects(world.getBounds())) {
			world.removeEntity(this);
		}
		if (time > (double)(int)invulTime) {
			collectable = true;
			// TODO: Implement collecting of Drops in "World" with a seperate list for Drops and ComonentDropCollector
		} else {
			dx *= 0.9f; // De-Acceleration
			dy += World.GRAVITY*delta;
			move(dx*delta, dy*delta, world);
		}
		afterTick(delta);
	}

	public void render(World world) {
		if (rect.intersects(world.getViewport())) {
			Var.col1 = world.getChunkManager().getLightness(
					(int)(rect.x/ResLoader.BLOCK_SIZE), 
					(int)(rect.y/ResLoader.BLOCK_SIZE), true);
			Sprite s = BlockID.itemToSprite(item.getID());
			s.bind();
			glColor3f(Var.col1, Var.col1, Var.col1);
			glBegin(GL_QUADS);
			{
				s.glTexCoord(0);
				rect.glVertex(0);
				s.glTexCoord(1);
				rect.glVertex(1);
				s.glTexCoord(2);
				rect.glVertex(2);
				s.glTexCoord(3);
				rect.glVertex(3);
			}
			glEnd();
		}
	}

}
