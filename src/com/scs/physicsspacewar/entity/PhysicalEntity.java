package com.scs.physicsspacewar.entity;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import com.scs.physicsspacewar.Main;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.components.IGetPosition;

public abstract class PhysicalEntity extends Entity implements IDrawable, IGetPosition {

	public Body body;
	
	public PhysicalEntity(Main _main, String _name) {
		super(_main, _name);
	}


	@Override
	public Vec2 getPosition() {
		return body.getWorldCenter();
	}


	public void applyForceToCenter(Vec2 vec) {
		body.applyForceToCenter(vec);
		
	}

	@Override
	public void cleanup(World world) {
		world.destroyBody(body);
	}

}
