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
package org.worldOfCube.client.logic.chunks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.worldOfCube.client.blocks.Block;
import org.worldOfCube.client.blocks.BlockEarth;
import org.worldOfCube.client.blocks.BlockGrass;
import org.worldOfCube.client.blocks.BlockLightstone;
import org.worldOfCube.client.blocks.BlockRock;

public class WorldMapGenerator {

	public static final String screendir = "worldimgs";

	public WorldMapGenerator(World world) {
		BufferedImage img = new BufferedImage(world.totalPix, world.totalPix, BufferedImage.TYPE_3BYTE_BGR);
		createImageData(img, world);

		File screendirectory = new File(screendir);
		if (!screendirectory.exists()) {
			screendirectory.mkdir();
		}
		String screenname = screendir + "/worldimg";
		String imageformat = ".png";
		int i = 0;
		File f = new File(screenname + i + imageformat);

		do {
			f = new File(screenname + i + imageformat);
			i++;
		} while(f.exists());
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		saveBuffimg(img, f);
	}

	private void createImageData(BufferedImage img, World world) {
		int r = 0;
		int g = 0;
		int b = 0;
		Block block;
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				block = world.getChunkManager().getBlock(x, y, true);
				if (block != null) {
					if (block instanceof BlockGrass) {
						r = 0x00;
						g = 0xFF;
						b = 0x00;
					} else if (block instanceof BlockEarth) {
						r = 0xAC;
						g = 0x77;
						b = 0x54;
					} else if (block instanceof BlockRock) {
						r = 0x44;
						g = 0x44;
						b = 0x44;
					} else if (block instanceof BlockLightstone) {
						r = 0x88;
						g = 0x88;
						b = 0x00;
					} else {
						r = 0x91;
						g = 0xBA;
						b = 0xFF;
					}
				} else {
					r = 0x91;
					g = 0xBA;
					b = 0xFF;
				}
				img.setRGB(x, y, (r >> 8) | (g >> 16) | (b >> 24));
			}
		}
	}

	private void saveBuffimg(BufferedImage img, File file) {
		try {
			ImageIO.write(img, "PNG", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
