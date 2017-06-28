package com.scs.physicsspacewar.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

import com.scs.physicsspacewar.BodyUserData;
import com.scs.physicsspacewar.JBox2DFunctions;
import com.scs.physicsspacewar.Main;
import com.scs.physicsspacewar.entity.components.IAffectedByGravity;
import com.scs.physicsspacewar.entity.components.ICollideable;
import com.scs.physicsspacewar.entity.components.IDamagable;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.components.IGetPosition;
import com.scs.physicsspacewar.entity.components.IProcessable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public class HomingMissile extends PhysicalEntity implements IDrawable, ICollideable, IAffectedByGravity, IProcessable {

	private static final float SPEED = 1f;
	
	private static final float RADIUS = 2f;
	private static final float DENSITY = 1f;
	
	private IGetPosition target;
	
	public HomingMissile(Main main, Vec2 pos, IGetPosition _target) {
		super(main, HomingMissile.class.getSimpleName());
		
		target = _target;

		BodyUserData bud = new BodyUserData("HomingMissile", Color.yellow, this);
		body = JBox2DFunctions.AddCircle(bud, main.world, pos.x, pos.y, RADIUS, BodyType.DYNAMIC, .1f, .2f, DENSITY);

		//body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);
	}


	@Override
	public void draw(Graphics g, DrawingSystem system) {
		system.drawShape(tmpPoint, g, body, true);
	}


	@Override
	public void collided(Entity other) {
		if (other instanceof IDamagable) {
			IDamagable id = (IDamagable)other;
			id.damage(1);
		}
		main.removeEntity(this);

	}


	@Override
	public void preprocess(long interpol) {
		// AIm towards target
		Vec2 force = this.target.getPosition().sub(this.getPosition());
		force.normalize();
		
		force.mulLocal(SPEED);
		
		this.applyForceToCenter(force);
		
	}


	@Override
	public void postprocess(long interpol) {
		
	}


}
