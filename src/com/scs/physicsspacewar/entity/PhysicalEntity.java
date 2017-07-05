package com.scs.physicsspacewar.entity;

import java.awt.Graphics;

import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;

import com.scs.physicsspacewar.Main_SpaceWar;
import com.scs.physicsspacewar.Statics;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.components.IGetPosition;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public abstract class PhysicalEntity extends Entity implements IDrawable, IGetPosition, RayCastCallback {

	public Body body;
	private boolean canSeeResult;
	
	public PhysicalEntity(Main_SpaceWar _main, String _name) {
		super(_main, _name);
	}


	@Override
	public Vec2 getPosition() {
		return body.getWorldCenter();
	}


	public void applyForceToCenter(Vec2 vec) {
		body.applyForceToCenter(vec);
		
	}


	public void applyLinearImpulse(Vec2 vec) {
		body.applyLinearImpulse(vec, Statics.VEC_CENTRE, true);
		
	}

	
	@Override
	public void cleanup(World world) {
		world.destroyBody(body);
	}


	public float getMass() {
		return body.getMass();
	}
	
	
	public boolean canSee(IGetPosition other) {
		canSeeResult = true; // default
		main.world.raycast(this, this.getPosition(), other.getPosition());
		return canSeeResult;
	}


	@Override
	public float reportFixture(Fixture fixture, Vec2 point, Vec2 normal, float fraction) {
		canSeeResult = false;
		return 0;
	}


}
