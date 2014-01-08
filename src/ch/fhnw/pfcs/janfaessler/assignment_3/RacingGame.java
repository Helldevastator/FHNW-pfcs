package ch.fhnw.pfcs.janfaessler.assignment_3;

import ch.fhnw.pfcs.janfaessler.util.Draw;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;

public class RacingGame extends JFrame implements GLEventListener, KeyListener {

    private static final long serialVersionUID = -8790324402153794190L;

    private static final int viewportWidth = 50;
    private static final double maxCentripetalForce = 2 * 9.80665; // 2g
    private static final int maxAngle = 25;
    private static final int maxSpeed = 20;
    private static final double speedSteps = 1.0;
    private static final double angleSteps = 1.5;

    private Course course;
    private Car car;

    private boolean running = true;
    private double speed;

    private double top;
    private double bottom;
    private double left;
    private double right;

    private TextRenderer renderer;

    public static void main(String[] args) { new RacingGame(); }

    public RacingGame() {
        this.setName("RacingGame");
        this.setTitle("RacingGame");
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

    @Override
    public void init(GLAutoDrawable drawable) { 
        GL gl0 = drawable.getGL(); 
        GL2 gl = gl0.getGL2(); 
        gl.glClearColor(0f, 0f, 0f, 1.0f);
        
        course = new RoundCourse(gl);
        car = new Car(gl, course.getStartPosition(), 2.5, Color.RED);
        
        renderer = new TextRenderer(new Font("Arial", Font.BOLD, 10));
    } 

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2(); 
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT); 
        gl.glMatrixMode(GL2.GL_MODELVIEW); 
        gl.glColor3d(0, 0, 0);

        course.draw();
        car.update();
        
        drawCentripetalForcePanel(gl);
        
        // draw car info
        renderer.beginRendering(drawable.getWidth(), drawable.getHeight());
        renderer.draw(car.toString(), 10, 20);
        renderer.endRendering();
    }
	
    private void drawCentripetalForcePanel(GL2 gl) {
        double width = 20;
        gl.glPushMatrix();
        gl.glColor3d(1,1,1);
        gl.glTranslated(left + 2.5, top -4, 0);
        Draw.rect2d(gl, new Point2D.Double(0,0), new Point2D.Double(width + 0.1, 2.5), false);
        double value = car.getCentripetalForce() * width / maxCentripetalForce;
        if (car.getCentripetalForce() > maxCentripetalForce) {
        	value = width ;
        	gl.glColor3d(1,0,0);
        } else {
        	gl.glColor3d(0,1,0);
        }
        Draw.rect2d(gl, new Point2D.Double(0.1, 0.1), new Point2D.Double(value, 2.5), true);
        gl.glPopMatrix();
    }


    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        double aspect = (double) height / width;
        left = -viewportWidth;
        right = viewportWidth;
        bottom = left * aspect;
        top = right * aspect;
        double near = -100, far = 100;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(left, right, bottom, top, near, far);
    }
    
    @Override
    public void dispose(GLAutoDrawable arg0) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (car.getWheelAngle() < maxAngle)
                        car.setWheelAngle(car.getWheelAngle() + angleSteps);
                break;
            case KeyEvent.VK_RIGHT:
                if (car.getWheelAngle() > -maxAngle)
                        car.setWheelAngle(car.getWheelAngle() - angleSteps);
                break;
            case KeyEvent.VK_UP:
                if (car.getSpeed() < maxSpeed)
                        car.setSpeed(car.getSpeed() + speedSteps);
                break;
            case KeyEvent.VK_DOWN:
                if (car.getSpeed() > -maxSpeed)
                        car.setSpeed(car.getSpeed() - speedSteps);
                break;
            case KeyEvent.VK_S:
                if (running) {
                        running = false;
                        speed = car.getSpeed();
                        car.setSpeed(0);
                } else {
                        running = true;
                        car.setSpeed(speed);
                }
                break;
            case KeyEvent.VK_R: 
                car.setSpeed(0);
                car.setAngle(0);
                car.setWheelAngle(0);
                car.setPosition(course.getStartPosition());
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

}
