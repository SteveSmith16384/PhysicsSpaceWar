package com.scs.physicsspacewar;

import java.util.Random;

import org.jbox2d.common.Vec2;

import ssmith.awt.ImageCache;

public class Statics {

	public static final boolean DEBUG = true;
	
	public static final int FPS = 30;
	public static final float LOGICAL_TO_PIXELS = 3f;
	public static final Vec2 VEC_CENTRE = new Vec2(0, 0);

	public static final boolean FULL_SCREEN = true;
	public static int WINDOW_WIDTH = 800;
	public static int WINDOW_HEIGHT = 600;

	public static int WORLD_WIDTH_LOGICAL;
	public static int WORLD_HEIGHT_LOGICAL;

	public static final Random rnd = new Random();

	public static ImageCache img_cache;

	private Statics() {

	}


	public static void p(String s) {
		System.out.println(System.currentTimeMillis() + ":" + s);
	}


}
