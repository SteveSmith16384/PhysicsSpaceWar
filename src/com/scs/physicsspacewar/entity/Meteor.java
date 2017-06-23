package com.scs.physicsspacewar.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import com.scs.physicsspacewar.BodyUserData;
import com.scs.physicsspacewar.JBox2DFunctions;
import com.scs.physicsspacewar.Main;
import com.scs.physicsspacewar.Statics;
import com.scs.physicsspacewar.entity.components.IAffectedByGravity;
import com.scs.physicsspacewar.entity.components.ICollideable;
import com.scs.physicsspacewar.entity.components.IDamagable;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public class Meteor extends PhysicalEntity implements IDrawable, ICollideable, IAffectedByGravity {

	public Meteor(Main main, Vec2 pos, Vec2 force) {
		super(main, Meteor.class.getSimpleName());

		BodyUserData bud = new BodyUserData("Meteor", Color.yellow, this);
		body = JBox2DFunctions.AddCircle(bud, main.world, pos.x, pos.y, .1f, BodyType.DYNAMIC, .1f, .2f, 1f);

		force.normalize();
		body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);
	}


	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		system.drawShape(tmpPoint, g, body, cam_centre);
	}


	@Override
	public void collided(Entity other) {
		if (other instanceof IDamagable) {
			IDamagable id = (IDamagable)other;
			id.damage(1f);
		}

	}


}
