package com.scs.physicsspacewar.map;

import java.awt.Color;
import java.awt.Point;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ssmith.lang.Functions;

import com.scs.physicsspacewar.Main_SpaceWar;
import com.scs.physicsspacewar.Statics;
import com.scs.physicsspacewar.entity.Sun;

public class LotsOfMoons extends AbstractMap {

	public LotsOfMoons(Main_SpaceWar main) {
		super(main);
	}


	@Override
	public Vec2 getGravity() {
		return new Vec2(0f, 0f);
	}


	public void createWorld(World world, Main_SpaceWar main) {
		for (int i=0 ; i<9 ; i++) {
			float x = Functions.rndFloat(0,  Statics.WORLD_WIDTH_LOGICAL);
			float y = Functions.rndFloat(0,  Statics.WORLD_HEIGHT_LOGICAL);
			float rad = Functions.rndFloat(15,  40);
			Sun sun = new Sun(main, x, y, rad, Color.DARK_GRAY);
			main.addEntity(sun);
		}

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
	}

}
