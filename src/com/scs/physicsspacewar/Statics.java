package com.scs.physicsspacewar;

import java.util.Random;

import org.jbox2d.common.Vec2;

import ssmith.awt.ImageCache;

public class Statics {

	public static final boolean DEBUG = true;
	
	public static final float TURN_TORQUE = 80*8f;
	public static final float JET_FORCE = 80*7f;
	
	public static final int FPS = 30;
	public static final float LOGICAL_TO_PIXELS = 3f;
	public static final Vec2 VEC_CENTRE = new Vec2(0, 0);

	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;

	public static final int WORLD_WIDTH_LOGICAL = (int)(WINDOW_WIDTH / LOGICAL_TO_PIXELS);
	public static final int WORLD_HEIGHT_LOGICAL = (int)(WINDOW_HEIGHT / LOGICAL_TO_PIXELS);

	public static final Random rnd = new Random();

	public static ImageCache img_cache;

	private Statics() {

	}


	public static void p(String s) {
		System.out.println(System.currentTimeMillis() + ":" + s);
	}


}
