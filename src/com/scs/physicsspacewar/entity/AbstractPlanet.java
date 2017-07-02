package com.scs.physicsspacewar.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.dynamics.BodyType;

import com.scs.physicsspacewar.BodyUserData;
import com.scs.physicsspacewar.JBox2DFunctions;
import com.scs.physicsspacewar.Main;
import com.scs.physicsspacewar.entity.components.IAffectedByGravity;
import com.scs.physicsspacewar.entity.components.ICausesGravity;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public abstract class AbstractPlanet extends PhysicalEntity implements IDrawable, ICausesGravity, IAffectedByGravity {

	public AbstractPlanet(Main main, String name, float x, float y, float rad, Color c) {
		super(main, name);
		
		BodyUserData bud = new BodyUserData(name, c, this);
		body = JBox2DFunctions.AddCircle(bud, main.world, x, y, rad, BodyType.DYNAMIC, .1f, .2f, 5f);
	}


	@Override
	public void draw(Graphics g, DrawingSystem system) {
		system.drawShape(tmpPoint, g, body, false);
		
	}
	
	
}
