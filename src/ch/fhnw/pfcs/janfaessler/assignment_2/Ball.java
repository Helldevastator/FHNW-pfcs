package ch.fhnw.pfcs.janfaessler.assignment_2;

import ch.fhnw.pfcs.janfaessler.util.Draw;
import ch.fhnw.pfcs.janfaessler.util.Vec2;
import java.awt.geom.Point2D;

import javax.media.opengl.GL2;

public class Ball { 
	
    private final double r  = 0.034;                      // ball radius 34cm 
    private final double m  = 0.058;                      // masse (58g)
    private final double cw = 0.4;                        // wiederstandswert des k??rpers
    
    private final double zoom = 10;
    
    private final double g  = Draw.getGravity();     // gravity
    private final double dt = Draw.getTimeStep();    // time steps
    private final double p  = Draw.getAirDensity();  // luftdichte (kg/m3)
    
    private final double c  = (p / 2) * cw * ((r * r) * Math.PI);
    private final GL2 gl;							   
    
    private Vec2 position;
    private Vec2 speed;
    private int lifeTime = 0;

    public Ball(GL2 gl, Vec2 start, double speed, double angle) { 
    	this.gl = gl;
        this.position = new Vec2(start.x, start.y);
        this.speed = new Vec2(speed * Math.cos(Math.toRadians(angle)), speed * Math.sin(Math.toRadians(angle)));
    } 

    public void update() { 
    	

    	position.x += speed.x * dt;
        position.y += speed.y * dt;

        double v = Math.sqrt(speed.x * speed.x + speed.y * speed.y);
    	double Rx = -c * v * speed.x;
    	double Ry = -c * v * speed.y;
    	double Fx = Rx;
    	double Fy = -m * g + Ry;

    	speed.x += Fx/m * dt;
    	speed.y += Fy/m * dt;

        
        if (position.y <= r * zoom) {
            position.y = r * zoom;
            speed.y = -speed.y;
        }
    	lifeTime++;
    	
        Draw.circle(gl, r * zoom, position.x, position.y, true); 
    } 
    
    public double getX()         { return position.x; }
    public double getY()         { return position.y; }
    public int getLifeTime()     { return lifeTime; }
} 
