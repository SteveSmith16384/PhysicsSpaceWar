package com.scs.physicsspacewar.map;

import java.awt.Point;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import com.scs.physicsspacewar.Main_SpaceWar;
import com.scs.physicsspacewar.entity.Entity;
import com.scs.physicsspacewar.entity.components.IProcessable;

public abstract class AbstractMap extends Entity implements IProcessable {

	public AbstractMap(Main_SpaceWar main) {
		super(main, "Map");
	}

	
	public abstract Vec2 getGravity();

	public abstract Point getPlayerStartPos(int pid);

	public abstract void createWorld(World world, Main_SpaceWar main);

	
	@Override
	public void preprocess(long interpol) {

	}


	@Override
	public void postprocess(long interpol) {

	}


	@Override
	public void cleanup(World world) {
		// Do nothing
	}

}
