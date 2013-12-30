package ch.fhnw.pfcs.janfaessler.assignment_2;

import java.awt.geom.Point2D;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;

public class DrawUtils {
	
	private static final double g = 9.81;    // gravity
	private static final double p = 1.2929;  // (kg/m3) Normdichte
	private static final double dt = 0.02;  // time step  (0.05) 
	
	private static int circlePoints = 40; 								  // Anzahl Punkte
	private static double circleStepSize = 2.0 * Math.PI / circlePoints;  // Parameter-Schrittweite
	
	
	 
	public static double getAirDensity() { return p; }
	public static double getGravity() { return g; }
	public static double getTimeStep() { return dt; }
	
	
    public static void drawAxes(GL2 gl) { 
    	drawLine(gl, new Point2D.Double(-100,    0), new Point2D.Double(100,   0));
    	drawLine(gl, new Point2D.Double(   0, -100), new Point2D.Double(  0, 100));
    } 
    
	public static void drawLine(GL2 gl, Point2D.Double start, Point2D.Double end) {
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2d(start.x, start.y);
		gl.glVertex2d(end.x,     end.y);
		gl.glEnd();
	}
  
    public static void drawCircle(GL2 gl, double r, double x, double y) { 
		gl.glBegin(GL2.GL_POLYGON);
		for (int i = 0; i < circlePoints; i++)
			gl.glVertex2d(x + r * Math.cos(i * circleStepSize), 
						  y + r * Math.sin(i * circleStepSize));
		gl.glEnd();
    } 
    
    public static void drawAngle(GL2 gl, Point2D.Double pos, double angle, double size) {
    	drawLine(gl, new Point2D.Double(pos.x,pos.y - size), new Point2D.Double(pos.x, pos.y+size));
    	drawLine(gl, new Point2D.Double(pos.x - size, pos.y), new Point2D.Double(pos.x+size, pos.y));
        drawLine(gl, pos, new Point2D.Double(pos.x + size * Math.cos(Math.toRadians(angle)), pos.y + size * Math.sin(Math.toRadians(angle))));
    	gl.glBegin(GL.GL_LINE_STRIP);
        double dt = Math.toRadians(angle) / 10;
        for (int i = 0; i < 10; i++) {
        	gl.glVertex2d(pos.x + size * Math.cos(i * dt), pos.y + size * Math.sin(i * dt));
        }
        gl.glVertex2d(pos.x + size * Math.cos(Math.toRadians(angle)), pos.y + size * Math.sin(Math.toRadians(angle)));
        gl.glEnd();
        
    }
}
