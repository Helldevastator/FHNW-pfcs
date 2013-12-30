package ch.fhnw.pfcs.janfaessler.assignment_3;

import java.awt.geom.Point2D;


public interface Course {
	
	public void draw();
	
	public Point2D.Double getStartPosition();
	
	public double getStartAngle();

}
