package com.scs.physicsspacewar.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import org.jbox2d.dynamics.BodyType;

import com.scs.physicsspacewar.BodyUserData;
import com.scs.physicsspacewar.JBox2DFunctions;
import com.scs.physicsspacewar.Main_SpaceWar;
import com.scs.physicsspacewar.entity.components.IAffectedByGravity;
import com.scs.physicsspacewar.entity.components.ICausesGravity;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public abstract class AbstractPlanet extends PhysicalEntity implements IDrawable, IAffectedByGravity { // ICausesGravity

	private Stroke stroke = new BasicStroke(4);

	public AbstractPlanet(Main_SpaceWar main, String name, float x, float y, float rad, Color c) {
		super(main, name);
		
		BodyUserData bud = new BodyUserData(name, c, this);
		body = JBox2DFunctions.AddCircle(bud, main.world, x, y, rad, BodyType.DYNAMIC, .1f, .2f, 5f);
	}


	@Override
	public void draw(Graphics g, DrawingSystem system) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(stroke);
		system.drawShape(tmpPoint, g, body, false);
		
	}
	
	
}
