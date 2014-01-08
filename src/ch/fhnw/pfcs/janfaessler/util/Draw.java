package ch.fhnw.pfcs.janfaessler.util;

import java.awt.geom.Point2D;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;

public class Draw {

    private static final double g = 9.81;    // gravity
    private static final double p = 1.2929;  // (kg/m3) Normdichte
    private static final double dt = 0.05;  // time step  (0.05) 
	
    public static double getAirDensity() { return p; }
    public static double getGravity() { return g; }
    public static double getTimeStep() { return dt; }
        
    public static void line2d(GL2 gl, Vec2 start, Vec2 end) {
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2d(start.x, start.y);
        gl.glVertex2d(end.x,     end.y);
        gl.glEnd();
    }
    
    public static void line2d(GL2 gl, double s) {
        line2d(gl, new Vec2(0,0), new Vec2(s, 0));
    }
    
    public static void line3d(GL2 gl, Vec3 start, Vec3 end) {
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3d(start.x, start.y, start.z);
        gl.glVertex3d(end.x,     end.y, end.z);
        gl.glEnd();
    }

    public static void axes2d(GL2 gl) { 
    	line2d(gl, new Vec2(-100,    0), new Vec2(100,   0));
    	line2d(gl, new Vec2(   0, -100), new Vec2(  0, 100));
    } 
    public static void axes3d(GL2 gl) {
        line3d(gl, new Vec3(0, 0, 0), new Vec3(100,0,0));
        line3d(gl, new Vec3(0, 0, 0), new Vec3(0,100,0));
        line3d(gl, new Vec3(0, 0, 0), new Vec3(0,0,100));
    }

    public static void rect2d(GL2 gl, Point2D.Double p1, Point2D.Double p2, boolean fill) {
        gl.glBegin(fill ? GL2.GL_POLYGON : GL2.GL_LINE_LOOP);
        gl.glVertex2d(p1.x, p1.y);
        gl.glVertex2d(p2.x, p1.y);
        gl.glVertex2d(p2.x, p2.y);
        gl.glVertex2d(p1.x, p2.y);
        gl.glEnd();
    }
    
    public static void angle(GL2 gl, Vec2 pos, double angle, double size) {
    	line2d(gl, new Vec2(pos.x,pos.y - size), new Vec2(pos.x, pos.y+size));
    	line2d(gl, new Vec2(pos.x - size, pos.y), new Vec2(pos.x+size, pos.y));
        line2d(gl, pos, new Vec2(pos.x + size * Math.cos(Math.toRadians(angle)), pos.y + size * Math.sin(Math.toRadians(angle))));
    	gl.glBegin(GL.GL_LINE_STRIP);
        double dt = Math.toRadians(angle) / 10;
        for (int i = 0; i < 10; i++) {
        	gl.glVertex2d(pos.x + size * Math.cos(i * dt), pos.y + size * Math.sin(i * dt));
        }
        gl.glVertex2d(pos.x + size * Math.cos(Math.toRadians(angle)), pos.y + size * Math.sin(Math.toRadians(angle)));
        gl.glEnd();
    }
    
    public static void smallCircle2d(GL2 gl, double r, double x, double y) {
        circle2d(gl, r, x, y, 5, true);
    }
    
    public static void smallCircle2d(GL2 gl, double r, double x, double y, boolean fill) {
        circle2d(gl, r, x, y, 5, fill);
    }
    
    public static void circle2d(GL2 gl, double r, double x, double y) {
        circle2d(gl, r, x, y, 40, true);
    }
    
    public static void circle2d(GL2 gl, double r, double x, double y, boolean fill) {
        circle2d(gl, r, x, y, 40, fill);
    }
    
    public static void circle2d(GL2 gl, double r, double x, double y, int circlePoints, boolean fill) { 
        double circleStepSize = 2.0 * Math.PI / circlePoints;
        gl.glBegin(fill ? gl.GL_POLYGON : gl.GL_LINE_LOOP);
        for (int i = 0; i < circlePoints; i++)
            gl.glVertex2d(x + r * Math.cos(i * circleStepSize), y + r * Math.sin(i * circleStepSize));
        gl.glEnd();
    } 
    
    public static void quad3d(GL2 gl, Vec3 A, Vec3 B, Vec3 C, Vec3 D) {
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3d(A.x, A.y, A.z);
        gl.glVertex3d(B.x, B.y, B.z);
        gl.glVertex3d(C.x, C.y, C.z);
        gl.glVertex3d(D.x, D.y, D.z);
        gl.glEnd();
    }
    
}
