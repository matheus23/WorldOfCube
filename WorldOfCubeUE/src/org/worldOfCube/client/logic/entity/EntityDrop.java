package org.worldOfCube.client.logic.entity;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;

import java.util.Random;

import org.worldOfCube.client.blocks.BlockID;
import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.logic.inventory.Item;
import org.worldOfCube.client.logic.inventory.ItemStack;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.res.Sprite;
import org.worldOfCube.client.util.Distance;
import org.worldOfCube.client.util.Rand;
import org.worldOfCube.client.util.Var;
import org.worldOfCube.client.util.vecmath.Vec;

public class EntityDrop extends Entity {
	
	public static final int PIX_SIZE = 8;
	public static final char LIFE_TIME = 180;
	
	private Item item;
	private EntityPlayer player;
	private char invulTime = 0;

	public EntityDrop(Item item, float x, float y, World world, EntityPlayer p) {
		super(x, y, PIX_SIZE, PIX_SIZE, world);
		this.item = item;
		this.player = p;
		Random r = new Random();
		dx = Rand.rangeFloat(4f, r);
		dy = -(r.nextFloat()*4f);
	}
	
	public EntityDrop(Item item, float x, float y, World world, EntityPlayer p, char invulTime) {
		super(x, y, PIX_SIZE, PIX_SIZE, world);
		this.item = item;
		this.player = p;
		this.invulTime = invulTime;
		Random r = new Random();
		dx = Rand.rangeFloat(4f, r);
		dy = -(r.nextFloat()*4f);
	}
	
	public Item getItem() {
		return item;
	}

	public void tick(double delta) {
		if (time > LIFE_TIME) {
			world.removeEntity(this);
		}
		if (!rect.intersects(world.bounds)) {
			world.removeEntity(this);
		}
		if (time > (double)(int)invulTime) {
			double dist = Distance.get(midx(), midy(), player.midx(), player.midy()); 
			if (dist < 48f) {
				if (dist < 8f) {
					world.getInventory().store(new ItemStack(item, 1));
					world.removeEntity(this);
				}
				Vec v = new Vec(midx(), midy(), player.midx(), player.midy());
				v.normalize();
				v.mul(256.0);
				dx = v.x;
				dy = v.y;
				rect.x += dx*delta;
				rect.y += dy*delta;
			} else {
				dx *= 0.9f; // De-Acceleration
				dy += World.gravity*delta;
				move(dx*delta, dy*delta);
			}
		} else {
			dx *= 0.9f; // De-Acceleration
			dy += World.gravity*delta;
			move(dx*delta, dy*delta);
		}
		afterTick(delta);
	}

	public void render() {
		if (rect.intersects(world.screen)) {
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
