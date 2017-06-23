package com.scs.physicsspacewar;

import java.awt.Color;

import com.scs.physicsspacewar.entity.Entity;

public class BodyUserData { // For attaching to bodies
	
	public String name;
	public Entity entity;
	public Color col;
	//public boolean harmsPlayer = false;

	
	public BodyUserData(String _name, Color c, Entity e) {
		super();
		
		name = _name;
		col = c;
		entity = e;
	}


	@Override
	public String toString() {
		return name;
	}

}
