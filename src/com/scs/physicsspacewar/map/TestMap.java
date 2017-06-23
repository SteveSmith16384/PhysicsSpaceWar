package com.scs.physicsspacewar.map;

import java.awt.Point;

import org.jbox2d.dynamics.World;

import com.scs.physicsspacewar.Main;
import com.scs.physicsspacewar.Statics;
import com.scs.physicsspacewar.entity.Sun;

public class TestMap extends AbstractMap {

	public TestMap(Main main) {
		super(main);
	}


	public void createWorld(World world, Main main) {
		Sun sun = new Sun(main, world, Statics.WORLD_WIDTH_LOGICAL/2, Statics.WORLD_HEIGHT_LOGICAL/2, 100);
		main.addEntity(sun);
		
	}


	@Override
	public Point getPlayerStartPos(int pid) {
		return new Point((int)Statics.WORLD_WIDTH_LOGICAL/4, (int)(Statics.WORLD_HEIGHT_LOGICAL /4));
	}


}
