package ch.fhnw.pfcs.janfaessler.assignment_4;

import javax.media.opengl.*;

import java.awt.*;
import java.awt.event.*;

import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.swing.JFrame;

import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.*;

public class Gyroscope implements GLEventListener, KeyListener {
	GLCanvas canvas;
	double left = -6, right = 6; 
	double bottom, top; 
	double near = -100, far = 100;
	GLUT glut;
	GL2 gl;
	double elev = 10;
	double azim = 40;
	double dist = 4;
	double phi = 60;
	double omega = 3;
	double dt = 0.01; 

	public Gyroscope() {
		JFrame f = new JFrame("Gyroskop");
		canvas = new GLCanvas();
		f.setSize(800, 600);
		f.setBackground(Color.gray);
		f.addKeyListener(this);
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		f.add(canvas);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FPSAnimator anim = new FPSAnimator(canvas, 120, true);
		anim.start();
	}

	public static void main(String[] args) {
		new Gyroscope();
	}

	public void init(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2();
		gl.glClearColor(0f, 0f, 0f, 1.0f);
		glut = new GLUT();
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GLLightingFunc.GL_NORMALIZE);
		gl.glEnable(GLLightingFunc.GL_LIGHT0);
	}

	public void display(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GLLightingFunc.GL_COLOR_MATERIAL);
		
		gl.glLoadIdentity();

		gl.glRotated(-90, 1, 0, 0);
		
		phi = (phi + omega * dt) % 360;
		draw();

	}
	
	private void draw() {
		gl.glPushMatrix();

		drawStage(4);

		rotateCam(elev, azim);

		drawComposition(phi, 6);
		
		gl.glPopMatrix();
	}
	
	private void drawStage(double height) {
		gl.glPushMatrix();
		
		// stab
		gl.glColor3d(0.6, 0.6, 0.6);
		glut.glutSolidCylinder(0.1, - height, 64, 64);
		
		// base
		gl.glTranslated(0, 0, -4);
		gl.glColor3d(0.6,0.6,0.6);
		glut.glutSolidCone(0.75, height/3, 64, 64);
		
		gl.glPopMatrix();
	}

	private void rotateCam(double elev, double azim) {
		gl.glRotated(elev-90, 1, 0, 0);
		gl.glRotated(-azim, 0, 1, 0);
	}

	private void drawComposition(double phi, double size) {
		gl.glPushMatrix();
		gl.glTranslated(-0.05, 0, -(size/2));
		
		// stab
		gl.glColor3d(0.6, 0.6, 0.6);
		glut.glutSolidCylinder(0.1, size, 64, 64);
		
		// gewichte
		gl.glColor3d(0.75, 0, 0);
		addWeight(0.35, 0.2, size*55/60);
		gl.glColor3d(0, 0, 0.75);
		addWeight(0.25, 0.2, size*50/60);

		
		gl.glRotated(phi * 180 / Math.PI, 0, 0, 1);
		
		// gyroscope
		gl.glColor3d(0.75, 0.75, 0);
		glut.glutSolidCylinder(1, 0.25, 20, 20);

		gl.glPopMatrix();
	}
	
	private void addWeight(double size, double thickness, double pos) {
		gl.glPushMatrix();
		
		gl.glTranslated(0, 0, pos);
		glut.glutSolidCylinder(size, thickness, 64, 64);
		
		gl.glPopMatrix();
	}
	


	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL gl0 = drawable.getGL();
		GL2 gl = gl0.getGL2();
		gl.glViewport(0, 0, width, height);
		double aspect = (float) height / width;
		bottom = aspect * left;
		top = aspect * right;
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(left, right, bottom, top, near, far);
	}

	public void dispose(GLAutoDrawable drawable) { }



	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		char key2 = e.getKeyChar();
		switch (key) {
		case KeyEvent.VK_UP:
			elev = elev - 3;
			break;
		case KeyEvent.VK_DOWN:
			elev = elev + 3;
			break;
		case KeyEvent.VK_LEFT:
			azim = azim - 3;
			break;
		case KeyEvent.VK_RIGHT:
			azim = azim + 3;
			break;
		}
		switch (key2) {
		case 'w':
			omega = omega - 2;
			break;
		case 'W':
			omega = omega + 2;
			break;
		}

	}

	@Override public void keyReleased(KeyEvent e) { }
	@Override public void keyTyped(KeyEvent e) { }

}