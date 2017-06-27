package com.scs.physicsspacewar.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import ssmith.lang.Functions;

import com.scs.physicsspacewar.BodyUserData;
import com.scs.physicsspacewar.JBox2DFunctions;
import com.scs.physicsspacewar.Main;
import com.scs.physicsspacewar.Statics;
import com.scs.physicsspacewar.entity.components.IAffectedByGravity;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.components.IProcessable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public class ExplosionShard extends PhysicalEntity implements IDrawable, IProcessable, IAffectedByGravity {

	private long removeTime = 3000;

	public ExplosionShard(Main main, Vec2 pos) {
		super(main, ExplosionShard.class.getSimpleName());

		BodyUserData bud = new BodyUserData("ExplosionShard", Color.red, this);
		body = JBox2DFunctions.AddCircle(bud, main.world, pos.x, pos.y, .1f, BodyType.DYNAMIC, .01f, 1f, 100f);
		
		Vec2 force = new Vec2(Functions.rndFloat(-10, 10), Functions.rndFloat(-10, 10));
		force.normalize();
		force.mulLocal(100);
		body.setBullet(true);
		body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);
	}

	
	@Override
	public void preprocess(long interpol) {
		removeTime -= interpol;
		if (removeTime <= 0) {
			main.removeEntity(this);
		}		
	}

	@Override
	public void postprocess(long interpol) {
		
	}


	@Override
	public void draw(Graphics g, DrawingSystem system) {
		system.drawShape(tmpPoint, g, body);
	}



}
