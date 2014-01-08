import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

public class Fly implements WindowListener, GLEventListener, KeyListener {
 // Startvektor
	GLCanvas canvas;
	double left = -40, right = 40;
	double bottom, top;
	double near = -100, far = 100;
	 double winkel = 30;
	double elev = 10;
	double azim = 40;
	double dist = 1;
	double rot = 0;
	double rot2 = 0;
	double rotStep = 1;
	double phi = 0; 
	GyroDynamics gyroDynamics = new GyroDynamics();
	//Schieferwurf
	double xm = 0;  // Koord. Ball
	double ym = 2; //Koord. Ball
	double zm = 0;
	double g = 9.81; //Erdbeschleunigung 
	double ax = 0; //Bei Lufstwiederstand < 0
    double ay = -g;
    double az = 0;
	double schneller = 1;
	double vx = 15;
	double vy = 12;
	double vz = 10;
	double dt = 0.01; //s
	double a = 2;
	double b = 2;
	double c = 2;

	boolean switchGeometry = true; //Wechsel Objekte
	int r = 3;
	double phit = 0.001;
	ArrayList<Geometry> geom = new ArrayList<Geometry>();
	private GLUT glut = new GLUT();
	int counter;
	int objektcounter = 0;

	void rotateCam(GL2 gl, double phi, double nx, double ny, double nz) {
		gl.glRotated(-phi, nx, ny, nz);
	}
	
	double[] cross(double[] u, double[] v) {
		double[] n = { u[1] * v[2] - u[2] * v[1], u[2] * v[0] - u[0] * v[2],
				u[0] * v[1] - u[1] * v[0] };
		return n;
	}

	double[] normale(double[] A, double[] B, double[] C) {
		double[] u = { B[0] - A[0], B[1] - A[1], B[2] - A[2] };
		double[] v = { C[0] - A[0], C[1] - A[1], C[2] - A[2] };
		return this.cross(u, v);
	}
	
	void zeichneCube(GL2 gl, double a, double b, double c){             // Pyramide zeichnen
   	 double[] A = {a/2, 0, b/2};
   	 double[] B = {a/2, 0, -b/2};
   	 double[] C = {a/2, c, -b/2};
   	 double[] D = {a/2, c, b/2};
   	 double[] E = {-a/2, 0, b/2};
   	 double[] F = {-a/2, 0, -b/2};
   	 double[] G = {-a/2, c, b/2};
   	 double[] H = {-a/2, c, -b/2};   	 
   	 gl.glColor3d(0.75,0.75,0.75);
   	 //Grundfläche
   	 gl.glBegin(gl.GL_POLYGON);  // Boden
   	  gl.glNormal3dv(normale(A,B,F), 0);
         gl.glVertex3dv(A,0);
         gl.glVertex3dv(B,0);
         gl.glVertex3dv(F,0);
         gl.glVertex3dv(E,0);
       gl.glEnd();
     	 gl.glBegin(gl.GL_POLYGON);  // Deckel
      	  gl.glNormal3dv(normale(D,C,H), 0);
            gl.glVertex3dv(D,0);
            gl.glVertex3dv(C,0);
            gl.glVertex3dv(H,0);
            gl.glVertex3dv(G,0);
          gl.glEnd();
      	 gl.glBegin(gl.GL_POLYGON);  // Hinten
     	  gl.glNormal3dv(normale(B,C,H), 0);
           gl.glVertex3dv(B,0);
           gl.glVertex3dv(C,0);
           gl.glVertex3dv(H,0);
           gl.glVertex3dv(F,0);
         gl.glEnd();
      	 gl.glBegin(gl.GL_POLYGON);  // Vorne
     	  gl.glNormal3dv(normale(A,D,G), 0);
           gl.glVertex3dv(A,0);
           gl.glVertex3dv(D,0);
           gl.glVertex3dv(G,0);
           gl.glVertex3dv(E,0);
         gl.glEnd();
      	 gl.glBegin(gl.GL_POLYGON);  // seite links
     	  gl.glNormal3dv(normale(A,B,C), 0);
           gl.glVertex3dv(A,0);
           gl.glVertex3dv(B,0);
           gl.glVertex3dv(C,0);
           gl.glVertex3dv(D,0);
         gl.glEnd();
      	 gl.glBegin(gl.GL_POLYGON);  // seite rechts
     	  gl.glNormal3dv(normale(E,G,H), 0);
           gl.glVertex3dv(E,0);
           gl.glVertex3dv(F,0);
           gl.glVertex3dv(H,0);
           gl.glVertex3dv(G,0);
         gl.glEnd();

    }	
	
	static class Geometry{
		 double m_x;
		 double m_y;
		 double m_z;
		 double m_vy;
		 double m_vx;
		 double m_vz;
		 double m_schneller;
		 double m_winkel;
		 double m_ax;
		 double m_ay;
		 double m_a;
		 double m_b;
		 double m_c;
		 boolean m_geom;
		 double m_phi;
		 GyroDynamics m_gyro;
		 double epsilon = 0.001;
		 
		 public Geometry(double m_a, double m_b, double m_c,double m_x,double m_y, double m_z, double m_vy, double m_vx, double m_vz, double m_schneller, double m_winkel, double m_ax, double m_ay, double m_az, boolean m_geom, double m_phi){
			 this.m_x = m_x; //+Math.cos(m_winkel*rad);
			 this.m_y = m_y; //+Math.sin(m_winkel*rad);
			 this.m_z = m_z;
			 this.m_vy = m_vy;
			 this.m_vx = m_vx;
			 this.m_vz = m_vz;
			 this.m_schneller = m_schneller;
			 this.m_winkel = m_winkel;
			 this.m_ax = m_ax;
			 this.m_ay = m_ay;
			 this.m_a = m_a;
			 this.m_b = m_b;
			 this.m_c = m_c;
			 this.m_winkel = m_winkel;
			 this.m_geom = m_geom;
			 this.m_phi = m_phi;
		 }
	 }

	void translateCam(GL2 gl, double dx, double dy, double dz) {
		gl.glTranslated(dx, dy, dz);
	}

	void zeichneAchsen(GL2 gl, double a) {
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(a, 0, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, a, 0);
		gl.glVertex3d(0, 0, 0);
		gl.glVertex3d(0, 0, a);
		gl.glEnd();
	}
	public static abstract class Dynamics {

		public double[] euler(double[] x, double dt) {
			double[] y = this.f(x);
			double[] xx = new double[x.length];
			for (int i = 0; i < x.length; i++) {
				xx[i] = x[i] + y[i] * dt; // xx = x+y*dt
			}

			return xx;
		}

		public double[] runge(double[] x, double dt) {
			double[] xx = new double[x.length];

			double[] y1 = this.f(x); // erster Hilfsvektor
			for (int i = 0; i < x.length; i++) {
				xx[i] = x[i] + y1[i] * dt / 2; // xx = x+y*dt/2
			}

			double[] y2 = this.f(xx); // zweiter Hilfsvektor
			for (int i = 0; i < x.length; i++) {
				xx[i] = x[i] + y2[i] * dt / 2; // xx = x+y*dt/2
			}

			double[] y3 = this.f(xx); // dritter Hilfsvektor
			for (int i = 0; i < x.length; i++) {
				xx[i] = x[i] + y3[i] * dt; // xx = x+y*dt
			}

			double[] y4 = this.f(xx); // vierter Hilfsvektor

			double[] ym = new double[x.length]; // gemittelter Vektor
			for (int i = 0; i < x.length; i++) {
				ym[i] = (y1[i] + 2 * y2[i] + 2 * y3[i] + y4[i]) / 6;
			}

			for (int i = 0; i < x.length; i++) {
				xx[i] = x[i] + ym[i] * dt; // xx = x+ym*dt
			}

			return xx;
		}

		public abstract double[] f(double[] x);
	}
	private class GyroDynamics extends Dynamics {
		double I1 = 1;
		double I2 = 2;
		double I3 = 3;
		double D1 = 0;
		double D2 = 0;
		double D3 = 0;	
		double[] vektorfeld = {10, 0, 0, 1, 0, 0, 0};		
		@Override
		public double[] f(double[] x) {
            //System.out.println("w1 = "+ x[0]+"  w2 = "+x[1]+"  w3 = "+ x[2]+"  q0 = "+ x[3]+"  q1 = "+ x[4]+"  q2 = "+ x[5]+"  q3 = "+ x[6]);
            double[] w = {x[0], x[1], x[2]};
            double[] q = {x[3], x[4], x[5], x[6]};
           
            double w1 = (1.0 / I1) * (I2 - I3) * w[1]  * w[2];
            double w2 = (1.0 / I2) * (I3- I1) * w[2]  * w[0];
            double w3 = (1.0 / I3) * (I1 - I2) * w[0]  * w[1];
            double q0 = -(1.0/2.0) * (q[1] * w[0] + q[2] * w[1] + q[3] * w[2]);
            double q1 = (1.0/2.0) * (q[0] * w[0] + q[2] * w[2] - q[3] * w[1]);
            double q2 = (1.0/2.0) * (q[0] * w[1] + q[3] * w[0] - q[1] * w[2]);
            double q3 = (1.0/2.0) * (q[0] * w[2] + q[1] * w[1] - q[2] * w[0]);           
            double[] res = {w1, w2, w3, q0, q1, q2, q3};
			 
			return res;
		}
	}
	
	public Fly() {
		Frame f = new Frame("MySecond");
		canvas = new GLCanvas();
		f.setSize(800, 600);
		f.setBackground(Color.gray);
		f.addWindowListener(this);
		f.addKeyListener(this);
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		f.add(this.canvas);
		f.setVisible(true);
		FPSAnimator anim = new FPSAnimator(this.canvas, 120, true);
		anim.start();
	}

	public static void main(String[] args) {
		new Fly();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(0.6f, 0.6f, 0.6f, 1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GLLightingFunc.GL_NORMALIZE);
		gl.glEnable(GLLightingFunc.GL_LIGHT0);
		gl.glShadeModel(GLLightingFunc.GL_FLAT); // FLAT or SMOOTH
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		//long time 
		GL2 gl = drawable.getGL().getGL2();
		float[] lightPos = { -10, 150, 100, 1 };
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);
		gl.glColor3d(1, 1, 1);

		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();

		// camera system
		translateCam(gl, 0, 0, 2);
		rotateCam(gl, -elev, 1, 0, 0);
		rotateCam(gl, azim, 0, 1, 0);
		gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_POSITION, lightPos, 0);

		// object system
		gl.glDisable(GLLightingFunc.GL_LIGHTING);
		gl.glTranslated(-10, -5, 0);
		zeichneAchsen(gl, 20);
		 gl.glEnable(GLLightingFunc.GL_LIGHTING);
	     if (counter == 60){
				if(schneller < 0){
					schneller = 0;
				}
				if(winkel <= 0){
					winkel = 1;
				}
				if(winkel >= 180){
					winkel = 179;
				}
				Geometry tmp = new Geometry(a,b,c,xm, ym, zm,vx, vy, vz, schneller, winkel, ax, ay, az, switchGeometry, phit);
				tmp.m_gyro = new GyroDynamics();
				geom.add(tmp);
				objektcounter = objektcounter+1;
				counter = 0;
			}
			else{
				for(int i = 0; i < geom.size(); i++) {
					Geometry tmp2 = geom.get(i);
					if (Math.abs(tmp2.m_y) < -80) {
						geom.remove(i);
						objektcounter = objektcounter-1;
					} else { 
						// double[] tmparr = gyroDynamics.runge(tmp2.m_gyro.vektorfeld, dt);
						 tmp2.m_gyro.vektorfeld = gyroDynamics.runge(tmp2.m_gyro.vektorfeld, dt);
						 double q0 = tmp2.m_gyro.vektorfeld[3];
						 double q1 = tmp2.m_gyro.vektorfeld[4];
						 double q2 = tmp2.m_gyro.vektorfeld[5];
						 double q3 = tmp2.m_gyro.vektorfeld[6];
						 double betrag =  Math.sqrt(q1*q1+q2+q2+q3*q3);
						 if (betrag != betrag) {//Betrag = NaN
							 	betrag = 1; 
						 }
						 tmp2.m_phi = 2*Math.acos(q0);
						 tmp2.m_x += tmp2.m_vx*dt;
						 tmp2.m_y += tmp2.m_vy*dt;
						 tmp2.m_z += tmp2.m_vz*dt;
						 tmp2.m_vy += ay*dt;
						 if(tmp2.m_geom){
						    gl.glTranslated(tmp2.m_x, 0, 0);
						    gl.glTranslated(0, tmp2.m_y, 0);
						    gl.glTranslated(0, 0, tmp2.m_z);
						    gl.glRotated(Math.toDegrees(tmp2.m_phi), q1/betrag, q2/betrag, q3/betrag);
					    	 zeichneCube(gl, tmp2.m_a, tmp2.m_b, tmp2.m_c);
					    	gl.glRotated(Math.toDegrees(-tmp2.m_phi), q1/betrag, q2/betrag, q3/betrag);
					    	 gl.glTranslated(0, 0, -tmp2.m_z);
					    	 gl.glTranslated(0, -tmp2.m_y, 0);
							 gl.glTranslated(-tmp2.m_x, 0, 0);
					     }
					     else{
							 gl.glTranslated(tmp2.m_x, 0, 0);
							 gl.glTranslated(0, tmp2.m_y, 0);
							 gl.glTranslated(0, 0, tmp2.m_z);
							 gl.glRotated(Math.toDegrees(tmp2.m_phi), q1/betrag, q2/betrag, q3/betrag);
					    	 glut.glutSolidTorus(tmp2.m_a/2-tmp2.m_a/4,tmp2.m_a/2,32,32);
					    	 gl.glRotated(-Math.toDegrees(tmp2.m_phi), q1/betrag, q2/betrag, q3/betrag);
					    	 gl.glTranslated(0, 0, -tmp2.m_z);
					    	 gl.glTranslated(0, -tmp2.m_y, 0);
							 gl.glTranslated(-tmp2.m_x, 0, 0);
							 
					     }

					}
				}
				counter = counter +1;
			}
		
		//gl.glPopMatrix();
	     gl.glFlush();
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		double aspect = (float) height / width;
		this.bottom = aspect * this .left;
		this.top = aspect * this.right;
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(this.left, this.right, this.bottom, this.top, this.near, this.far);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

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
		case 'A':
			a = a + 0.5;
			break;
		case 'a':
			a = a - 0.5;
			break;
		case 'B':
			b = b + 0.5;
			break;
		case 'b':
			b = b - 0.5;
			break;
		case 'C':
			c = c + 0.5;
			break;
		case 'c':
			c = c - 0.5;
			break;
		case 'X':
			
			vx = vx + 1;
			break;
		case 'x':
			vx = vx - 1;
			break;
		case 'Y':
			
			vy = vy + 1;
			break;
		case 'y':
			vy = vy - 1;
			break;
		case 'Z':
			
			vz = vz + 1;
			break;
		case 'z':
			vz = vz - 1;
			break;	
		
		case '1':
			switchGeometry = true;
			break;	
		case '2':
			switchGeometry = false;
			break;	
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	//double[] d = {w1,w2,w3,q0,q1,q2,q3}
	
}