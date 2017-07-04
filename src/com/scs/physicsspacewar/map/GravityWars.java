package com.scs.physicsspacewar.map;

import java.awt.Point;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import com.scs.physicsspacewar.Main;
import com.scs.physicsspacewar.Statics;
import com.scs.physicsspacewar.entity.Wall;

public class GravityWars extends AbstractMap {

	public GravityWars(Main main) {
		super(main);
	}


	public void createWorld(World world, Main main) {
		Wall horiz = new Wall(main, Statics.WORLD_WIDTH_LOGICAL/2, Statics.WORLD_HEIGHT_LOGICAL/2, Statics.WORLD_WIDTH_LOGICAL/3, Statics.WORLD_HEIGHT_LOGICAL/20);
		main.addEntity(horiz);

		Wall vert = new Wall(main, Statics.WORLD_WIDTH_LOGICAL/2, Statics.WORLD_HEIGHT_LOGICAL/2, Statics.WORLD_WIDTH_LOGICAL/20, Statics.WORLD_HEIGHT_LOGICAL/3);
		main.addEntity(vert);
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


	@Override
	public Vec2 getGravity() {
		return new Vec2(0f, 0f);
	}

}
