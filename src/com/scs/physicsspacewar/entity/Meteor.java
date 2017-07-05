package com.scs.physicsspacewar.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import com.scs.physicsspacewar.BodyUserData;
import com.scs.physicsspacewar.JBox2DFunctions;
import com.scs.physicsspacewar.Main_SpaceWar;
import com.scs.physicsspacewar.Statics;
import com.scs.physicsspacewar.entity.components.IAffectedByGravity;
import com.scs.physicsspacewar.entity.components.ICollideable;
import com.scs.physicsspacewar.entity.components.IDamagable;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public class Meteor extends PhysicalEntity implements IDrawable, ICollideable, IAffectedByGravity {

	private static final float RADIUS = 10f;
	private static final float DENSITY = 1f;
	
	private Stroke stroke = new BasicStroke(4);

	public Meteor(Main_SpaceWar main, Vec2 pos, Vec2 force) {
		super(main, Meteor.class.getSimpleName());

		BodyUserData bud = new BodyUserData("Meteor", Color.DARK_GRAY, this);
		body = JBox2DFunctions.AddCircle(bud, main.world, pos.x, pos.y, RADIUS, BodyType.DYNAMIC, .1f, .2f, DENSITY);

		float mass = (float)(Math.PI * (RADIUS*RADIUS) * DENSITY);
		
		body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);
	}


	@Override
	public void draw(Graphics g, DrawingSystem system) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(stroke);
		system.drawShape(tmpPoint, g, body, false);
	}


	@Override
	public void collided(Entity other) {
		if (other instanceof IDamagable) {
			IDamagable id = (IDamagable)other;
			id.damage(1);
		}

	}


}
