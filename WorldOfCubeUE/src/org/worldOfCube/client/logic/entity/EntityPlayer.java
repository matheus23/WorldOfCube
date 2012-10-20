package org.worldOfCube.client.logic.entity;

import org.lwjgl.input.Keyboard;
import org.worldOfCube.Log;
import org.worldOfCube.client.input.InputManager;
import org.worldOfCube.client.logic.animation.Bone;
import org.worldOfCube.client.logic.animation.Skeleton;
import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.logic.entity.components.ComponentInventory;
import org.worldOfCube.client.logic.inventory.Inventory;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.util.vecmath.FastMath;
import org.worldOfCube.client.util.vecmath.Vec;

public class EntityPlayer extends Entity {

	public static final double JUMP_SPEED = 500.0;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int FLY_SPEED = 8;

	private double speed = 180.0;
	private boolean onBottom;
	private boolean moving;
	private int dir;
	private Bone legFront = new Bone(ResLoader.get(ResLoader.PLAYER_SHEET, ResLoader.LEG_FRONT),
			10f, 4f, 2f, 9f, 14f);
	private Bone legBack = new Bone(ResLoader.get(ResLoader.PLAYER_SHEET, ResLoader.LEG_BACK),
			10f, 6f, 2f, 9f, 14f);
	private Bone toPelvis = new Bone(null,
			10f, 0f, 0f, 0f, 0f, legBack, legFront);
	private Bone body = new Bone(ResLoader.get(ResLoader.PLAYER_SHEET, ResLoader.BODY),
			4f, 7f, 4f, 13f, 18f, toPelvis);
	private Bone arm = new Bone(ResLoader.get(ResLoader.PLAYER_SHEET, ResLoader.ARM),
			11f, 3f, 2f, 7f, 13f);
	private Bone head = new Bone(ResLoader.get(ResLoader.PLAYER_SHEET, ResLoader.HEAD),
			8f, 15f, 25f, 26f, 29f, body, arm);
	private Skeleton skel = new Skeleton(0, 0, head);

	private float legFrontTime = 0f;
	private float legBackTime = (float)Math.PI;
	private boolean godmode = false;
	private final String name;
	private double lastMouseX;
	private double lastMouseY;

	private ComponentInventory compInv;

	public EntityPlayer(double x, double y, String name) {
		super(x, y, 24, 40);
		this.name = name;
		body.setRotation(0f);
		skel.setDebug(false);
		compInv = new ComponentInventory(new Inventory());
	}

	@Override
	public void tick(double delta, World world) {
		if (godmode) {
			dx = 0;
			dy = 0;
			if (InputManager.down("up")) {
				dy = -FLY_SPEED;
			}
			if (InputManager.down("down")) {
				dy = FLY_SPEED;
			}
			if (InputManager.down("left")) {
				dx = -FLY_SPEED;
			}
			if (InputManager.down("right")) {
				dx = FLY_SPEED;
			}
			rect.x += dx;
			rect.y += dy;
		} else {
			moving = false;
			onBottom = !positionEmpty(0f, 1f, world);

			calcGravity(delta, world);

			move(dx*delta, dy*delta, world);
		}
		clipPosition(world);

		skel.tick(rect.x+13f, rect.y+6f+(moving && onBottom ? (float)Math.sin(time) : 0f));

		Vec v = new Vec(skel.getX(), skel.getY(), lastMouseX, lastMouseY);
		double heading = FastMath.atan2Deg(v.x, v.y);
		arm.setRotation((dir == RIGHT ? heading : 360f-heading));

		if (onBottom && moving) {
			legFront.setRotation((float)Math.sin(legFrontTime)*60f);
			legBack.setRotation((float)Math.sin(legBackTime)*60f);
			legFrontTime += delta * 12.0;
			legBackTime += delta * 12.0;
		} else if (!onBottom) {
			legFront.setRotation(60f);
			legBack.setRotation(-60f);
		} else {
			legFrontTime = 0f;
			legBackTime = (float)Math.PI;
			legFront.setRotation(0f);
			legBack.setRotation(0f);
		}
		compInv.tick(delta);
		afterTick(delta);
	}

	public Inventory getInventory() {
		return compInv.getInv();
	}

	public final String getName() {
		return name;
	}

	private void calcGravity(double delta, World world) {
		dy += World.GRAVITY*delta;
		if (rect.y > world.totalPix - rect.h) {
			rect.y = world.totalPix - rect.h;
			dy = 0;
			onBottom = true;
		}
		if (InputManager.down("up") && onBottom) {
			dy = -JUMP_SPEED;
			onBottom = false;
		}
		if (InputManager.down("right")) {
			dx = speed;
			dir = RIGHT;
			moving = true;
		} else if (InputManager.down("left")) {
			dx = -speed;
			dir = LEFT;
			moving = true;
		} else {
			dx = 0f;
		}
	}

	private void clipPosition(World world) {
		if (rect.x < 0) {
			rect.x = 0;
			dx = 0;
		} else if (rect.x > world.totalPix - rect.w) {
			rect.x = world.totalPix - rect.w;
			dx = 0;
			onBottom = true;
		}
		if (rect.y < 0) {
			rect.y = 0;
			dy = 0;
		}
	}

	public boolean collect(EntityDrop drop) {
		return compInv.add(drop.getItem());
	}

	@Override
	public void handleKeyEvent(int keyCode, char keyChar, boolean down, World world) {
		if (keyCode == Keyboard.KEY_G && down) {
			godmode = !godmode;
			Log.out("%s godmode", (godmode ? "Enabling" : "Disabling"));
		}
	}

	@Override
	public void handleMousePosition(int mousex, int mousey, World world) {
		lastMouseX = world.convertXToWorld(mousex);
		lastMouseY = world.convertYToWorld(mousey);
	}

	@Override
	public void render(World world) {
		skel.render(dir == RIGHT,
				world.getChunkManager().getLightness(
						(int)(skel.getX()/ResLoader.BLOCK_SIZE),
						(int)(skel.getY()/ResLoader.BLOCK_SIZE), true));
	}

	@Override
	public boolean colliding(World world) {
		return super.colliding(world) && !godmode;
	}

}
