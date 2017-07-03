package com.scs.physicsspacewar.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import com.scs.physicsspacewar.BodyUserData;
import com.scs.physicsspacewar.JBox2DFunctions;
import com.scs.physicsspacewar.Main;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public class SpaceStation extends PhysicalEntity implements IDrawable {
	
	private static final float FRICTION = .1f;
	

	public SpaceStation(Main main, World world, float x, float y, float w, float h) {
		super(main, SpaceStation.class.getSimpleName());
		
		BodyUserData bud = new BodyUserData(this.getClass().getSimpleName(), Color.cyan, this);
		body = JBox2DFunctions.AddRectangle(bud, world, x, y, w, h, BodyType.KINEMATIC, .1f, FRICTION, .1f);
		
		//body.applyTorque(100f);
		body.setAngularVelocity(1f);
	}


	@Override
	public void draw(Graphics g, DrawingSystem system) {
		//drawableBody.draw(g, system, cam_centre);
		system.drawShape(tmpPoint, g, body, false);
		
	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}


}
