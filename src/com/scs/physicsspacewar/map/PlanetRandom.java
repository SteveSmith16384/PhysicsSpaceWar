package com.scs.physicsspacewar.map;

import org.jbox2d.dynamics.World;

import ssmith.lang.Functions;

import com.scs.physicsspacewar.Main_SpaceWar;
import com.scs.physicsspacewar.Statics;
import com.scs.physicsspacewar.entity.Wall;

public class PlanetRandom extends PlanetSurface {

	public PlanetRandom(Main_SpaceWar main) {
		super(main);
	}


	public void createWorld(World world, Main_SpaceWar main) {
		super.createWorld(world, main);

		for (int i=0 ; i<6 ; i++) {
			float x = Functions.rndFloat(0, Statics.WORLD_WIDTH_LOGICAL);
			float y = Functions.rndFloat(0, Statics.WORLD_WIDTH_LOGICAL);
			float w = Functions.rndFloat(Statics.WORLD_WIDTH_LOGICAL/4, Statics.WORLD_WIDTH_LOGICAL/2);
			float h = Functions.rndFloat(5, 20);
			Wall block = null;
			if (Functions.rnd(1, 2) == 1) {
				// wide/thin
				block = new Wall(main, x, y, w, h);
			} else {
				// Tall/thin
				block = new Wall(main, x, y, h, w);
			}
			main.addEntity(block);
		}
	}

}
