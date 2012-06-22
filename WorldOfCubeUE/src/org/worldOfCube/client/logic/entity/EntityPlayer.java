package org.worldOfCube.client.logic.entity;

import org.lwjgl.input.Keyboard;
import org.worldOfCube.client.input.InputManager;
import org.worldOfCube.client.logic.animation.Bone;
import org.worldOfCube.client.logic.animation.Skeleton;
import org.worldOfCube.client.logic.chunks.World;
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
	private boolean pressedG = false;
	private boolean godmode = false;

	public EntityPlayer(float x, float y, World world, boolean move) {
		super(x, y, 24, 40, world);
		if (move) {
			move(0f, world.totalPix);
		}
		body.setRotation(0f);
		skel.setDebug(false);
	}

	public void tick(double delta) {
		if (Keyboard.isKeyDown(Keyboard.KEY_G) && !pressedG) {
			pressedG = true;
			godmode = !godmode;
		}
		if (!Keyboard.isKeyDown(Keyboard.KEY_G) && pressedG) {
			pressedG = false;
		}
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
			onBottom = !positionEmpty(0f, 1f);
	
			calcGravity(delta);
	
			if (!world.hasKeyboardLock()) {
				processKeyEvents();
			}
	
			move(dx*delta, dy*delta);
		}	
		clipPosition();
		
		skel.tick(rect.x+13f, rect.y+6f+(moving && onBottom ? (float)Math.sin(time) : 0f));
		
		if (skel.getX() > world.getGameMouseX()) {
			dir = LEFT;
		} else {
			dir = RIGHT;
		}
		
		Vec v = new Vec(skel.getX(), skel.getY(), world.getGameMouseX(), world.getGameMouseY());
		double heading = FastMath.atan2Deg(v.x, v.y);
		arm.setRotation((dir == RIGHT ? heading : 360f-heading));
		
		if (onBottom && moving) {
			legFront.setRotation((float)Math.sin(legFrontTime)*60f);
			legBack.setRotation((float)Math.sin(legBackTime)*60f);
			legFrontTime += 0.2f;
			legBackTime += 0.2f;
		} else if (!onBottom) {
			legFront.setRotation(60f);
			legBack.setRotation(-60f);
		} else {
			legFrontTime = 0f;
			legBackTime = (float)Math.PI;
			legFront.setRotation(0f);
			legBack.setRotation(0f);
		}
		afterTick(delta);
	}

	private void calcGravity(double delta) {
		dy += World.gravity*delta;
		if (rect.y > world.totalPix - rect.h) {
			rect.y = world.totalPix - rect.h;
			dy = 0;
			onBottom = true;
		}
	}

	private void clipPosition() {
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

	private void processKeyEvents() {
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
		if (InputManager.down("up") && onBottom) {
			dy = -JUMP_SPEED;
			onBottom = false;
		}
	}

	public void render() {
		skel.render(dir == RIGHT, 
				world.getChunkManager().getLightness(
				(int)(skel.getX()/ResLoader.BLOCK_SIZE), 
				(int)(skel.getY()/ResLoader.BLOCK_SIZE), true));
	}
	
	public boolean colliding() {
		return super.colliding() && !godmode;
	}

}
