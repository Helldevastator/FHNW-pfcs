package ch.fhnw.pfcs.janfaessler.assignement_2;

import java.awt.geom.Point2D;

import javax.media.opengl.GL2;

public class Ball { 
	
    private final double r  = 0.034;                      // ball radius 34cm 
    private final double m  = 0.058;                      // masse (58g)
    private final double cw = 0.4;                        // wiederstandswert des k??rpers
    
    private final double zoom = 10;
    
    private final double g  = DrawUtils.getGravity();     // gravity
    private final double dt = DrawUtils.getTimeStep();    // time steps
    private final double p  = DrawUtils.getAirDensity();  // luftdichte (kg/m3)
    
    private final double c  = (p / 2) * cw * ((r * r) * Math.PI);
    private final GL2 gl;							   
    
    private Point2D.Double position;
    private Point2D.Double speed;
    private int lifeTime = 0;

    public Ball(GL2 gl, Point2D.Double start, double speed, double angle) { 
    	this.gl = gl;
        this.position = new Point2D.Double(start.x, start.y);
        this.speed = new Point2D.Double(speed * Math.cos(Math.toRadians(angle)), speed * Math.sin(Math.toRadians(angle)));
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
    	
        DrawUtils.drawCircle(gl, r * zoom, position.x, position.y); 
    } 
    
    public double getX()         { return position.getX(); }
	public double getY()         { return position.getY(); }
	public int getLifeTime()     { return lifeTime; }
} 
