package com.scs.physicsspacewar.map;

import java.awt.Point;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ssmith.util.Interval;

import com.scs.physicsspacewar.Main;
import com.scs.physicsspacewar.Statics;
import com.scs.physicsspacewar.entity.Meteor;
import com.scs.physicsspacewar.entity.Sun;

public class TestMap extends AbstractMap {

	private Interval meteorInt = new Interval(3000);

	public TestMap(Main main) {
		super(main);
	}


	public void createWorld(World world, Main main) {
		Sun sun = new Sun(main, world, Statics.WORLD_WIDTH_LOGICAL/2, Statics.WORLD_HEIGHT_LOGICAL/2, 50);
		main.addEntity(sun);

	}


	@Override
	public Point getPlayerStartPos(int pid) {
		return new Point((int)Statics.WORLD_WIDTH_LOGICAL/4, (int)(Statics.WORLD_HEIGHT_LOGICAL /4));
	}



	@Override
	public void preprocess(long interpol) {
		if (meteorInt.hitInterval()) {
			Vec2 pos = new Vec2(Statics.WORLD_WIDTH_LOGICAL * .75f, Statics.WORLD_HEIGHT_LOGICAL * 0.75f);
			Vec2 dir = new Vec2(-40000, 0);
			Meteor met = new Meteor(main, pos, dir);
			//main.addEntity(met);
		}
	}

}
