package org.worldOfCube.client.logic.chunks;

import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import org.universeengine.display.UniDisplay;
import org.worldOfCube.client.ClientMain;
import org.worldOfCube.client.blocks.Block;
import org.worldOfCube.client.logic.chunks.light.LightUpdater;
import org.worldOfCube.client.logic.entity.Entity;
import org.worldOfCube.client.logic.entity.EntityMouselight;
import org.worldOfCube.client.logic.entity.EntityPlayer;
import org.worldOfCube.client.res.GLFont;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.util.Config;

/**
 * <p>SingleWorld is an alias for "Single-player World"</p>
 * <p>This Class is a subclass of {@link World} and tries to build a
 * Single-player version of it. It needs all the OpenGL-Rendering features
 * whereas the (TODO: to-be-implemented) MultiWorld version does not use
 * OpenGL features at all, and is only used for the Server-side.</p>
 *
 * @author matheusdev
 *
 */
public class SingleWorld extends World {

	protected MouseCursor cursor;
	protected Entity watching;
	protected EntityPlayer player;

	/**
	 * @see World#World(int, int, long, String)
	 */
	public SingleWorld(EntityPlayer ep, int numChunks, int chunkSize, long seed, String name, UniDisplay display) {
		super(numChunks, chunkSize, seed, name);
		initialize(ep, display);
	}

	/**
	 * @see World#World(int, int, String)
	 */
	public SingleWorld(EntityPlayer ep, int numChunks, int chunkSize, String name, UniDisplay display) {
		super(numChunks, chunkSize, name);
		initialize(ep, display);
	}

	/**
	 * @see World#World(ChunkManager, String)
	 */
	public SingleWorld(EntityPlayer ep, ChunkManager cManager, String name, UniDisplay display) {
		super(cManager, name);
		initialize(ep, display);
	}

	protected void initialize(EntityPlayer ep, UniDisplay display) {
		// Setup stuff with the given Player Entity, so it is set as player and
		// will be followed by the viewport when moving around the world.
		// See the Javadoc of these methods for more information.
		// But first position the player to the spawn position:
		setToSpawnPosition(ep);
		addEntity(ep);
		setPlayer(ep);
		setWatchingEntity(ep);
		light = new LightUpdater(cManager);
		cursor = new MouseCursor(ep.getName());

		// Calculate width and height of the viewport:
		viewport.w = display.getWidth();
		viewport.h = display.getHeight();

		// TODO: (Mouselight) Remove it, when lightblocks are implemented ;)
		addEntity(new EntityMouselight(0, 0, 1, 1));
	}

	public void tick(double delta, UniDisplay display) {
		super.tick(delta);
		// Calculate width and height of the viewport:
		viewport.w = display.getWidth();
		viewport.h = display.getHeight();

		if (watching != null) { // If there is something to be watched
			// Set the viewport position to follow the "watching" entity.
			viewport.x = Math.max( // Limit to 0 (= lower limit)
					0,
					Math.min( // Limit to totalPix-screen.w (= higher Limit)
							totalPix-viewport.w,
							watching.midx()-(display.getWidth()/2)));
			viewport.y = Math.max( // Limit to 0 (= lower limit)
					0,
					Math.min( // Limit to totalPix-screen.h (= higher Limit)
							totalPix-viewport.h,
							watching.midy()-(display.getHeight()/2)));
		} else {
			// If there is nothing to be watched, then we need to follow another track.
			// We now need to keep sure that upon resizing, whatever was watched before
			// and now is in the middle, will be in the middle after resizing too.
			// That means we need to move the viewport's x and y positions by the
			// Half of the change of the viewport sizes (the "delta")
			double deltax = (viewport.w-display.getWidth());
			double deltay = (viewport.h-display.getHeight());
			// Yes. We do the same clamping again.
			viewport.x = Math.max(
					0,
					Math.min(
							totalPix-viewport.w,
							viewport.x-deltax));
			viewport.y = Math.max(
					0,
					Math.min(
							totalPix-viewport.h,
							viewport.y-deltay));
		}
		// Yeah... Let's add a little bit fun here:
		if (viewport.area() > bounds.area()) {
			throw new RuntimeException("\"!bounds.contains(viewport)\"\n" +
					"Wow! You seen to either have a VERY huge display,\n" +
					"or the world which was created is truly little :)");
		}
		// Now update lights :)
		light.tick(viewport);
	}

	/**
	 * <p>Renders a representation of this World with an <tt>OpenGL</tt> Rendering context
	 * using <tt>LWJGL</tt></p>
	 * <p>This method invokes rendering methods.</p>
	 * <p>The invokation-tree looks like this:</p>
	 * <ul>
	 * <li>{@link World}</li>
	 * <li>{@link ChunkManager}</li>
	 * <li>{@link Chunk}</li>
	 * <li>{@link Block}</li>
	 * </ul>
	 */
	@Override
	public void render() {
		glPushMatrix();
		{
			glTranslatef((float)-viewport.x, (float)-viewport.y, 0f);
			glColor3f(1f, 1f, 1f);

			boolean vaorend = Config.get("block_rendering").equals("vao");
			if (vaorend) {
				glEnableClientState(GL_VERTEX_ARRAY);
				glEnableClientState(GL_TEXTURE_COORD_ARRAY);
				glEnableClientState(GL_COLOR_ARRAY);
			}

			cManager.renderChunks(viewport);

			if (vaorend) {
				glDisableClientState(GL_VERTEX_ARRAY);
				glDisableClientState(GL_TEXTURE_COORD_ARRAY);
				glDisableClientState(GL_COLOR_ARRAY);
			}

			for (int i = 0; i < entitys.size(); i++) {
				entitys.get(i).render(this);
			}
		}
		glPopMatrix();
		// Set the color stuff back to wonderful white:
		glColor3f(1f, 1f, 1f);
		// Render the inventory, if existing:
		if (player != null && player.getInventory() != null) {
			player.getInventory().render();
		}

		if (Config.get("debug").equals("on")) {
			GLFont.render(10f, 30f, GLFont.ALIGN_LEFT, entitys.size() + " entitys.", 10);
			GLFont.render(10f, 40f, GLFont.ALIGN_LEFT,
					String.format(
							"Player position: (%G, %G)",
							player.getRect().x, player.getRect().y),
					10);
		}
	}

	/**
	 * <p>Increases the functionality of {@link World#removeEntity(Entity)}, so no "memory leaks" are created.</p>
	 * @see World#removeEntity(Entity)
	 */
	@Override
	public void removeEntity(Entity e) {
		if (e == player) {
			player = null;
		}
		super.removeEntity(e);
	}

	/**
	 * <p>Sets the {@link EntityPlayer} used as Player to <tt>e</tt></p>
	 * <p>This method includes adding the <tt>Entity</tt> to the other
	 * Data Structures from {@link World}. If you want to remove the player
	 * entirely, use {@link #removeEntity(Entity)} with the Player-Entity as
	 * argument.</p>
	 * @param e the {@link EntityPlayer} to use as Player.
	 * @throws NullPointerException if <tt>e == null</tt>
	 */
	public void setPlayer(EntityPlayer e) {
		if (e == null) throw new NullPointerException("e == null");
		player = e;
	}

	/**
	 * @return the {@link EntityPlayer} set by {@link #setPlayer(EntityPlayer)}
	 */
	public EntityPlayer getPlayer() {
		return player;
	}

	/**
	 * <p>Set the {@link Entity} to be "watched" to <tt>e</tt></p>
	 * <p>The "watched" <tt>Entity</tt> of this World will be followed by the Camera.
	 * The <tt>Entity</tt> will always sit in the middle of the viewport.</p>
	 * <p>If the <tt>Entity</tt> is <tt>null</tt>, then the viewport will not be
	 * changed in the next {@link #tick(double)}.</p>
	 * @param e the {@link Entity} to be watched.
	 */
	public void setWatchingEntity(Entity e) {
		watching = e;
	}

	/**
	 * <p>Returns the {@link Entity} currently watched.</p>
	 * <p>Set the <tt>Entity</tt> to be watched with {@link #setWatchingEntity(Entity)}</p>
	 * <p>Usually the watched Entity is the Player.</p>
	 * @return the {@link Entity} currently watched.
	 */
	public Entity getWatchingEntity() {
		return watching;
	}

	@Override
	public void handleKeyEvent(int keyCode, char keyChar, boolean down) {
		super.handleKeyEvent(keyCode, keyChar, down);
		cursor.handleKeyEvent(keyCode, keyChar, down, this);
	}

	@Override
	public void handleMouseEvent(int mousex, int mousey, int button, boolean down) {
		super.handleMouseEvent(mousex, mousey, button, down);
		cursor.handleMouseEvent(mousex, mousey, button, down, this);
	}

	@Override
	public void handleMousePosition(int mousex, int mousey) {
		super.handleMousePosition(mousex, mousey);
		cursor.handleMousePosition(mousex, mousey, this);
	}

	private void setToSpawnPosition(EntityPlayer ep) {
		ep.getRect().x = totalPix / 2 + ((rand.nextInt() % (64 * ResLoader.BLOCK_SIZE))- (32 * ResLoader.BLOCK_SIZE));;
		ep.move(0, totalPix/2, this);
	}

	public float getClearColorRed() { return ClientMain.BG_R * light.getSunlight().getStrength(); }
	public float getClearColorGreen() { return ClientMain.BG_G * light.getSunlight().getStrength(); }
	public float getClearColorBlue() { return ClientMain.BG_B * light.getSunlight().getStrength(); }

	@Override
	public void destroy() {
		light.destroy();
	}

}
