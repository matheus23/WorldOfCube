package org.worldOfCube;

import java.io.PrintStream;

import org.worldOfCube.client.util.TimeUtil;

public class Log {
	
	private static final String prefix = "LOG::";
	private static Log instance;
	private PrintStream err;
	private PrintStream out;
	private long startupTime;
	
	private Log() {
		// TODO: Prepare for release: (or maybe not change the streams?)
		err = System.err;
		out = System.out;
		startupTime = TimeUtil.ms();
	}
	
	private String pref(Class<? extends Object> c) {
		return "[" + getTimeString() + " " + prefix + c.getSimpleName() + "] ";
	}
	
	private void errInst(Class<? extends Object> c, String s) {
		err.println(pref(c) + s);
	}
	
	private void outInst(Class<? extends Object> c, String s) {
		out.println(pref(c) + s);
	}
	
	private void errfInst(Class<? extends Object> c, String s, Object ... objs) {
		err.printf(pref(c) + s + "\n", objs);
	}
	
	private void outfInst(Class<? extends Object> c, String s, Object ... objs) {
		out.printf(pref(c) + s + "\n", objs);
	}
	
	private void setOutInst(PrintStream out) {
		this.out = out;
	}
	
	private void setErrInst(PrintStream err) {
		this.err = err;
	}
	
	private String getTimeString() {
		long delta = TimeUtil.ms()-startupTime;
		long deltaSec = delta/1000L;
		return String.format("%02d:%02d", deltaSec/60L, deltaSec%60L);
	}
	
	private static Log instance() {
		if (instance == null) {
			return instance = new Log();
		}
		return instance;
	}
	
	public static void err(Class<? extends Object> c, String s) {
		instance().errInst(c, s);
	}
	
	public static void out(Class<? extends Object> c, String s) {
		instance().outInst(c, s);
	}
	
	public static void errf(Class<? extends Object> c, String s, Object ... objs) {
		instance().errfInst(c, s, objs);
	}
	
	public static void out(Class<? extends Object> c, String s, Object ... objs) {
		instance().outfInst(c, s, objs);
	}
	
	public static void err(Object o, String s) {
		instance().errInst(o.getClass(), s);
	}
	
	public static void out(Object o, String s) {
		instance().outInst(o.getClass(), s);
	}
	
	public static void errf(Object o, String s, Object ... objs) {
		instance().errfInst(o.getClass(), s, objs);
	}
	
	public static void out(Object o, String s, Object ... objs) {
		instance().outfInst(o.getClass(), s, objs);
	}
	
	public static void setOut(PrintStream out) {
		instance().setOutInst(out);
	}
	
	public static void setErr(PrintStream err) {
		instance().setErrInst(err);
	}
	
}
