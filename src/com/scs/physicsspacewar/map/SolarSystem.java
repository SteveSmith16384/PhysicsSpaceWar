package com.scs.physicsspacewar.map;

import java.awt.Color;
import java.awt.Point;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ssmith.util.RealtimeInterval;

import com.scs.physicsspacewar.Main_SpaceWar;
import com.scs.physicsspacewar.Statics;
import com.scs.physicsspacewar.entity.Moon;
import com.scs.physicsspacewar.entity.Sun;

public class SolarSystem extends AbstractMap {

	private RealtimeInterval meteorInt = new RealtimeInterval(3000);

	public SolarSystem(Main_SpaceWar main) {
		super(main);
	}


	@Override
	public Vec2 getGravity() {
		return new Vec2(0f, 0f);
	}


	public void createWorld(World world, Main_SpaceWar main) {
		Sun sun = new Sun(main, Statics.WORLD_WIDTH_LOGICAL/2, Statics.WORLD_HEIGHT_LOGICAL/2, 50, Color.yellow);
		main.addEntity(sun);

		Moon highMoon = new Moon(main, Statics.WORLD_WIDTH_LOGICAL/2, (Statics.WORLD_HEIGHT_LOGICAL/2) + 200, 10, Color.white);
		main.addEntity(highMoon);
		highMoon.applyForceToCenter(new Vec2(-highMoon.getMass() * 1200, 0)); // Orbit sun

		Moon lowMoon = new Moon(main, Statics.WORLD_WIDTH_LOGICAL/2, (Statics.WORLD_HEIGHT_LOGICAL/2) + 400, 12, Color.white.darker());
		main.addEntity(lowMoon);
		lowMoon.applyForceToCenter(new Vec2(lowMoon.getMass() * 900, 0)); // Orbit sun

	}


	@Override
	public Point getPlayerStartPos(int pid) {
		switch (pid) {
		case 0:
			return new Point((int)Statics.WORLD_WIDTH_LOGICAL/4, (int)(Statics.WORLD_HEIGHT_LOGICAL /4));
		case 1:
			return new Point((int)(Statics.WORLD_WIDTH_LOGICAL*.75f), (int)(Statics.WORLD_HEIGHT_LOGICAL*.75f));
		case 2:
			return new Point((int)Statics.WORLD_WIDTH_LOGICAL/4, (int)(Statics.WORLD_HEIGHT_LOGICAL*.75f));
		case 3:
			return new Point((int)(Statics.WORLD_WIDTH_LOGICAL*.75f), (int)(Statics.WORLD_HEIGHT_LOGICAL /4));
		default:
			throw new RuntimeException("Todo");
		}
	}



	@Override
	public void preprocess(long interpol) {
		if (meteorInt.hitInterval()) {
			Vec2 pos = new Vec2(Statics.WORLD_WIDTH_LOGICAL * .75f, Statics.WORLD_HEIGHT_LOGICAL * 0.75f);
			Vec2 dir = new Vec2(-40000, 0);
			//Meteor met = new Meteor(main, pos, dir);
			//main.addEntity(met);
		}
	}

}
