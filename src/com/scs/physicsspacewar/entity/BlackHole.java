package com.scs.physicsspacewar.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.jbox2d.common.Vec2;

import com.scs.physicsspacewar.Main_SpaceWar;
import com.scs.physicsspacewar.entity.components.ICausesGravity;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public class BlackHole extends PhysicalEntity implements ICausesGravity {

	private Point tmp = new Point();
	private Vec2 worldpos;

	public BlackHole(Main_SpaceWar _main, float x, float y) {
		super(_main, "BlockHole");

		worldpos = new Vec2(x, y);
	}


	@Override
	public void draw(Graphics g, DrawingSystem system) {
		system.drawDot(tmp, g, worldpos, Color.white);


	}

	
	@Override
	public float getMass() {
		return 10000;
	}

}
