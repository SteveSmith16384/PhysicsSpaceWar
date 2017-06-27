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

public class Bullet extends PhysicalEntity implements IDrawable, ICollideable, IAffectedByGravity {
	
	private Entity shooter;
	
	public Bullet(Main main, Entity shooter_, Vec2 pos, Vec2 force) {
		super(main, Bullet.class.getSimpleName());

		shooter = shooter_;
		
		BodyUserData bud = new BodyUserData("Bullet", Color.darkGray, this); // todo - make yellow
		body = JBox2DFunctions.AddCircle(bud, main.world, pos.x, pos.y, .1f, BodyType.DYNAMIC, .1f, .2f, .1f);
		
		body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);
		//body.applyForceToCenter(force);//, v);

	}

	
	@Override
	public void draw(Graphics g, DrawingSystem system) {
		system.drawShape(tmpPoint, g, body);
	}


	@Override
	public void collided(Entity other) {
		if (other != shooter) {
			if (other instanceof IDamagable) {
				IDamagable id = (IDamagable)other;
				id.damage(1f);
			}
			main.removeEntity(this);
		}
		
	}


	@Override
	public float getMass() {
		return super.getMass() * 10;
	}

}
