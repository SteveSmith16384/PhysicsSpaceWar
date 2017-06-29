package com.scs.physicsspacewar.entity;

import java.awt.Color;

import com.scs.physicsspacewar.Main;

public class Moon extends AbstractPlanet {

	public Moon(Main main, float x, float y, float rad) {
		super(main, "Moon", x, y, rad, Color.white);
	}
	
}
