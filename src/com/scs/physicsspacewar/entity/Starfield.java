package com.scs.physicsspacewar.entity;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import com.scs.physicsspacewar.Main;
import com.scs.physicsspacewar.entity.components.IDrawable;
import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public class Starfield extends Entity implements IDrawable {

	private static final int SPACING = 20;

	private Vec2 prevCamPos = new Vec2();

	public Starfield(Main _main) {
		super(_main, "Starfield");
	}


	@Override
	public void draw(Graphics g, DrawingSystem system) {
		Vec2 offset = this.prevCamPos.sub(system.cam_centre);

		g.setColor(Color.LIGHT_GRAY);
		for (int y=0 ; y<main.window.getHeight() ; y+= SPACING) {
			for (int x=0 ; x<main.window.getWidth() ; x+= SPACING) {
				int offx = (int)offset.x % SPACING;
				int offy = (int)offset.y % SPACING;
				g.drawRect(x+offx, y+offy, 2, 2);
			}			
		}

		prevCamPos.set(system.cam_centre);

	}


	@Override
	public void cleanup(World world) {

	}


}

