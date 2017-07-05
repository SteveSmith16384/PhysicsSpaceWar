package com.scs.physicsspacewar.entity.systems;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import com.scs.physicsspacewar.BodyUserData;
import com.scs.physicsspacewar.Statics;

public class DrawingSystem {

	private static final float OUTER = 0.2f;
	private static final float INNER = 0.22f; // 0.3f
	private static final float ZOOM_IN_SPEED = 1.01f;
	private static final float ZOOM_OUT_SPEED = .99f;
	private static final float MAX_ZOOM_IN = 2.5f;
	private static final float MAX_ZOOM_OUT = 0.5f;

	public float currZoom = (MAX_ZOOM_IN+MAX_ZOOM_OUT)/2;
	public Vec2 cam_centre_logical = new Vec2();
	private boolean zoomIn, zoomOut;

	// for drawing edges
	private Point tmp1 = new Point();
	private Point tmp2 = new Point();

	public DrawingSystem() {
	}


	public void startOfDrawing(Graphics g) {
		zoomIn = false;
		zoomOut = false;

		// Draw edges
		g.setColor(Color.darkGray);
		// Left
		this.getPixelPos(tmp1, new Vec2());
		this.getPixelPos(tmp2, new Vec2(0, Statics.WORLD_HEIGHT_LOGICAL));
		g.drawLine(tmp1.x, tmp1.y, tmp2.x, tmp2.y);

		// Top
		this.getPixelPos(tmp1, new Vec2());
		this.getPixelPos(tmp2, new Vec2(Statics.WORLD_WIDTH_LOGICAL, 0));
		g.drawLine(tmp1.x, tmp1.y, tmp2.x, tmp2.y);

		// Right
		this.getPixelPos(tmp1, new Vec2(Statics.WORLD_WIDTH_LOGICAL, 0));
		this.getPixelPos(tmp2, new Vec2(Statics.WORLD_WIDTH_LOGICAL, Statics.WORLD_HEIGHT_LOGICAL));
		g.drawLine(tmp1.x, tmp1.y, tmp2.x, tmp2.y);

		// Bottom
		this.getPixelPos(tmp1, new Vec2(0, Statics.WORLD_HEIGHT_LOGICAL));
		this.getPixelPos(tmp2, new Vec2(Statics.WORLD_HEIGHT_LOGICAL, Statics.WORLD_HEIGHT_LOGICAL));
		g.drawLine(tmp1.x, tmp1.y, tmp2.x, tmp2.y);
	}


	public void endOfDrawing() {
		if (this.zoomOut) {
			if (this.currZoom > MAX_ZOOM_OUT) {
				currZoom *= ZOOM_OUT_SPEED;
			}
		} else if (this.zoomIn) {
			if (this.currZoom < MAX_ZOOM_IN) {
				currZoom *= ZOOM_IN_SPEED;
			}
		}
	}


	public void getPixelPos(Point ret, Vec2 worldpos) {
		int x1 = (int)((worldpos.x-cam_centre_logical.x) * currZoom + (Statics.WINDOW_WIDTH/2));
		int y1 = (int)((worldpos.y-cam_centre_logical.y) * currZoom + (Statics.WINDOW_HEIGHT/2));
		ret.x = x1;
		ret.y = y1;
	}


	public void drawImage(Point tmp, BufferedImage img, Graphics g, Body b) {
		Vec2 worldpos = b.getPosition();
		getPixelPos(tmp, worldpos);
		int rad = img.getWidth()/2;
		g.drawImage(img, (int)(tmp.x-rad), (int)(tmp.y-rad), null);

	}


	public void drawDot(Point tmp, Graphics g, Vec2 worldpos, Color c) {
		g.setColor(c);
		getPixelPos(tmp, worldpos);
		g.drawRect(tmp.x, tmp.y, 1, 1);

	}


	public void drawShape(Point tmp, Graphics g, Body b, boolean mustBeOnscreen) {
		/*Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(stroke);
*/
		// Ensure within bounds of the world
		Vec2 pos = b.getWorldCenter();
		if (pos.x < 0) {
			b.setTransform(new Vec2(pos.x+Statics.WORLD_WIDTH_LOGICAL, pos.y), b.getAngle());
		} else if (pos.x > Statics.WORLD_WIDTH_LOGICAL) {
			b.setTransform(new Vec2(pos.x-Statics.WORLD_WIDTH_LOGICAL, pos.y), b.getAngle());
		}
		if (pos.y < 0) {
			b.setTransform(new Vec2(pos.x, pos.y+Statics.WORLD_HEIGHT_LOGICAL), b.getAngle());
		} else if (pos.y > Statics.WORLD_HEIGHT_LOGICAL) {
			b.setTransform(new Vec2(pos.x, pos.y-Statics.WORLD_HEIGHT_LOGICAL), b.getAngle());
		}

		if (mustBeOnscreen) {
			this.getPixelPos(tmp, b.getWorldCenter());
			zoomOut |= tmp.x < Statics.WINDOW_WIDTH * OUTER || tmp.x > Statics.WINDOW_WIDTH * (1f-OUTER) || tmp.y < Statics.WINDOW_HEIGHT * OUTER || tmp.y > Statics.WINDOW_HEIGHT * (1f-OUTER);
			zoomIn |= (tmp.x > Statics.WINDOW_WIDTH * INNER && tmp.x < Statics.WINDOW_WIDTH * (1f-INNER) && tmp.y > Statics.WINDOW_HEIGHT * INNER && tmp.y < Statics.WINDOW_HEIGHT * (1f-INNER));
		}

		Fixture f = b.getFixtureList();
		if (f == null) {
			BodyUserData bud = (BodyUserData)b.getUserData();
			Statics.p("WARNING: " + bud.name + " has no fixture");
		}
		while (f != null) {
			Color col = Color.gray;
			BodyUserData userdata = (BodyUserData)f.getUserData();
			if (userdata != null && userdata.col != null) {
				col = userdata.col;
			}
			Color darker = col.darker().darker();

			if (f.getShape() instanceof PolygonShape) {
				Polygon polygon = new Polygon();
				PolygonShape shape = (PolygonShape)f.getShape();
				for (int i=0 ; i<shape.getVertexCount() ; i++) {
					Vec2 v = shape.getVertex(i);
					getPixelPos(tmp, b.getWorldPoint(v));
					polygon.addPoint(tmp.x, tmp.y);
				}
				g.setColor(darker);
				g.fillPolygon(polygon);
				g.setColor(col);
				g.drawPolygon(polygon);

			} else if (f.getShape() instanceof EdgeShape) {
				EdgeShape shape = (EdgeShape)f.getShape();
				Vec2 prev = shape.m_vertex1;
				Vec2 v = shape.m_vertex2;
				Vec2 worldpos = b.getWorldPoint(prev);
				Point p = new Point();
				getPixelPos(p, worldpos);

				//int x1 = (int)((worldpos.x-cam_centre.x)*Statics.WORLD_TO_PIXELS);
				//int y1 = (int)((worldpos.y-cam_centre.y)*Statics.WORLD_TO_PIXELS);

				worldpos = b.getWorldPoint(v);
				//int x2 = (int)((worldpos.x-cam_centre.x)*Statics.WORLD_TO_PIXELS);
				//int y2 = (int)((worldpos.y-cam_centre.y)*Statics.WORLD_TO_PIXELS);
				getPixelPos(tmp, worldpos);

				g.setColor(col);
				g.drawLine(p.x, p.y, tmp.x, tmp.y);

				/*} else if (f.getShape() instanceof ChainShape) {
			ChainShape shape2 = (ChainShape)f.getShape();
			EdgeShape shape = new EdgeShape();
			for (int i=0 ; i<shape2.getChildCount() ; i++) {
				shape2.getChildEdge(shape, i);

				Vec2 worldpos = b.getWorldPoint(shape.m_vertex1);
				int x1 = (int)((worldpos.x-cam_centre.x)*Statics.WORLD_TO_PIXELS);
				int y1 = (int)((worldpos.y-cam_centre.y)*Statics.WORLD_TO_PIXELS);

				worldpos = b.getWorldPoint(shape.m_vertex2);
				int x2 = (int)((worldpos.x-cam_centre.x)*Statics.WORLD_TO_PIXELS);
				int y2 = (int)((worldpos.y-cam_centre.y)*Statics.WORLD_TO_PIXELS);

				g.drawLine(x1, y1, x2, y2);
			}*/

			} else if (f.getShape() instanceof CircleShape) {
				CircleShape shape2 = (CircleShape)f.getShape();
				Vec2 worldpos = b.getPosition();
				getPixelPos(tmp, worldpos);
				int rad = (int)(shape2.getRadius() * currZoom);
				if (rad < 1) {
					rad = 1;
				}
				g.setColor(darker);
				g.fillOval((int)(tmp.x-rad), (int)(tmp.y-rad), rad*2, rad*2);
				g.setColor(col);
				g.drawOval((int)(tmp.x-rad), (int)(tmp.y-rad), rad*2, rad*2);

			} else {
				throw new RuntimeException("Cannot draw " + b);
			}

			f = f.getNext();

		}
	}

	/*
	private void DrawParticles(Graphics g, World world, Vec2 cam_centre) {
		int particleCount = world.getParticleCount();// system.getParticleCount();
		if (particleCount != 0) {
			float particleRadius = world.getParticleRadius();
			Vec2[] positionBuffer = world.getParticlePositionBuffer();// system.getParticlePositionBuffer();
			//ParticleColor[] colorBuffer = null;
			drawParticles(g, positionBuffer, particleRadius, particleCount, cam_centre);
		}

	}

	private static void drawParticles(Graphics g, Vec2[] centers, float radius, int count, Vec2 cam_centre) {
		for (int i = 0; i < count; i++) {
			Vec2 center = centers[i];
			int draw_rad = (int)(radius*23f);
			g.setColor(Color.blue);
			int x = (int)(((center.x-cam_centre.x)-radius)*Statics.WORLD_TO_PIXELS);
			int y = (int)(((center.y-cam_centre.y)-radius)*Statics.WORLD_TO_PIXELS);
			g.fillOval(x, y, draw_rad, draw_rad);

		}
	}
	 */
}
