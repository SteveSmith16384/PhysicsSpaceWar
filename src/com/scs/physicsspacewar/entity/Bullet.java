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
import com.scs.physicsspacewar.entity.components.ICanShoot;
import com.scs.physicsspacewar.entity.components.ICollideable;
import com.scs.physicsspacewar.entity.components.IDamagable;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.components.IProcessable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public class Bullet extends PhysicalEntity implements IDrawable, ICollideable, IAffectedByGravity, IProcessable {
	
	private ICanShoot shooter;
	private long timeUntilDeadlyToShooter;
	
	private Stroke stroke = new BasicStroke(2);

	public Bullet(Main_SpaceWar main, ICanShoot shooter_, Vec2 pos, Vec2 force) {
		super(main, Bullet.class.getSimpleName());

		shooter = shooter_;
		
		BodyUserData bud = new BodyUserData("Bullet", shooter.getBulletColor(), this);
		body = JBox2DFunctions.AddCircle(bud, main.world, pos.x, pos.y, .1f, BodyType.DYNAMIC, .1f, .2f, .1f);
		
		body.applyLinearImpulse(force, Statics.VEC_CENTRE, true);
		//body.applyForceToCenter(force);//, v);
		
		timeUntilDeadlyToShooter = 2000;

	}

	
	@Override
	public void draw(Graphics g, DrawingSystem system) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(stroke);
		system.drawShape(tmpPoint, g, body, false);
	}


	@Override
	public void collided(Entity other) {
		if (other != shooter || timeUntilDeadlyToShooter < 0) {
			if (other instanceof IDamagable) {
				IDamagable id = (IDamagable)other;
				id.damage(1);
			}
			main.removeEntity(this);
		}
		
	}


	@Override
	public float getMass() {
		return super.getMass() * 5;//10;
	}


	@Override
	public void preprocess(long interpol) {
		this.timeUntilDeadlyToShooter -= interpol;
		
	}


	@Override
	public void postprocess(long interpol) {
	}

}
