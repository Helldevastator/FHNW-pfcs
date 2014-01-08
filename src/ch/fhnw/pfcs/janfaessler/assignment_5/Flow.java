package ch.fhnw.pfcs.janfaessler.assignment_5;

import ch.fhnw.pfcs.janfaessler.util.Draw;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import com.jogamp.opengl.util.FPSAnimator;

public class Flow implements GLEventListener, KeyListener {
    
    private double left = -25, right = 25;
    private double near = -100, far = 100;

    private static final double r = 5.0;
    private static final Color color = Color.RED;
    
    private ArrayList<Particle> particles = new ArrayList<Particle>();
    
    private int waveCount = 0;
    private int waveDistance = 4;
    
    private double startY = -5.5;
    private double startX = -25;
       
    FlowDynamics flowDyn = new FlowDynamics(r);
   

    public Flow() {
        JFrame f = new JFrame("Flow");
        GLCanvas canvas = new GLCanvas();
        f.setSize(800, 600);
        f.addKeyListener(this);
        canvas.addGLEventListener(this);
        canvas.addKeyListener(this);
        f.add(canvas);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FPSAnimator anim = new FPSAnimator(canvas, 200, true);
        anim.start();
    }

    public static void main(String[] args) {
        new Flow();
    }
    
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT + GL.GL_DEPTH_BUFFER_BIT);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();

                
        if (waveCount++ % waveDistance == 0) {
            
            for (int i = 0; i < 24; i++) 
                particles.add(new Particle(startX, (i*0.5+startY)));
        }
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.move(flowDyn);
            p.draw(gl);
            if (p.getX() > right) it.remove();
        }
        gl.glColor3d(color.getRed(), color.getGreen(), color.getBlue());
        Draw.circle2d(gl, r, 0, 0);
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {}

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0f, 0f, 0f, 1.0f); 
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl0 = drawable.getGL();
        GL2 gl = gl0.getGL2();
        gl.glViewport(0, 0, width, height);
        double aspect = (double) height / width;
        double bottom = aspect * left, top = aspect * right;
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(left, right, bottom, top, near, far);
    }

    @Override
    public void keyTyped(KeyEvent e) { }
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_R:
                particles.clear();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}