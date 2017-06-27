package com.scs.physicsspacewar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import org.gamepad4j.Controllers;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import ssmith.awt.ImageCache;
import ssmith.util.TSArrayList;

import com.scs.physicsspacewar.entity.Entity;
import com.scs.physicsspacewar.entity.PlayersShip;
import com.scs.physicsspacewar.entity.Starfield;
import com.scs.physicsspacewar.entity.components.IAffectedByGravity;
import com.scs.physicsspacewar.entity.components.ICausesGravity;
import com.scs.physicsspacewar.entity.components.ICollideable;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.components.IPlayerControllable;
import com.scs.physicsspacewar.entity.components.IProcessable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;
import com.scs.physicsspacewar.entity.systems.PlayerInputSystem;
import com.scs.physicsspacewar.input.DeviceThread;
import com.scs.physicsspacewar.input.IInputDevice;
import com.scs.physicsspacewar.input.NewControllerListener;
import com.scs.physicsspacewar.map.AbstractMap;
import com.scs.physicsspacewar.map.TestMap;

public class Main implements ContactListener, NewControllerListener, KeyListener {

	public World world;
	public MainWindow window;

	private TSArrayList<Entity> entities;
	private TSArrayList<ICausesGravity> gravityCausers;
	private List<IInputDevice> newControllers = new ArrayList<>();
	private List<Player> players = new ArrayList<>();

	private DrawingSystem drawingSystem;
	private PlayerInputSystem playerInputSystem;
	private List<Contact> collisions = new LinkedList<>();
	private AbstractMap level;
	private boolean restartLevel = false;
	private int levelNum = 1;


	public static void main(String[] args) {
		new Main();
	}


	public Main() {
		super();

		window = new MainWindow(this);

		try {
			DeviceThread dt = new DeviceThread(window);
			dt.addListener(this);
			dt.start();

			Statics.img_cache = ImageCache.GetInstance(null);
			Statics.img_cache.c = window;

			drawingSystem = new DrawingSystem();
			playerInputSystem = new PlayerInputSystem();

			startLevel();
			this.gameLoop();

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(window, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	private void gameLoop() {
		long interpol = 30;
		final float timeStep = 1.0f / Statics.FPS;//10.f;
		final int velocityIterations = 6;//8;//6;
		final int positionIterations = 4;//3;//2;

		while (window.isVisible()) {
			synchronized (newControllers) {
				while (this.newControllers.isEmpty() == false) {
					this.loadPlayer(this.newControllers.remove(0));
				}
			}

			this.entities.refresh();
			this.gravityCausers.refresh();

			boolean anyAvatars = false;

			// Preprocess
			for (Entity e : this.entities) {
				if (e instanceof IProcessable) {
					IProcessable id = (IProcessable)e;
					id.preprocess(interpol);
				}
				if (e instanceof IAffectedByGravity) {
					IAffectedByGravity affected = (IAffectedByGravity)e;
					for (ICausesGravity cg : this.gravityCausers) {
						if (e != cg) {
							Vec2 dir = cg.getPosition().sub(affected.getPosition());
							float dist = (float)Math.sqrt(dir.length()*cg.getMass());
							//affected.applyLinearImpulse(dir.mul(1/dist));
							//float force = 10/dist;
							float force = (float)Math.pow(dist, -2);
							force = force * (affected.getMass() * cg.getMass());  // adjust by mass?
							affected.applyForceToCenter(dir.mul(force));
						}
					}
				}
			}

			// Player input first
			if (DeviceThread.USE_CONTROLLERS) {
				Controllers.checkControllers();
			}
			for (Entity e : this.entities) {
				if (e instanceof IPlayerControllable) {
					IPlayerControllable id = (IPlayerControllable)e;
					this.playerInputSystem.process(id);
					anyAvatars = true;
					this.drawingSystem.cam_centre = id.getPosition(); // todo - get average positions for multiple players
				}
			}

			collisions.clear();
			world.step(timeStep, velocityIterations, positionIterations);
			while (collisions.isEmpty() == false) {
				processCollision(collisions.remove(0));
			}

			// Draw screen
			Graphics g = window.BS.getDrawGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, Statics.WINDOW_WIDTH, Statics.WINDOW_HEIGHT);

			g.setColor(Color.white);
			//g.drawString("Press ESC to Restart", 20, 50);

			for (Entity e : this.entities) {
				if (e instanceof IDrawable) {
					IDrawable sprite = (IDrawable)e;
					sprite.draw(g, drawingSystem);
				}
				if (e instanceof IProcessable) {
					IProcessable id = (IProcessable)e;
					id.postprocess(interpol);
				}
			}

			if (this.players.size() > 0 && anyAvatars == false) {
				this.nextLevel();
			} else if (restartLevel) {
				restartLevel = false;
				this.startLevel();
			}

			window.BS.show();

			try {
				interpol = 1000/Statics.FPS;
				Thread.sleep(interpol); // todo - calc start-end diff
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.exit(0);
	}


	private void startLevel() {
		this.entities = new TSArrayList<Entity>();
		this.gravityCausers = new TSArrayList<ICausesGravity>();

		Vec2 gravity = new Vec2(0f, 0f);
		world = new World(gravity);
		world.setContactListener(this);
		
		this.addEntity(new Starfield(this));

		level = new TestMap(this);//   AbstractLevel.GetLevel(levelNum, this);//new Level3(this);// 
		level.createWorld(world, this);
		this.addEntity(level);

		// Create avatars
		for (Player player : this.players) {
			this.createAvatar(player);
		}

	}


	/*private void launchRock() {
		BodyUserData bud = new BodyUserData("Rock", BodyUserData.Type.Rock, Color.gray);
		Body rock = JBox2DFunctions.AddCircle(bud, world, 60, 10, 1, BodyType.DYNAMIC, .4f, 1f, .1f);
		DrawableBody db = new DrawableBody(rock);
		bud.entity = db;

		this.entities.add(db);

		int offx = Statics.rnd.nextInt(70)+35;
		int offy = Statics.rnd.nextInt(50);
		Vec2 force = new Vec2(-offx, -offy);
		rock.applyForceToCenter(force);
	}*/


	private void processCollision(Contact contact) {
		Body ba = contact.getFixtureA().getBody();
		Body bb = contact.getFixtureB().getBody();

		BodyUserData ba_ud = (BodyUserData)ba.getUserData();
		BodyUserData bb_ud = (BodyUserData)bb.getUserData();

		//Statics.p("BeginContact BodyUserData A:" + ba_ud);
		//Statics.p("BeginContact BodyUserData B:" + bb_ud);

		if (ba_ud != null && bb_ud != null) {
			Entity entityA = ba_ud.entity;
			Entity entityB = bb_ud.entity;

			//Statics.p("BeginContact Entity A:" + entityA);
			//Statics.p("BeginContact Entity B:" + entityB);

			if (entityA instanceof ICollideable) {
				ICollideable ic = (ICollideable) entityA;
				ic.collided(entityB);
			} else if (entityB instanceof ICollideable) {
				ICollideable ic = (ICollideable) entityB;
				ic.collided(entityA);
			}

		}

	}


	@Override
	public void beginContact(Contact contact) {
		//Statics.p("Generic collision");

		/*Body ba = contact.getFixtureA().getBody();
		Body bb = contact.getFixtureB().getBody();

		BodyUserData ba_ud = (BodyUserData)ba.getUserData();
		BodyUserData bb_ud = (BodyUserData)bb.getUserData();

		//Statics.p("BeginContact BodyUserData A:" + ba_ud);
		//Statics.p("BeginContact BodyUserData B:" + bb_ud);

		Entity entityA = ba_ud.entity;
		Entity entityB = bb_ud.entity;

		//Statics.p("BeginContact Entity A:" + entityA);
		//Statics.p("BeginContact Entity B:" + entityB);

		//if (entityA)*/

		this.collisions.add(contact);

	}


	@Override
	public void endContact(Contact contact) {
		//p("EndContact");

	}


	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}


	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}



	public void removeEntity(Entity b) {
		b.cleanup(world);
		/*if (b instanceof PhysicalEntity) {
			PhysicalEntity pe = (PhysicalEntity)b;
			world.destroyBody(pe.body);
		}*/

		synchronized (entities) {
			this.entities.remove(b);
		}		
		if (b instanceof ICausesGravity) {
			ICausesGravity cg = (ICausesGravity)b;
			this.gravityCausers.remove(cg);
		}
	}


	@Override
	public void newController(IInputDevice input) {
		synchronized (newControllers) {
			this.newControllers.add(input);
		}
	}


	private void loadPlayer(IInputDevice input) {
		Player player = new Player(input);
		synchronized (players) {
			this.players.add(player);
		}
		this.createAvatar(player);

	}


	private void createAvatar(Player player) {
		Point p = this.level.getPlayerStartPos(player.id);
		PlayersShip avatar = new PlayersShip(player, this, world, p.x, p.y);
		this.addEntity(avatar);
	}


	public void restartAvatar(PlayersShip avatar) {
		Point p = this.level.getPlayerStartPos(avatar.id);
		avatar.body.setTransform(new Vec2(p.x, p.y), 0);
	}


	public void nextLevel() {
		levelNum++;
		this.startLevel();
	}


	public void addEntity(Entity o) {
		synchronized (entities) {
			if (Statics.DEBUG) {
				if (this.entities.contains(o)) {
					throw new RuntimeException(o + " has already been added");
				}
			}
			this.entities.add(o);
			if (o instanceof ICausesGravity) {
				ICausesGravity cg = (ICausesGravity)o;
				this.gravityCausers.add(cg);
			}
		}
	}


	@Override
	public void keyPressed(KeyEvent arg0) {

	}


	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
			//this.setVisible(false);
			//this.dispose();
			restartLevel = true;
			//startLevel();
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}
}

