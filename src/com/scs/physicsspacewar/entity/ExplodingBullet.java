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
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.components.IProcessable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public class ExplodingBullet extends PhysicalEntity implements IDrawable, IProcessable, IAffectedByGravity {
	
	private long explode = 3000;

	public ExplodingBullet(Main main, Vec2 pos, int dir) {
		super(main, ExplodingBullet.class.getSimpleName());

		BodyUserData bud = new BodyUserData("Grenade", Color.yellow, this);
		body = JBox2DFunctions.AddCircle(bud, main.world, pos.x, pos.y, .1f, BodyType.DYNAMIC, .1f, .2f, 1f);
		
		Vec2 force = new Vec2(0, -1);
		if (dir == 1) {
			force.x = -1;
		} else {
			force.x = 1;
		}
		force.normalize();
		body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);
	}

	
	@Override
	public void preprocess(long interpol) {
		// Explode?
		explode -= interpol;
		if (explode <= 0) {
			for (int i=0 ; i<30 ; i++) {
				ExplosionShard shard = new ExplosionShard(main, this.body.getWorldCenter());
				main.addEntity(shard);
			}
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
