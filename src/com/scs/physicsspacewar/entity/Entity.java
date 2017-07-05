package com.scs.physicsspacewar.entity;

import java.awt.Point;

import org.jbox2d.dynamics.World;

import com.scs.physicsspacewar.Main_SpaceWar;

public abstract class Entity { 
	
	private static int next_id = 0;

	public int id;
	public Point tmpPoint = new Point();
	protected Main_SpaceWar main;
	public String name;
	
	public Entity(Main_SpaceWar _main, String _name) {
		super();
		
		main = _main;
		id = next_id++;
		name =_name;
	}
	

	public abstract void cleanup(World world);
	
	/*@Override
	public String toString() {
		return name;
	}*/
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}





}
