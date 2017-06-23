package com.scs.physicsspacewar.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import com.scs.physicsspacewar.BodyUserData;
import com.scs.physicsspacewar.JBox2DFunctions;
import com.scs.physicsspacewar.Main;
import com.scs.physicsspacewar.Player;
import com.scs.physicsspacewar.Statics;
import com.scs.physicsspacewar.entity.components.IAffectedByGravity;
import com.scs.physicsspacewar.entity.components.ICollideable;
import com.scs.physicsspacewar.entity.components.IDamagable;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.components.IPlayerControllable;
import com.scs.physicsspacewar.entity.components.IProcessable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;
import com.scs.physicsspacewar.input.IInputDevice;

public class PlayersShip extends PhysicalEntity implements IPlayerControllable, IDrawable, ICollideable, IProcessable, IAffectedByGravity, IDamagable {

	private static final int SHOT_INT = 2000;
	public static final float RAD = 10f;
	//private static final float MAX_VELOCITY = 5;//7f;	

	public int id;
	private IInputDevice input;
	private long lastShotTime;

	public PlayersShip(Player player, Main main, World world, float x, float y) {
		super(main, PlayersShip.class.getSimpleName());

		input = player.input;
		id = player.id;

		BodyUserData bud = new BodyUserData("Player_Body", Color.black, this);
		//Body body = JBox2DFunctions.AddRectangle(bud, world, 50, 10, 4, 4, BodyType.DYNAMIC, .2f, .2f, .4f);
		//body = JBox2DFunctions.AddCircle(bud, world, x, y, RAD, BodyType.DYNAMIC, .1f, .6f, .5f);
		Vec2[] vertices = new Vec2[3];
		vertices[0] = new Vec2(RAD/2, 0);
		vertices[1] = new Vec2(RAD, RAD);
		vertices[2] = new Vec2(0, RAD);
		body = JBox2DFunctions.CreateComplexShape(bud, world, vertices, BodyType.DYNAMIC, .2f, .3f, 1f);
		body.setBullet(true);

		PolygonShape ps = new PolygonShape();
		ps.setAsBox(RAD/2, RAD/10, new Vec2(0, RAD), 0);

	}


	@Override
	public void processInput() {
		if (input.isLeftPressed()) {
			body.applyTorque(-Statics.TURN_TORQUE);
		} else if (input.isRightPressed()) {
			body.applyTorque(Statics.TURN_TORQUE);		
		}
		
		if (input.isJumpPressed()) {
			Vec2 force = new Vec2();
			force.y = (float)Math.cos(body.getAngle()) * -1;
			force.x = (float)Math.sin(body.getAngle());
			force.mulLocal(Statics.ROTOR_FORCE);
			body.applyForceToCenter(force);//, v);
		}
		
		if (input.isFirePressed()) {
			if (System.currentTimeMillis() > lastShotTime + SHOT_INT) {
				Vec2 dir = new Vec2();
				dir.set(body.getLinearVelocity());
				Bullet bullet = new Bullet(main, this, this.body.getWorldCenter().add(dir), dir);
				main.addEntity(bullet);
				lastShotTime = System.currentTimeMillis();
			}
		}

		//Vec2 vel = body.getLinearVelocity();
		// cap max velocity on x		
		/*if(Math.abs(vel.x) > MAX_VELOCITY) {			
			vel.x = Math.signum(vel.x) * MAX_VELOCITY;
			body.setLinearVelocity(vel);
		}*/

	}


	@Override
	public void draw(Graphics g, DrawingSystem system, Vec2 cam_centre) {
		system.drawShape(tmpPoint, g, body, cam_centre);
	}


	/*@Override
	public void collided(Contact contact, boolean weAreA) {
		Fixture f = null;
		if (weAreA) {
			f = contact.getFixtureB();
		} else {
			f = contact.getFixtureA();
		}
		//Statics.p("Player collided with " + f.getBody());
		BodyUserData bud = (BodyUserData)f.getBody().getUserData();
		if (bud != null) {
			Statics.p("Player collided with " + bud.name + " at " + contact.getTangentSpeed());
			if (bud.harmsPlayer) {
				Statics.p("Death!");
				main.restartAvatar(this);
			}
		}
	}*/


	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}


	@Override
	public void preprocess(long interpol) {

	}


	@Override
	public void postprocess(long interpol) {
	}


	@Override
	public void collided(Entity them) {
		/*BodyUserData bud = (BodyUserData)f.getBody().getUserData();
		if (bud != null) {
			Statics.p("Player collided with " + bud.name + " at " + contact.getTangentSpeed());
			if (bud.harmsPlayer) {
				Statics.p("Death!");
				main.restartAvatar(this);
			}
		}*/
		
	}


	@Override
	public void damage(float amt) {
		// TODO
		
	}


}
