package org.worldOfCube.client.logic.chunks.generation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.worldOfCube.Log;
import org.worldOfCube.client.logic.chunks.World;
import org.worldOfCube.client.logic.chunks.generation.cave.CaveGenerator;
import org.worldOfCube.client.logic.chunks.generation.trees.FineRootGenerator;
import org.worldOfCube.client.logic.chunks.generation.trees.FineTreeGenerator;
import org.worldOfCube.client.logic.chunks.generation.trees.LittleRootGenerator;
import org.worldOfCube.client.logic.chunks.generation.trees.LittleTreeGenerator;
import org.worldOfCube.client.logic.chunks.generation.trees.Tree;
import org.worldOfCube.client.logic.chunks.generation.trees.TreeGenerator;
import org.worldOfCube.client.util.Distance;
import org.worldOfCube.client.util.Rand;

public class Generator {
	
	private float[][] left;
	private float[][] right;
	private float[][] top;
	private float[][] bottom;
	private boolean[][] cave;
	private int size;
	private Random rand;
	private World world;
	private TreeGenerator bigTreeGen;
	private TreeGenerator bigRootGen;
	private TreeGenerator littleTreeGen;
	private TreeGenerator littleRootGen;
	
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
	 * @see org.worldOfCube.client.logic.chunks.generation.PerlinNoise#PerlinNoise(int, float, float, float, float, Random)
	 * @see org.worldOfCube.client.logic.chunks.ChunkManager#create(Generator, World)
	 */
	public Generator(float minchange, float smoothness, Random rand, World world) {
		this.rand = rand;
		this.world = world;
		bigTreeGen = new FineTreeGenerator(rand, world);
		bigRootGen = new FineRootGenerator(rand, world);
		
		littleTreeGen = new LittleTreeGenerator(rand, world);
		littleRootGen = new LittleRootGenerator(rand, world);
		
		size = worldSize();
		Log.out(this, "Creating Generator for size " + size + " (wsize: " + world.getChunkManager().size + ", csize: " + world.getChunkManager().csize + ")");
		
		left = new float[2][];
		right = new float[2][];
		top = new float[2][];
		bottom = new float[2][];
		
		/*
		 * Everything following is Planet Generation:
		 */
		PerlinNoise noiseLeft = new PerlinNoise(
				size, getSeed(), getSeed(), minchange, smoothness, rand);
		PerlinNoise noiseRight = new PerlinNoise(
				size, getSeed(), getSeed(), minchange, smoothness, rand);
		PerlinNoise noiseTop = new PerlinNoise(
				size, getSeed(), getSeed(), minchange, smoothness, rand);
		PerlinNoise noiseBottom = new PerlinNoise(
				size, getSeed(), getSeed(), minchange, smoothness, rand);
		
		left[0] = noiseLeft.get();
		right[0] = noiseRight.get();
		top[0] = noiseTop.get();
		bottom[0] = noiseBottom.get();

		/*
		noiseLeft = new PerlinNoise(
				size, getSeed(), getSeed(), minchange, smoothness, rand);
		noiseRight = new PerlinNoise(
				size, getSeed(), getSeed(), minchange, smoothness, rand);
		noiseTop = new PerlinNoise(
				size, getSeed(), getSeed(), minchange, smoothness, rand);
		noiseBottom = new PerlinNoise(
				size, getSeed(), getSeed(), minchange, smoothness, rand);
		
		left[1] = noiseLeft.get();
		right[1] = noiseRight.get();
		top[1] = noiseTop.get();
		bottom[1] = noiseBottom.get();
		*/
		
		/*
		 * Now we generate Caves:
		 */
		cave = new boolean[world.totalBlocks][world.totalBlocks];
//		new CavePathGenerator(cave, world.totalBlocks, world.totalBlocks/2, (int) top[0][world.totalBlocks/2], 1, 3, rand);
		new CaveGenerator(cave, world.totalBlocks, 3, 0.55f, 0.3f, rand);
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
		world.treesToLoad = 4;
		genTreesOnSide(world, top[0], 0);
		world.treesLoaded++;
		genTreesOnSide(world, left[0], 1);
		world.treesLoaded++;
		genTreesOnSide(world, bottom[0], 2);
		world.treesLoaded++;
		genTreesOnSide(world, right[0], 3);
		world.treesLoaded++;
	}
	
	/**
	 * This Method is used to help Generating trees on a specific side of the world,
	 * where argument "side" being set to 
	 * 0 - means top side,
	 * 1 - means left side
	 * 2 - means bottom side
	 * 3 - means right side
	 * .
	 * What the method does, is finding the x and y value for a random value 
	 * index of float[] surface, and then generating a tree with rotation,
	 * set due to argument "side".
	 * 
	 * Throws an IllegalArgumentException if "side" is not 0, 1, 2, or 3.
	 * @param world the World to generate trees on.
	 * @param tg the TreeGenerator used for the Trees(Logs).
	 * @param rg the TreeGenerator used for Roots.
	 * @param surface an array, describing the surface of the specific side of the world.
	 * @param side the side of the world. Either 0, 1, 2, or 3.
	 */
	public void genTreesOnSide(World world, float[] surface, int side) {
		//TODO: Change this mindist... in some way. It's bad this way. Generate it out of the world-size.
		int mindist = 20;
		int repeat = world.totalBlocks/(mindist*6);
		double rotation;
		switch (side) {
		case 0:
			rotation = 180.0;
			break;
		case 1:
			rotation = 270.0;
			break;
		case 2:
			rotation = 0.0;
			break;
		case 3:
			rotation = 90.0;
			break;
		default:
			throw new IllegalArgumentException("\"side\" has invalid value: " + side);
		}
		List<Point> points = new ArrayList<Point>();
		Point p;
		for (int i = 0; i < repeat; i++) {
			do {
				p = getSurfacePosition(surface, side);
			} while(!checkPointDist(points, p, mindist));
			points.add(p);
			genTree(p.x, p.y, rotation, world);
		}
	}
	
	/**
	 * @param points the already accepted list of Points.
	 * @param p the Point to be checked.
	 * @param mindist the minimal distance the Point needs to have to each Point from "points"
	 * @return whether the distance of Point "p" is bigger to each Point from "points", than "mindist".
	 */
	public boolean checkPointDist(List<Point> points, Point p, int mindist) {
		Point pList;
		for (int i = 0; i < points.size(); i++) {
			pList = points.get(i);
			if (Distance.get(pList.x, pList.y, p.x, p.y) <= mindist) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Searches a Point on "surface", until it finds
	 * a Point, which "isValid(int, int, int)". 
	 * @param surface an array, describing the surface of the side "side".
	 * @param side the side of the world, either 0, 1, 2, or 3.
	 * If "side" is none of them, this method returns "null".
	 * @return a Point, which was found. Cycles forever, if no Point can
	 * be found.
	 */
	public Point getSurfacePosition(float[] surface, int side) {
		int x, y;
		do {
			switch(side) {
			case 0:
				x = Math.abs(rand.nextInt()%world.totalBlocks);
				y = (int)surface[x];
				break;
			case 1:
				y = Math.abs(rand.nextInt()%world.totalBlocks);
				x = (int)surface[y];
				break;
			case 2:
				x = Math.abs(rand.nextInt()%world.totalBlocks);
				y = world.totalBlocks-(int)surface[x];
				break;
			case 3:
				y = Math.abs(rand.nextInt()%world.totalBlocks);
				x = world.totalBlocks-(int)surface[y];
				break;
			default:
				return null;
			}
		} while (!isValid(x, y, -1));
		return new Point(x, y);
	}
	
	/**
	 * Generates a tree and builds him automatically with the given arguments.
	 * @see org.worldOfCube.client.logic.chunks.generation.trees.Tree#Tree(int, int, double, double, TreeGenerator, TreeGenerator)
	 * @see org.worldOfCube.client.logic.chunks.generation.trees.Tree#build(org.worldOfCube.client.logic.chunks.ChunkManager, Random)
	 */
	private void genTree(int x, int y, double dir, World world) {
		boolean bigTree = (rand.nextInt()%20) < 5;
		double length = bigTree ? 6.0+rand.nextDouble()*4 : 4.0+rand.nextDouble()*2;
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
	public int getSeed() {
		int seed = ((int)(size*0.2f)) + Rand.rangeInt(20, rand);
		return seed;
	}
	
	/**
	 * @param x the world-space x position.
	 * @param y the world-space y position.
	 * @return whether the given position should be generated as Planet, or not.
	 */
	public boolean isValid(int x, int y) {
		return isLeftValid(x, y, 0)
				&& isRightValid(x, y, 0)
				&& isTopValid(x, y, 0)
				&& isBottomValid(x, y, 0);
	}
	
	/**
	 * @param x the world-space x position.
	 * @param y the world-space y position.
	 * @param offset an offset variable, offsetting the calculation.
	 * Big offsets result in a little planet.
	 * @return whether the given position with an offset should be generated as Planet, or not.
	 */
	public boolean isValid(int x, int y, int offset) {
		return isLeftValid(x, y, offset)
				&& isRightValid(x, y, offset)
				&& isTopValid(x, y, offset)
				&& isBottomValid(x, y, offset);
	}
	
	private boolean isLeftValid(int x, int y, int offset) {
		return x > left[0][y]+offset;
	}
	
	private boolean isRightValid(int x, int y, int offset) {
		return world.totalBlocks-x > right[0][y]+offset;
	}
	
	private boolean isTopValid(int x, int y, int offset) {
		return y > top[0][x]+offset;
	}
	
	private boolean isBottomValid(int x, int y, int offset) {
		return world.totalBlocks-y > bottom[0][x]+offset;
	}
	
	private int worldSize() {
		return world.getChunkManager().size * world.getChunkManager().csize;
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
