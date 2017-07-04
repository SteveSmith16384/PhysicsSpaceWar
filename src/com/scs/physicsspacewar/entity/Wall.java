package com.scs.physicsspacewar.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.dynamics.BodyType;

import com.scs.physicsspacewar.BodyUserData;
import com.scs.physicsspacewar.JBox2DFunctions;
import com.scs.physicsspacewar.Main;
import com.scs.physicsspacewar.entity.components.ICausesGravity;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public class Wall extends PhysicalEntity implements IDrawable {

	public Wall(Main main, float x, float y, float w, float h) {
		super(main, "Wall");
		
		BodyUserData bud = new BodyUserData("Wall", Color.green, this);
		body = JBox2DFunctions.AddRectangle(bud, main.world, x, y, w, h, BodyType.STATIC, .1f, .2f, 10f);
	}


	@Override
	public void draw(Graphics g, DrawingSystem system) {
		system.drawShape(tmpPoint, g, body, false);
		
	}
	
	
}
