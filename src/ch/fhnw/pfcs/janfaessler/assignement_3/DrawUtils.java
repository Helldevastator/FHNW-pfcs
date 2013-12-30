package ch.fhnw.pfcs.janfaessler.assignement_3;

import java.awt.geom.Point2D;

import javax.media.opengl.GL2;

public class DrawUtils {

	private static final double dt = 0.05;  // time step  (0.05) 
	
	public static double getTimeStep() { return dt; }
	
	public static void drawLine(GL2 gl, Point2D.Double start, Point2D.Double end) {
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex2d(start.x, start.y);
		gl.glVertex2d(end.x,     end.y);
		gl.glEnd();
	}
	
	public static void drawLine(GL2 gl, double s) {
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex2d(  0, 0);
		gl.glVertex2d(  s, 0);
		gl.glEnd();
	}
	
	public static void drawRect(GL2 gl, Point2D.Double p1, Point2D.Double p2, boolean fill) {
		gl.glBegin(fill ? GL2.GL_POLYGON : GL2.GL_LINE_LOOP);
		gl.glVertex2d(p1.x, p1.y);
		gl.glVertex2d(p2.x, p1.y);
		gl.glVertex2d(p2.x, p2.y);
		gl.glVertex2d(p1.x, p2.y);
		gl.glEnd();
	}
}
