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
package org.worldOfCube.client.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.worldOfCube.Log;

public class Config {

	private static final String configDir = "config/";
	private static final String configFile = configDir + "config.cfg";

	private static Config instance;

	private HashMap<String, String> cfg = new HashMap<String, String>();
	private HashMap<String, String> cfgRestart = new HashMap<String, String>();

	private Config() {
		initialize();
	}

	private void loadFromFile() throws IOException {
		File config = new File(configFile);
		if (!config.exists()) {
			return;
		}
		FileInputStream fis = null;
		BufferedReader read = null;
		try {
			fis = new FileInputStream(config);
			read = new BufferedReader(new InputStreamReader(fis));
			String line;
			int linenum = 0;
			while ((line = read.readLine()) != null) {
				linenum++;
				if (line.isEmpty()
						|| line.startsWith("//")
						|| line.startsWith("#")) continue;
				String[] strs = line.split(":");
				if (strs.length != 2) {
					Log.err("Config corrupt: line " + linenum + ": " + line);
					continue;
				}
				String key = clearup(strs[0]);
				String value = clearup(strs[1]);
				cfg.put(key, value);
				cfgRestart.put(key, value);
			}
		} catch (IOException e) {
			Log.err("Error loading Config:");
			e.printStackTrace();
		} finally {
			if (read != null) {
				read.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
	}

	private void saveToFile() throws IOException {
		File dir = new File(configDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File config = new File(configFile);
		if (!config.exists()) {
			config.createNewFile();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(config);
			BufferedWriter write = new BufferedWriter(new OutputStreamWriter(fos));
			for (Map.Entry<String, String> entry : cfg.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				String restartVal = cfgRestart.get(key);
				if (restartVal != null) {
					write.write(key + ":" + restartVal);
					Log.out("Wrote to Config: " + key + ":" + restartVal);
				} else {
					write.write(key + ":" + value);
					Log.out("Wrote to Config (!using Restart): " + key + ":" + value);
				}
				write.newLine();
			}
			write.close();
		} catch (IOException e) {
			Log.err("Error saving Config:");
			e.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	private String clearup(String s) {
		StringTokenizer st = new StringTokenizer(s);
		StringBuilder sb = new StringBuilder();
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken());
		}
		return sb.toString();
	}

	private void initialize() {
		cfg.put("vsync", "on");
		cfg.put("block_rendering", "imm");
		cfg.put("debug", "off");
		cfg.put("show_fps", "false");
		cfgRestart.put("block_rendering", "imm");
		try {
			loadFromFile();
		} catch (IOException e) {
			Log.err("Error loading Config:");
			e.printStackTrace();
		}
		Log.out("Config loaded.");
	}

	private void saveInst() {
		try {
			saveToFile();
		} catch (IOException e) {
			Log.err("Error saving Config:");
			e.printStackTrace();
		}
		Log.out("Config saved.");
	}

	private String getInst(String key) {
		return cfg.get(key);
	}

	private String getRestartInst(String key) {
		return cfgRestart.get(key);
	}

	private void setInst(String key, String value) {
		cfg.put(key, value);
		cfgRestart.put(key, value);
	}

	private void setRestartInst(String key, String value) {
		cfgRestart.put(key, value);
	}

	private static Config inst() {
		if (instance == null) {
			return init();
		}
		return instance;
	}

	private static Config init() {
		return instance = new Config();
	}

	public static String get(String key) {
		return inst().getInst(key);
	}

	public static void set(String key, String value) {
		inst().setInst(key, value);
	}

	public static String getRestart(String key) {
		return inst().getRestartInst(key);
	}

	public static void setRestart(String key, String value) {
		inst().setRestartInst(key, value);
	}

	public static void save() {
		inst().saveInst();
	}

}
