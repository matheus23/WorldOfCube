/*
 * Copyright (c) 2012 matheusdev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.worldOfCube.client.blocks;

import org.worldOfCube.client.logic.inventory.Item;
import org.worldOfCube.client.res.ResLoader;
import org.worldOfCube.client.res.Sprite;

public final class BlockID {

	private BlockID() {
		// TODO: Blocks: Add to ID parser.
	}

	/**
	 * Transforms a BlockID to a Block instance, which did not
	 * get added to a Chunk at this point.
	 * Throws a IllegalArgumentException, when the BlockID has
	 * a wrong value.
	 * @param id the BlockID to create the block from.
	 * @param foreground whether the block should be a foreground-block or not.
	 * @return the Block, or null, if the id is 0.
	 */
	public static Block idToBlock(char id, boolean foreground) {
		switch (id) {
		case 0: return null;
		case 1: return new BlockEarth(foreground);
		case 2: return new BlockGrass(foreground);
		case 3: return new BlockRock(foreground);
		case 4: return new BlockLightstone(foreground);
		case 5: return new BlockTreewood(foreground);
		case 6: return new BlockLeaves(foreground);
		case 7: return new BlockWood(foreground);
		default: throw new IllegalArgumentException("Invalid Block ID: " + (int)id);
		}
	}

	/**
	 * Turn an instance of Item to a block.
	 * @param i the Item to get the Block from.
	 * @param foreground whether the block should be a forground-block or not.
	 * @return the Block.
	 * @see org.worldOfCube.client.blocks.BlockID#itemToBlock(int, boolean)
	 */
	public static Block itemToBlock(Item i, boolean foreground) {
		return itemToBlock(i.getID(), foreground);
	}

	/**
	 * Creates a Block from a ItemID, without having added the
	 * Block to a specific Chunk.
	 * Throws an IllegalArgumentException, when the ItemID is invalid.
	 * @param id the ItemID to create the block from.
	 * @param foreground whether the block should be a foreground-block or not.
	 * @return the Block.
	 */
	public static Block itemToBlock(int id, boolean foreground) {
		switch (id) {
		case Item.EARTH: return new BlockEarth(foreground);
		case Item.GRASS: return new BlockGrass(foreground);
		case Item.ROCK: return new BlockRock(foreground);
		case Item.LIGHTSTONE: return new BlockLightstone(foreground);
		case Item.TREEWOOD: return new BlockTreewood(foreground);
		case Item.LEAVES: return new BlockLeaves(foreground);
		case Item.WOOD: return new BlockWood(foreground);
		default: throw new IllegalArgumentException("Invalid Item ID: " + id);
		}
	}

	/**
	 * Used by the WorldSaveManager to save char representations
	 * of Blocks into a file.
	 * @param b the block to get the BlockID from.
	 * @return a char representation of the Block a.k.a. BlockID.
	 */
	public static char blockToId(Block b) {
		if (b == null) return 0;
		if (b instanceof BlockEarth) {
			return 1;
		} else if (b instanceof BlockGrass) {
			return 2;
		} else if (b instanceof BlockRock) {
			return 3;
		} else if (b instanceof BlockLightstone) {
			return 4;
		} else if (b instanceof BlockTreewood) {
			return 5;
		} else if (b instanceof BlockLeaves) {
			return 6;
		} else if (b instanceof BlockWood) {
			return 7;
		}
		return 0;
	}

	/**
	 * @param b the Block to get the itemID from.
	 * @return the ItemID.
	 */
	public static int blockToItem(Block b) {
		if (b instanceof BlockEarth) {
			return Item.EARTH;
		} if (b instanceof BlockGrass) {
			return Item.EARTH;
		} if (b instanceof BlockRock) {
			return Item.ROCK;
		} if (b instanceof BlockLightstone) {
			return Item.LIGHTSTONE;
		} if (b instanceof BlockTreewood) {
			return Item.TREEWOOD;
		} if (b instanceof BlockLeaves) {
			return Item.LEAVES;
		} if (b instanceof BlockWood) {
			return Item.WOOD;
		}
		return -1;
	}

	/**
	 * Used by the Inventory, to render Block sprites,
	 * or to render Items in general.
	 * @param id the ItemID to get the sprite from.
	 * @return the Sprite.
	 */
	public static Sprite itemToSprite(int id) {
		switch(id) {
		case Item.EARTH: return ResLoader.get(ResLoader.BLOCK_EARTH, ResLoader.ALONE);
		case Item.GRASS: return ResLoader.get(ResLoader.BLOCK_GRASS, ResLoader.ALONE);
		case Item.ROCK: return ResLoader.get(ResLoader.BLOCK_ROCK, ResLoader.ALONE);
		case Item.LIGHTSTONE:return ResLoader.get(ResLoader.BLOCK_LIGHTSTONE, ResLoader.ALONE);
		case Item.TREEWOOD: return ResLoader.get(ResLoader.BLOCK_TREEWOOD, ResLoader.ALONE);
		case Item.LEAVES: return ResLoader.get(ResLoader.BLOCK_LEAVES, ResLoader.ALONE);
		case Item.WOOD: return ResLoader.get(ResLoader.BLOCK_WOOD, ResLoader.ALONE);
		default: throw new IllegalArgumentException("Invalid Item ID: " + id);
		}
	}

}
