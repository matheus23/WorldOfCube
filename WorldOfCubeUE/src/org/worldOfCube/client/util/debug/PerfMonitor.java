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
package org.worldOfCube.client.util.debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.Sys;
import org.worldOfCube.client.res.GLFont;

public class PerfMonitor {

	private static PerfMonitor instance;

	private long actTime;
	private List<String> current = new ArrayList<String>();
	private HashMap<String, Long> profiles = new HashMap<String, Long>();
	private float offsetx = 10f;
	private float offsety = 400f;
	private int longestString = 0;

	private void startProfileInst(String name) {
		synchronized(this) {
			longestString = Math.max(longestString, name.length());
			actTime = ms();
			current.add(name);
		}
	}

	private void stopProfileInst(String name) {
		int ind = current.indexOf(name);
		if (ind >= 0) {
			synchronized(this) {
				try {
					profiles.put(current.get(ind), ms()-actTime);
					current.remove(name);
				} catch (Exception e) {
					current.remove(name);
				}
			}
		}
	}

	private void renderInst() {
		synchronized(this) {
			Iterator<Entry<String, Long>> it = profiles.entrySet().iterator();
			int i = 0;
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pair = it.next();
				GLFont.render(offsetx, offsety+i*10, GLFont.ALIGN_LEFT,
						String.format("%" + String.valueOf(longestString) + "s: %4d ms", pair.getKey(), pair.getValue()),
						10);
				i++;
			}
		}
	}

	private static PerfMonitor inst() {
		if (instance == null) {
			return instance = new PerfMonitor();
		}
		return instance;
	}

	private static long ms() {
		return ((Sys.getTime()*1000)/Sys.getTimerResolution());
	}

	public static void startProfile(String name) {
		inst().startProfileInst(name);
	}

	public static void stopProfile(String name) {
		inst().stopProfileInst(name);
	}

	public static void render() {
		inst().renderInst();
	}

}
