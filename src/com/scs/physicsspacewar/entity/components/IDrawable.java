package com.scs.physicsspacewar.entity.components;

import java.awt.Graphics;

import org.jbox2d.common.Vec2;

import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public interface IDrawable {

	void draw(Graphics g, DrawingSystem system);
}
