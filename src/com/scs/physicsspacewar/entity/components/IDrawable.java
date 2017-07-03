package com.scs.physicsspacewar.entity.components;

import java.awt.Graphics;

import com.scs.physicsspacewar.entity.systems.DrawingSystem;

public interface IDrawable {

	void draw(Graphics g, DrawingSystem system);
}
