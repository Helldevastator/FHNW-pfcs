package ch.fhnw.pfcs.janfaessler.assignment_5;
import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;
import com.sun.prism.paint.Color;
 
 
public class Particle {
    
    private static final Color color = Color.BLUE;
    private static final double RADIUS = 0.1;
    
    private double x = 0;
    private double y = 0;
    private double speed = 0.2;

    public Particle (double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void draw(GL2 gl) {
        gl.glColor3d(color.getRed(), color.getGreen(), color.getBlue());
        DrawUtils.drawCircle(gl, RADIUS, x, y);
    }
    
    public void move(Dynamics d) {
        double[] v = {x, y};
        double[] p = d.move(v, speed);
        x = p[0];
        y = p[1];
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
}