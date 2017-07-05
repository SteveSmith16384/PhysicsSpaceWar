package com.scs.physicsspacewar.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import com.scs.physicsspacewar.Main_SpaceWar;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.components.IProcessable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public class Particle extends Entity implements IDrawable, IProcessable {

	private static final int DURATION = 3000;
	
	private Stroke stroke = new BasicStroke(1);

	private long timeLeft = DURATION;
	private Point tmp = new Point();
	private Vec2 worldpos;
	
	public Particle(Main_SpaceWar _main, Vec2 pos) {
		super(_main, "Particle");
		
		worldpos = pos.clone();//new Vec2(x, y);
	}
	

	@Override
	public void draw(Graphics g, DrawingSystem system) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(stroke);
		system.drawDot(tmp, g, worldpos, Color.LIGHT_GRAY);
		
	}

	@Override
	public void cleanup(World world) {
		// Do nothing
	}

	
	@Override
	public void preprocess(long interpol) {
		timeLeft -= interpol;
		if (timeLeft < 0) {
			main.removeEntity(this);
		}
		
	}

	
	@Override
	public void postprocess(long interpol) {
		
	}

}
