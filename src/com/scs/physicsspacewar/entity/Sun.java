package com.scs.physicsspacewar.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import com.scs.physicsspacewar.BodyUserData;
import com.scs.physicsspacewar.JBox2DFunctions;
import com.scs.physicsspacewar.Main;
import com.scs.physicsspacewar.entity.components.ICausesGravity;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public class Sun extends PhysicalEntity implements IDrawable, ICausesGravity {

	public Sun(Main main, World world, float x, float y, float rad) {
		super(main, "Sun");
		
		BodyUserData bud = new BodyUserData("Sun", Color.yellow, this);
		body = JBox2DFunctions.AddCircle(bud, world, x, y, rad, BodyType.DYNAMIC, .1f, .2f, 10);
	}


	@Override
	public void draw(Graphics g, DrawingSystem system) {
		system.drawShape(tmpPoint, g, body);
		
	}
	
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}


	@Override
	public float getGravityStrength() {
		return .5f;
	}

}
