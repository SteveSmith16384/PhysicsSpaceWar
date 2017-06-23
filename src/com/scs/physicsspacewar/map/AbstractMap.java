package com.scs.physicsspacewar.map;

import java.awt.Point;

import org.jbox2d.dynamics.World;

import com.scs.physicsspacewar.Main;
import com.scs.physicsspacewar.entity.Entity;
import com.scs.physicsspacewar.entity.components.IProcessable;

public abstract class AbstractMap extends Entity implements IProcessable {

	/*public static AbstractLevel GetLevel(int id, Main main) {
		switch (id) {
		case 1:
			return new Level1(main);
		case 2:
			return new Level2(main);
		case 3:
			return new Level3_DonkeyKong(main);
		case 4:
			return new Level4_FallingCrates(main);
		case 5:
			return new Level5_BigSpinner(main);
		case 6:
			return new Level6_Warehouse(main);
		case 7:
			return new Level7_Dominoes(main);
		case 8:
			return new Level8_MovingPlatforms(main);
		default:
			throw new RuntimeException("No such level: " + id);
		}
	}*/


	public AbstractMap(Main main) {
		super(main, "level");
	}


	public abstract Point getPlayerStartPos(int pid);

	public abstract void createWorld(World world, Main main);

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
