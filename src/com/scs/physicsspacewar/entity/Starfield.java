package com.scs.physicsspacewar.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.dynamics.World;

import ssmith.lang.Functions;

import com.scs.physicsspacewar.Main;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public class Starfield extends Entity implements IDrawable {

	public Starfield(Main _main) {
		super(_main, "Starfield");
	}


	@Override
	public void draw(Graphics g, DrawingSystem system) {
		//Vec2 offset = this.prevCamPos.sub(system.cam_centre);
		int spacing = (int)(system.currZoom * 30);
		g.setColor(Color.LIGHT_GRAY);
		int sx = (int)system.cam_centre_logical.x % spacing;
		int sy = (int)system.cam_centre_logical.y % spacing;
		for (int y=-sy ; y<main.window.getHeight() ; y+= spacing) {
			for (int x=-sx ; x<main.window.getWidth() ; x+= spacing) {
				//int offx = (int)system.cam_centre_logical.x % spacing;
				//int offy = (int)system.cam_centre_logical.y % spacing;
				g.drawRect(x, y, Functions.rnd(1, 2), Functions.rnd(1, 2));
			}			
		}

		//prevCamPos.set(system.cam_centre);

	}


	@Override
	public void cleanup(World world) {

	}


}

