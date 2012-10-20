package org.worldOfCube.client.logic.chunks.generation;

import java.util.Random;

import org.matheusdev.interpolation.FloatInterpolation;
import org.matheusdev.interpolation.FloatInterpolationCubicSpline;
import org.matheusdev.noises.noise1.SimplexNoise1;
import org.matheusdev.noises.noise1.SimplexNoiseLayer1;
import org.worldOfCube.Log;
import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.logic.chunks.generation.cave.CaveGenerator;
import org.worldOfCube.client.logic.chunks.generation.trees.FineRootGenerator;
import org.worldOfCube.client.logic.chunks.generation.trees.FineTreeGenerator;
import org.worldOfCube.client.logic.chunks.generation.trees.LittleRootGenerator;
import org.worldOfCube.client.logic.chunks.generation.trees.LittleTreeGenerator;
import org.worldOfCube.client.logic.chunks.generation.trees.Tree;
import org.worldOfCube.client.logic.chunks.generation.trees.TreeGenerator;
import org.worldOfCube.client.util.Rand;

public class Generator {

	protected static enum Layer {
		SURFACE,
		TREE_DENSITY,
	}

	protected static enum Side {
		LEFT(270, true, false, false), TOP(180, false, false, false), RIGHT(90, true, true, false), BOTTOM(0, false, false, true);

		public final int degree;
		protected final boolean swit;
		protected final boolean invX;
		protected final boolean invY;

		Side(int degree, boolean swit, boolean invX, boolean invY) {
			this.degree = degree;
			this.swit = swit;
			this.invX = invX;
			this.invY = invY;
		}

		public Side toRight() {
			return values()[(ordinal()+1)%4];
		}
		public Side toLeft() {
			return values()[(ordinal()+3)%4];
		}
		public int unpackX(int x, int y, int invVal) {
			if (swit) {
				return invX ? invVal - y : y;
			} else {
				return invX ? invVal - x : x;
			}
		}
		public int unpackY(int x, int y, int invVal) {
			if (swit) {
				return invY ? invVal - x : x;
			} else {
				return invY ? invVal - y : y;
			}
		}
	}

	protected final float[][][] vals;
	protected final boolean[][] cave;
	protected final int size;
	protected final Random rand;
	protected final World world;
	protected final TreeGenerator bigTreeGen;
	protected final TreeGenerator bigRootGen;
	protected final TreeGenerator littleTreeGen;
	protected final TreeGenerator littleRootGen;

	/**
	 * Used to Generate a world.
	 * This Method does NOT generate blocks on the World/ChunkManager/Chunks.
	 * This Method is ONLY for generating the Data, used to create Blocks
	 * in the World, which means: the PerlinNoise values, and the
	 * CaveGenerators.
	 * The Methods to create Block onto Chunks is ChunkManager.create(Generator, World).
	 * @param minchange see PerlinNoise Constructor.
	 * @param smoothness see PerlinNoise Constructor.
	 * @param rand an instance of Random for generating Random values.
	 * @param world the instance of world to be randomized.
	 * @see org.matheusdev.noises.ValueNoise#PerlinNoise(int, float, float, float, float, Random)
	 * @see org.worldOfCube.client.logic.chunks.ChunkManager#create(Generator, World)
	 */
	public Generator(float minchange, float smoothness, Random rand, World world) {
		this.rand = rand;
		this.world = world;
		bigTreeGen = new FineTreeGenerator(rand, world);
		bigRootGen = new FineRootGenerator(rand, world);

		littleTreeGen = new LittleTreeGenerator(rand, world);
		littleRootGen = new LittleRootGenerator(rand, world);

		size = world.totalBlocks;
		Log.out("Creating Generator for size " + size + " (wsize: " + world.getChunkManager().getSize() + ", csize: " + world.getChunkManager().getChunkSize() + ")");

		vals = new float[Side.values().length][Layer.values().length][];

		/*
		 * Everything following is Planet Generation:
		 */
		final float fsize = size;
		genSimplexNoise(Layer.SURFACE, 5, new NoiseModifier() {
			@Override
			public float modify(float val) {
				return (((val + 1) / 2) * 32f) + (fsize / 8);
			}
		});
		genSmoothSimplexNoise(Layer.TREE_DENSITY, 4, new NoiseModifier() {
			@Override
			public float modify(float val) {
				float newVal = ((val + 1) / 2) * 6f;
				return newVal - (int) newVal;
			}
		});

		/*
		 * Now we generate Caves:
		 */
		cave = new boolean[world.totalBlocks][world.totalBlocks];
		//TODO: CavePathGenerator?
		new CaveGenerator(cave, world.totalBlocks, 3, 0.55f, 0.3f, rand);
	}

	protected void genSimplexNoise(Layer layer, final int octaves, NoiseModifier modifier) {
		int pos = layer.ordinal();
		for (Side side : Side.values()) {
			SimplexNoise1 noise = new SimplexNoise1(size, octaves, rand, new FloatInterpolationCubicSpline());
			if (modifier != null) {

				float[] noiseVals = noise.get();
				vals[side.ordinal()][pos] = new float[noiseVals.length];

				for (int i = 0; i < vals[side.ordinal()][pos].length; i++) {
					vals[side.ordinal()][pos][i] = modifier.modify(noiseVals[i]);
				}
			} else {
				vals[side.ordinal()][pos] = noise.get();
			}
		}
	}

	protected void genSmoothSimplexNoise(Layer layer, final int octaves, NoiseModifier modifier) {
		int pos = layer.ordinal();
		for (Side side : Side.values()) {
			FloatInterpolation interp = new FloatInterpolationCubicSpline();
			SimplexNoise1 noise = new SimplexNoise1(size,
					new SimplexNoiseLayer1[] {
							new SimplexNoiseLayer1(size, 64, rand, interp),
							new SimplexNoiseLayer1(size, 32, rand, interp)
					}, new float[] {
						2, 1
					}, rand);
			if (modifier != null) {

				float[] noiseVals = noise.get();
				vals[side.ordinal()][pos] = new float[noiseVals.length];

				for (int i = 0; i < vals[side.ordinal()][pos].length; i++) {
					vals[side.ordinal()][pos][i] = modifier.modify(noiseVals[i]);
				}
			} else {
				vals[side.ordinal()][pos] = noise.get();
			}
		}
	}

	/**
	 * Calling this will cause trees to be generated
	 * on the world, given in the constructor.
	 * This Method is not included in the constructor,
	 * so "world" can call ChunkManager.create(Generator) first.
	 */
	public void generateTrees() {
		genTrees(world);
	}

	/**
	 * This is a helper Method to create Trees on all sides of
	 * the world (see parameter), using TreeGenerator-s
	 * and genTreesOnSide(World, float[], int).
	 * It also sets the Loading variables for trees in World (world.treesToLoad
	 * and world.treesloaded).
	 * @param world
	 */
	private void genTrees(World world) {
		//TODO: Add load progress stuff again.
		for (Side side : Side.values()) {
			genTreesOnSide(world, side);
		}
	}

	/**
	 * <p>This Method is used to help Generating trees on a specific side of the world.</p>
	 *
	 * <p>What the method does, is finding the x and y value for a random value
	 * index of float[] surface, and then generating a tree with rotation,
	 * set due to argument "side".<p>
	 *
	 * @param world the World to generate trees on.
	 * @param tg the TreeGenerator used for the Trees(Logs).
	 * @param rg the TreeGenerator used for Roots.
	 * @param surface an array, describing the surface of the specific side of the world.
	 * @param side the side of the world. Either Side.TOP, Side.LEFT, Side.BOTTOM, Side.RIGHT
	 */
	public void genTreesOnSide(World world, Side side) {
		float[] surface = vals[side.ordinal()][Layer.SURFACE.ordinal()];
		float[] trees = vals[side.ordinal()][Layer.TREE_DENSITY.ordinal()];

		float diff = 0;
		int lastTree = 0;

		for (int i = 0; i < surface.length; i++) {
			diff = Math.abs(trees[i] - trees[(i+1) % trees.length]);

			if (diff >= 0.5f) {
				int x = side.unpackX(i, (int)surface[i], world.totalBlocks);
				int y = side.unpackY(i, (int)surface[i], world.totalBlocks);

				if (isValid(x, y, -1)) {
					genTree(x, y, side.degree, Math.abs(i - lastTree) > 32, world);

					lastTree = i;
				}
			}
		}
	}

	/**
	 * Generates a tree and builds him automatically with the given arguments.
	 * @see org.worldOfCube.client.logic.chunks.generation.trees.Tree#Tree(int, int, double, double, TreeGenerator, TreeGenerator)
	 * @see org.worldOfCube.client.logic.chunks.generation.trees.Tree#build(org.worldOfCube.client.logic.chunks.ChunkManager, Random)
	 */
	private void genTree(int x, int y, double dir, boolean bigTree, World world) {
		double length = bigTree ? 4.0 + rand.nextDouble()*2 : 3.0 + rand.nextDouble()*2;
		TreeGenerator tg = bigTree ? bigTreeGen : littleTreeGen;
		TreeGenerator rg = bigTree ? bigRootGen : littleRootGen;
		new Tree(x, y, dir+(rand.nextDouble()*40.0)-20.0, length, tg, rg).build(world.getChunkManager(), rand);
	}

	/**
	 * @return the instance of Random, given in the constructor.
	 */
	public Random getRand() {
		return rand;
	}

	/**
	 * @return a random seed, used for PerlinNoise, to generate
	 * in the right height.
	 */
	public int createSeed() {
		int seed = ((int)(size*0.2f)) + Rand.rangeInt(20, rand);
		return seed;
	}

	/**
	 * @param x the world-space x position.
	 * @param y the world-space y position.
	 * @return whether the given position should be generated as Planet, or not.
	 */
	public boolean isValid(int x, int y) {
		return isValid(x, y, 0);
	}

	/**
	 * @param x the world-space x position.
	 * @param y the world-space y position.
	 * @param offset an offset variable, offsetting the calculation.
	 * Big offsets result in a little planet.
	 * @return whether the given position with an offset should be generated as Planet, or not.
	 */
	public boolean isValid(int x, int y, int offset) {
		return isValid(x, y, offset, Side.LEFT)
				&& isValid(x, y, offset, Side.RIGHT)
				&& isValid(x, y, offset, Side.TOP)
				&& isValid(x, y, offset, Side.BOTTOM);
	}

	private boolean isValid(int x, int y, int offset, Side side) {
		switch (side) {
		case TOP:
			return y > (vals[side.ordinal()][Layer.SURFACE.ordinal()][x] + offset);
		case LEFT:
			return x > (vals[side.ordinal()][Layer.SURFACE.ordinal()][y] + offset);
		case RIGHT:
			return (world.totalBlocks - x) > (vals[side.ordinal()][Layer.SURFACE.ordinal()][y] + offset);
		case BOTTOM:
			return (world.totalBlocks - y) > (vals[side.ordinal()][Layer.SURFACE.ordinal()][x] + offset);
		default:
			throw new IllegalArgumentException("Side is not a valid value.");
		}
	}

	public void floatvToIntv(float[] fv, int [] dest) {
		for (int i = 0; i < dest.length; i++) {
			dest[i] = (int)fv[i];
		}
	}

	/**
	 * @param x the world-space x position.
	 * @param y the world-space y position.
	 * @return whether the position should be a cave, or not.
	 */
	public boolean isCaveSet(int x, int y) {
		return cave[x][y];
	}

}
