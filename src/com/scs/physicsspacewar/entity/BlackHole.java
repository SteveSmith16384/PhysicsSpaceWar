package com.scs.physicsspacewar.entity;

import java.awt.Graphics;

import org.jbox2d.common.Vec2;

import com.scs.physicsspacewar.Main;
import com.scs.physicsspacewar.entity.components.ICausesGravity;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public class BlackHole extends PhysicalEntity implements ICausesGravity {

	public BlackHole(Main _main) {
		super(_main, "BlockHole");
	}

	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		// Do nothing
		
	}

	@Override
	public float getGravityStrength() {
		return 1;
	}

}
