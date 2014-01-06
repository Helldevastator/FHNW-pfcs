package ch.fhnw.pfcs.janfaessler.assignment_5;

import javax.media.opengl.GL2;

public class DrawUtils {
    
    private static int circlePoints = 40; 					// Anzahl Punkte
    private static double circleStepSize = 2.0 * Math.PI / circlePoints;        // Parameter-Schrittweite
        
    public static void drawCircle(GL2 gl, double r, double x, double y) { 
        gl.glBegin(GL2.GL_POLYGON);
        for (int i = 0; i < circlePoints; i++)
            gl.glVertex2d(x + r * Math.cos(i * circleStepSize), y + r * Math.sin(i * circleStepSize));
        gl.glEnd();
    } 
}
