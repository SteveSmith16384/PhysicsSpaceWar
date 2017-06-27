package com.scs.physicsspacewar.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import ssmith.util.Timer;

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
	public static final float SIZE = 4f;

	public int id;
	private IInputDevice input;
	private long lastShotTime;
	private Timer jetTimer = new Timer(100);
	private boolean isJetting = false;

	public PlayersShip(Player player, Main main, World world, float x, float y) {
		super(main, PlayersShip.class.getSimpleName());

		input = player.input;
		id = player.id;

		BodyUserData bud = new BodyUserData("Player_Body", Color.black, this);
		Vec2[] vertices = new Vec2[3];
		vertices[0] = new Vec2(SIZE/2, 0);
		vertices[1] = new Vec2(SIZE, SIZE);
		vertices[2] = new Vec2(0, SIZE);
		body = JBox2DFunctions.CreateComplexShape(bud, world, vertices, BodyType.DYNAMIC, .2f, .3f, 10f);
		body.setTransform(new Vec2(x, y), 1);
		body.setBullet(true);

		PolygonShape ps = new PolygonShape();
		ps.setAsBox(SIZE/2, SIZE/10, new Vec2(0, SIZE), 0);

	}


	@Override
	public void processInput() {
		if (input.isLeftPressed()) {
			body.applyTorque(-Statics.TURN_TORQUE);
			//if (body.getAngularVelocity() )
		} else if (input.isRightPressed()) {
			body.applyTorque(Statics.TURN_TORQUE);		
		}
		
		if (input.isJumpPressed()) {
			isJetting = true;
			Vec2 force = new Vec2();
			force.y = (float)Math.cos(body.getAngle()) * -1;
			force.x = (float)Math.sin(body.getAngle());
			force.mulLocal(Statics.JET_FORCE);
			body.applyForceToCenter(force);
		} else {
			isJetting = false;
		}
		
		if (input.isFirePressed()) {
			if (System.currentTimeMillis() > lastShotTime + SHOT_INT) {
				float dirx = (float)Math.sin(this.body.getAngle());
				float diry = (float)Math.cos(this.body.getAngle());
				Vec2 dir = new Vec2(dirx, diry*-1);
				
				Vec2 startOffset = dir.clone();
				startOffset.normalize();
				startOffset.mulLocal(SIZE*2);
				
				Vec2 force = dir.mul(1600*3f);
				//dir.set(body.getLinearVelocity()); // todo - add our speed/dir

				Bullet bullet = new Bullet(main, this, this.body.getWorldCenter().add(startOffset), force);
				main.addEntity(bullet);
				lastShotTime = System.currentTimeMillis();
			}
		}

	}


	@Override
	public void draw(Graphics g, DrawingSystem system) {
		system.drawShape(tmpPoint, g, body);
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
		if (isJetting && jetTimer.hasHit(interpol)) {
			Particle p = new Particle(main, this.getPosition());
			main.addEntity(p);
		}

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
		main.removeEntity(this);
		
	}



}
