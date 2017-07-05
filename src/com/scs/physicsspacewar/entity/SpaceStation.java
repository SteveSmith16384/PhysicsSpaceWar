package com.scs.physicsspacewar.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import com.scs.physicsspacewar.BodyUserData;
import com.scs.physicsspacewar.JBox2DFunctions;
import com.scs.physicsspacewar.Main_SpaceWar;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public class SpaceStation extends PhysicalEntity implements IDrawable {
	
	private static final float FRICTION = .1f;
	
	private Stroke stroke = new BasicStroke(4);


	public SpaceStation(Main_SpaceWar main, World world, float x, float y, float w, float h) {
		super(main, SpaceStation.class.getSimpleName());
		
		BodyUserData bud = new BodyUserData(this.getClass().getSimpleName(), Color.cyan, this);
		body = JBox2DFunctions.AddRectangle(bud, world, x, y, w, h, BodyType.KINEMATIC, .1f, FRICTION, .1f);
		
		//body.applyTorque(100f);
		body.setAngularVelocity(1f);
	}


	@Override
	public void draw(Graphics g, DrawingSystem system) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(stroke);
		system.drawShape(tmpPoint, g, body, false);
		
	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}


}
