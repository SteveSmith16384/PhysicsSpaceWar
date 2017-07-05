package com.scs.physicsspacewar.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import ssmith.util.Timer;

import com.scs.physicsspacewar.BodyUserData;
import com.scs.physicsspacewar.JBox2DFunctions;
import com.scs.physicsspacewar.Main_SpaceWar;
import com.scs.physicsspacewar.Player;
import com.scs.physicsspacewar.Statics;
import com.scs.physicsspacewar.entity.components.IAffectedByGravity;
import com.scs.physicsspacewar.entity.components.ICanShoot;
import com.scs.physicsspacewar.entity.components.ICollideable;
import com.scs.physicsspacewar.entity.components.IDamagable;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.components.IPlayerControllable;
import com.scs.physicsspacewar.entity.components.IProcessable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;
import com.scs.physicsspacewar.input.IInputDevice;

public class PlayersShip extends PhysicalEntity implements IPlayerControllable, IDrawable, ICollideable, IProcessable, IAffectedByGravity, IDamagable, ICanShoot {

	private static final float TURN_TORQUE = 80*8f;
	private static final float TURN_SLOWDOWN_TORQUE = 80f;
	private static final float MAX_ANGLE_VEL = 4f;
	private static final float JET_FORCE = 70*15f;
	private static final float MAX_VELOCITY = 30f;

	private static final int SHOT_INT = 500;
	public static final float SIZE = 4f;

	public int id;
	private IInputDevice input;
	private long lastShotTime;
	private Timer jetTimer = new Timer(100);
	private boolean isJetting = false;
	public int health = 1;
	private Stroke stroke = new BasicStroke(1);
	private Color col;

	public PlayersShip(Player player, Main_SpaceWar main, World world, float x, float y) {
		super(main, PlayersShip.class.getSimpleName());

		input = player.input;
		id = player.id_ZB;

		col = getColour(player.id_ZB);
		
		BodyUserData bud = new BodyUserData("Player_Body", col, this);
		Vec2[] vertices = new Vec2[3];
		vertices[0] = new Vec2(SIZE/3, 0); // middle-top
		vertices[1] = new Vec2(SIZE*.66f, SIZE); // bottom-right
		vertices[2] = new Vec2(0, SIZE); // bottom-left
		body = JBox2DFunctions.CreateComplexShape(bud, world, vertices, BodyType.DYNAMIC, .2f, .3f, 10f);
		body.setTransform(new Vec2(x, y), 1);
		body.setBullet(true);

	}


	protected static Color getColour(int i) {
		switch (i) {
		case 0: return Color.magenta;
		case 1: return Color.green;
		case 2: return Color.cyan;
		case 3: return Color.orange;
		default: return Color.pink;
		}
	}



	@Override
	public void processInput() {
		if (input.isLeftPressed()) {
			if (body.getAngularVelocity() > -MAX_ANGLE_VEL) {
				body.applyTorque(-TURN_TORQUE);
			}
			//Statics.p("Ang vel: " + body.getAngularVelocity() );
		} else if (input.isRightPressed()) {
			if (body.getAngularVelocity() < MAX_ANGLE_VEL) {
				body.applyTorque(TURN_TORQUE);		
			}
			//Statics.p("Ang vel: " + body.getAngularVelocity() );
		} else {
			// Slowdown
			if (body.getAngularVelocity() > 0) {
				body.applyTorque(-TURN_SLOWDOWN_TORQUE);
			} else if (body.getAngularVelocity() < 0) {
				body.applyTorque(TURN_SLOWDOWN_TORQUE);
			}
		}

		if (input.isThrustPressed()) {
			isJetting = true;
			Vec2 force = new Vec2();
			force.y = (float)Math.cos(body.getAngle()) * -1;
			force.x = (float)Math.sin(body.getAngle());
			force.mulLocal(JET_FORCE);
			body.applyForceToCenter(force);
		} else {
			isJetting = false;
		}

		Vec2 vel = body.getLinearVelocity();
		// cap max velocity on x		
		if(Math.abs(vel.x) > MAX_VELOCITY) {			
			vel.x = Math.signum(vel.x) * MAX_VELOCITY;
			Statics.p("Max speed!");
			body.setLinearVelocity(vel);
		}

		if (input.isFirePressed()) {
			if (System.currentTimeMillis() > lastShotTime + SHOT_INT) {
				float dirx = (float)Math.sin(this.body.getAngle());
				float diry = (float)Math.cos(this.body.getAngle());
				Vec2 dir = new Vec2(dirx, diry*-1);

				Vec2 startOffset = dir.clone();
				startOffset.normalize();
				startOffset.mulLocal(SIZE*2);

				Vec2 force = dir.mul(1600*1f);
				//dir.set(body.getLinearVelocity()); // todo - add our speed/dir

				Bullet bullet = new Bullet(main, this, this.body.getWorldCenter().add(startOffset), force);
				main.addEntity(bullet);
				lastShotTime = System.currentTimeMillis();
			}
		}

	}


	@Override
	public void draw(Graphics g, DrawingSystem system) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(stroke);
		system.drawShape(tmpPoint, g, body, true);
	}


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
		System.out.println("Here:" + them);
	}


	@Override
	public void damage(int amt) {
		this.health -= amt;
		Statics.p("Player damaged " + amt);
		if (this.health <= 0) {
			main.removeEntity(this);
		}
	}


	@Override
	public Color getBulletColor() {
		return col;
	}


}
