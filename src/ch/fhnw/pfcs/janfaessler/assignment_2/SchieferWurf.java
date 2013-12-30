package ch.fhnw.pfcs.janfaessler.assignment_2; 
  
import com.jogamp.opengl.util.FPSAnimator; 

import java.awt.event.*; 
import java.awt.geom.Point2D;
import java.util.ArrayList; 

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas; 
import javax.swing.JFrame;
  
public class SchieferWurf extends JFrame implements GLEventListener, KeyListener { 

	private static final long serialVersionUID = -4760225131061774357L;
	
	ArrayList<Ball> balls = new ArrayList<Ball>(); 
    
	private double v0 = 20; // start speed 
    private double w = 45; // angle 
    private Point2D.Double startPos = new Point2D.Double(-30, 10);
    
    private double frames = 0; // number of time steps 
    private double delay = 10; // time delay 
    
    private double viewportWidth = 40;
    
    private boolean shift = false;
    
    
    public static void main(String[] args) { 
        new SchieferWurf(); 
    } 

    public SchieferWurf() { 
		this.setName("Schiefer Wurf");
		this.setTitle("Schiefer Wurf");
        this.setSize(800, 600);  
        this.addKeyListener(this); 
        
        GLCanvas canvas = new GLCanvas(); 
        canvas.addGLEventListener(this); 
        canvas.addKeyListener(this); 
        this.add(canvas); 
        FPSAnimator anim = new FPSAnimator(canvas, 60, true); 
        anim.start(); 
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true); 
        
    } 

    @Override public void init(GLAutoDrawable drawable) { 
        GL gl0 = drawable.getGL(); 
        GL2 gl = gl0.getGL2(); 
        gl.glClearColor(0.0f, 0.0f, 1.0f, 1.0f); 
    } 
  
    @Override public void display(GLAutoDrawable drawable) { 
        GL gl0 = drawable.getGL(); 
        GL2 gl = gl0.getGL2(); 
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT); 
        gl.glMatrixMode(GL2.GL_MODELVIEW); 
        gl.glLoadIdentity(); 
        
        // draw axes
        gl.glTranslated(0, -25, 0); 
        gl.glColor3d(0.5, 0.5, 0.5); 
        DrawUtils.drawLine(gl,new Point2D.Double(-viewportWidth, 0), new Point2D.Double(viewportWidth, 0));
        
        // draw ball
        gl.glColor3d(1, 1, 1); 
        
        // throw new ball
        if (frames % delay == 0) balls.add(new Ball(gl, startPos, v0, w)); 
  
        // update balls 
        for (int i = 0; i < balls.size(); i++)
            balls.get(i).update();
        
        // remove balls
        for (int i = 0; i < balls.size(); i++)
        	if (balls.get(i).getX() < -viewportWidth || balls.get(i).getX() > viewportWidth || balls.get(i).getLifeTime() > 1500)
        		balls.remove(i);
        
        gl.glColor3d(0.75, 0.75, 0.75); 
        DrawUtils.drawAngle(gl, startPos, w, 2);

        frames++; 
    } 
  
    @Override public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
            GL2 gl = drawable.getGL().getGL2();
            gl.glViewport(0, 0, width, height);
            double aspect = (double) height / width;
            double left = -viewportWidth;
            double right = viewportWidth;
            double bottom = left * aspect;
            double top = right * aspect;
            double near = -100, far = 100;
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrtho(left, right, bottom, top, near, far);
        }
  
    @Override public void dispose(GLAutoDrawable drawable) {  } 
  
    @Override public void keyTyped(KeyEvent e) {  } 
    @Override public void keyPressed(KeyEvent e) { 
        switch (e.getKeyCode()) { 
            case KeyEvent.VK_ESCAPE: System.exit(0);    break;  
            case KeyEvent.VK_SHIFT:
            	shift = true;
            	break;
            case KeyEvent.VK_RIGHT:  
            	if (shift) {
            		startPos.x += 0.5;
            	} else {
            		w = (w - 6) % 360;
                	System.out.println("angle changed: "+w);
            	}
            	break; 
            case KeyEvent.VK_LEFT:   
            	if (shift) {
            		startPos.x -= 0.5;
            	} else {
            		w = (w + 6) % 360; 
                	System.out.println("angle changed: "+w);
            	}
            	break; 
            case KeyEvent.VK_DOWN: 
            	if (shift) {
            		startPos.y -= 0.5;
            	} else {
            		if (v0 > 0) v0 -= 0.5;
                	System.out.println("speed changed: "+v0);
            	}
            	break; 
            case KeyEvent.VK_UP:   
            	if (shift) {
            		startPos.y += 0.5;
            	} else {
            		v0 += 0.5;
                	System.out.println("speed changed: "+v0);
            	}
            	break; 
            case KeyEvent.VK_A:   
            	delay += 1;
            	System.out.println("delay changed: "+delay);
            	break; 
            case KeyEvent.VK_D:   
            	if (delay > 2) delay -= 1;
            	System.out.println("delay changed: "+delay);
        } 
    }
    @Override public void keyReleased(KeyEvent e) { 
    	switch (e.getKeyCode()) { 
    	case KeyEvent.VK_SHIFT:
        	shift = false;
        	break;
    	}
    } 
} 