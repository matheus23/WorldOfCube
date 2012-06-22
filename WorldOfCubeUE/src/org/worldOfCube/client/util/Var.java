package org.worldOfCube.client.util;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public final class Var {
	
	public static float drawx;
	public static float drawy;
	public static float col1;
	public static float col2;
	public static float col3;
	public static float col4;
	public static FloatBuffer floatBuffer8 = BufferUtils.createFloatBuffer(8);
	public static FloatBuffer floatBuffer8_vert = BufferUtils.createFloatBuffer(8);
	public static FloatBuffer floatBuffer12_col = BufferUtils.createFloatBuffer(12);
	public static FloatBuffer floatBuffer8_tex = BufferUtils.createFloatBuffer(8);

}
