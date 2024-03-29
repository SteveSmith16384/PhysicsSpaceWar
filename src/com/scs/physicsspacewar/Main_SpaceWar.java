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

import com.scs.physicsspacewar.entity.Entity;
import com.scs.physicsspacewar.entity.HomingMissile;
import com.scs.physicsspacewar.entity.PlayersShip;
import com.scs.physicsspacewar.entity.Starfield;
import com.scs.physicsspacewar.entity.components.IAffectedByGravity;
import com.scs.physicsspacewar.entity.components.ICausesGravity;
import com.scs.physicsspacewar.entity.components.ICollideable;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.components.IGetPosition;
import com.scs.physicsspacewar.entity.components.IPlayerControllable;
import com.scs.physicsspacewar.entity.components.IProcessable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;
import com.scs.physicsspacewar.entity.systems.PlayerInputSystem;
import com.scs.physicsspacewar.input.DeviceThread;
import com.scs.physicsspacewar.input.IInputDevice;
import com.scs.physicsspacewar.input.NewControllerListener;
import com.scs.physicsspacewar.map.AbstractMap;
import com.scs.physicsspacewar.map.GravityWars_Cross;
import com.scs.physicsspacewar.map.LotsOfMoons;
import com.scs.physicsspacewar.map.PlanetRandom;
import com.scs.physicsspacewar.map.SolarSystem;

import ssmith.awt.ImageCache;
import ssmith.util.TSArrayList;

public class Main_SpaceWar implements ContactListener, NewControllerListener, KeyListener {

	public World world;
	public MainWindow window;

	private TSArrayList<Entity> entities;
	private List<Entity> playerShips;
	private TSArrayList<ICausesGravity> gravityCausers;
	private List<IInputDevice> newControllers = new ArrayList<>();
	private List<Player> players = new ArrayList<>();

	private DrawingSystem drawingSystem;
	private PlayerInputSystem playerInputSystem;
	private List<Contact> collisions = new LinkedList<>();
	private AbstractMap map;
	private boolean restartLevel = false;

	private int gameStage = -1;
	private int levelID;
	private int newLevel = -1;
	
	public static void main(String[] args) {
		new Main_SpaceWar();
	}


	public Main_SpaceWar() {
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

			//startLevel();
			this.gameLoop();

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(window, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	private void startLevel(int level) {
		levelID = level;
		this.gameStage = 0;
		this.startLevel();
	}
	
	
	private void startLevel() {
		this.entities = new TSArrayList<Entity>();
		this.gravityCausers = new TSArrayList<ICausesGravity>();
		playerShips = new ArrayList<Entity>();

		switch (levelID) {
		case 1:
			map = new SolarSystem(this);
			break;
		case 2:
			map = new PlanetRandom(this);
			break;
		case 3:
			map = new GravityWars_Cross(this);
			break;
		case 4:
			map = new LotsOfMoons(this);
			break;
		default:
			map = new LotsOfMoons(this);
		}

		world = new World(map.getGravity());
		world.setContactListener(this);

		this.addEntity(new Starfield(this));

		map.createWorld(world, this);
		this.addEntity(map);

		// Create avatars
		for (Player player : this.players) {
			this.createAvatar(player);
		}

	}


	private void gameLoop() {
		long interpol = 30;
		final float timeStep = 1.0f / Statics.FPS;//10.f;
		final int velocityIterations = 6;//8;//6;
		final int positionIterations = 4;//3;//2;

		while (window.isVisible()) {
			long start = System.currentTimeMillis();

			Graphics g = window.BS.getDrawGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, this.window.getWidth(), this.window.getHeight());

			if (gameStage == -1) {
				drawMainMenu(g);
			} else {
				synchronized (newControllers) {
					while (this.newControllers.isEmpty() == false) {
						this.loadPlayer(this.newControllers.remove(0));
					}
				}
				
				if (newLevel >= 0) {
					this.startLevel(newLevel);
					newLevel = -1;
				}

				this.entities.refresh();
				this.gravityCausers.refresh();

				// Preprocess
				for (Entity e : this.entities) {
					if (e instanceof IProcessable) {
						IProcessable id = (IProcessable)e;
						id.preprocess(interpol);
					}
					// Positional gravity
					if (e instanceof IAffectedByGravity) {
						IAffectedByGravity affected = (IAffectedByGravity)e;
						for (ICausesGravity cg : this.gravityCausers) {
							if (e != cg) {
								Vec2 dir = cg.getPosition().sub(affected.getPosition());
								float dist = (float)Math.sqrt(dir.length()*cg.getMass());
								float force = (float)Math.pow(dist, -2);
								force = force * (affected.getMass() * cg.getMass()) * 2; // adjust by mass
								affected.applyForceToCenter(dir.mul(force));
							}
						}
					}
				}

				// Player input first
				if (DeviceThread.USE_CONTROLLERS) {
					Controllers.checkControllers();
				}

				Vec2 newCamPos = new Vec2();
				for (Entity e : this.playerShips) {
					IPlayerControllable id = (IPlayerControllable)e;
					this.playerInputSystem.process(id);
					newCamPos.x += id.getPosition().x;
					newCamPos.y += id.getPosition().y;
				}
				if (playerShips.size() > 0) {
					newCamPos.x /= playerShips.size();
					newCamPos.y /= playerShips.size();

					this.drawingSystem.cam_centre_logical.x = newCamPos.x;
					this.drawingSystem.cam_centre_logical.y = newCamPos.y;
				} else {
					// Default to centre
					this.drawingSystem.cam_centre_logical.x = Statics.WORLD_WIDTH_LOGICAL/2;
					this.drawingSystem.cam_centre_logical.y = Statics.WORLD_HEIGHT_LOGICAL/2;
				}

				collisions.clear();
				world.step(timeStep, velocityIterations, positionIterations);
				while (collisions.isEmpty() == false) {
					processCollision(collisions.remove(0));
				}

				// Draw screen
				g.setColor(Color.white);
				g.drawString("Press ESC to Restart", 20, 50);
				g.drawString("Num Entities: " + this.entities.size(), 20, 70);
				g.drawString("Zoom: " + this.drawingSystem.currZoom, 20, 90);

				drawingSystem.startOfDrawing(g);
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
				drawingSystem.endOfDrawing();

				if (this.players.size() > 0 && playerShips.isEmpty()) {
					this.startLevel();
				} else if (restartLevel) {
					restartLevel = false;
					this.startLevel();
				}
			}

			window.BS.show();

			long end = System.currentTimeMillis();
			long diff = end-start;
			try {
				interpol = (1000/Statics.FPS) - diff;
				if (interpol > 0) {
					Thread.sleep(interpol);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.exit(0);
	}


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

			Statics.p("BeginContact Entity A:" + entityA);
			Statics.p("BeginContact Entity B:" + entityB);

			if (entityA instanceof ICollideable) {
				ICollideable ic = (ICollideable) entityA;
				ic.collided(entityB);
			}
			if (entityB instanceof ICollideable) {
				ICollideable ic = (ICollideable) entityB;
				ic.collided(entityA);
			}

		}

	}


	@Override
	public void beginContact(Contact contact) {
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

		synchronized (entities) {
			this.entities.remove(b);
		}		

		if (b instanceof ICausesGravity) {
			ICausesGravity cg = (ICausesGravity)b;
			this.gravityCausers.remove(cg);
		}

		if (b instanceof IPlayerControllable) {
			this.playerShips.remove(b);
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
		Point p = this.map.getPlayerStartPos(player.id_ZB);
		PlayersShip avatar = new PlayersShip(player, this, world, p.x, p.y);
		this.addEntity(avatar);
	}


	public void restartAvatar(PlayersShip avatar) {
		Point p = this.map.getPlayerStartPos(avatar.id);
		avatar.body.setTransform(new Vec2(p.x, p.y), 0);
	}


	public void addEntity(Entity o) {
		synchronized (entities) {
			if (Statics.DEBUG) {
				/*if (this.entities.contains(o)) {
					throw new RuntimeException(o + " has already been added");
				}*/
			}
			this.entities.add(o);
			if (o instanceof ICausesGravity) {
				ICausesGravity cg = (ICausesGravity)o;
				this.gravityCausers.add(cg);
			}
			if (o instanceof IPlayerControllable) {
				this.playerShips.add(o);
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
			//restartLevel = true;
			//startLevel();
			this.gameStage = -1;
		} else if (ke.getKeyCode() >= KeyEvent.VK_1 && ke.getKeyCode() <= KeyEvent.VK_5) {
			newLevel = ke.getKeyCode() - KeyEvent.VK_1 + 1;
			this.gameStage = 0;
		} else if (ke.getKeyCode() == KeyEvent.VK_T) {
			IGetPosition ig = (IGetPosition)this.playerShips.get(0);
			Vec2 pos = new Vec2();
			pos.y += 50;
			HomingMissile m = new HomingMissile(this, pos, ig);
			this.addEntity(m);
			Statics.p("Homing missile");
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		//  Who uses this?!
	}


	@Override
	public void controllerRemoved(IInputDevice input) {
		for (Player player : this.players) {
			if (player.input == input) {
				this.players.remove(player);
				break;
			}
		}
		for (Entity e : this.entities) {
			if (e instanceof PlayersShip) {
				this.removeEntity(e);
				//break;
			}
		}

	}


	private void drawMainMenu(Graphics g) {
		g.setColor(Color.white);
		int x = 20;
		int y = 60;
		int yInc = 20;
		g.drawString("PLEASE SELECT A STAR SYSTEM", x, y);
		y += yInc;
		g.drawString("1 - Solar System", x, y);
		y += yInc;
		g.drawString("2 - Planet Surface", x, y);
		y += yInc;
		g.drawString("3 - The Cross", x, y);
		y += yInc;
		g.drawString("4 - Lots of Moons", x, y);
		y += yInc;
	}
}

