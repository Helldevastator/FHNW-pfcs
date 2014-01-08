package ch.fhnw.pfcs.janfaessler.assignment_6;

import ch.fhnw.pfcs.janfaessler.util.Draw;
import ch.fhnw.pfcs.janfaessler.util.Vec2;
import ch.fhnw.pfcs.janfaessler.util.Vec3;
import com.jogamp.opengl.util.FPSAnimator;
import java.util.List;
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
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.swing.JFrame;

public class FlyingObjects extends JFrame implements GLEventListener, KeyListener{
    
    private static enum Type { Quader, Torrus }

    private double viewportWidth = 250;
    
    private float[] ambReflection =  { 0.6f, 0.6f, 0.6f, 1f} ;
    private float[] diffReflection = { 0.6f, 0.6f, 0.6f, 1f} ;
    private float[] specReflection = { 0.2f, 0.2f, 0.2f, 1f} ;
    private float[] specExp = {20};
    private float[] ambient = { 0.4f, 0.4f, 0.4f, 1f };
    private float[] lightPos = { -10, 10, 10, 1f };
    
    private boolean shift = false;
    
    private final double dt = 0.01;                    // time steps 
    private static Type objectType = Type.Torrus;      // Object Type
    private double elev = 10;                          // Elevation Camera
    private double azim = 40;                          // Azimut Camera
    private double v0 = 5;                             // shooting start speed 
    private double w = 45;                             // shooting angle 
    private final Vec3 aV0 = new Vec3(2, 2, 2);     // starting angle speed
    private final int bulletDelay = 10;                // delay of the bullets
    private final Vec3 startPos = new Vec3(0,0,0);
    private final Vec3 quaderSize = new Vec3(5,5,5);
    private final Vec2 torrusSize = new Vec2(1, 4);
    
    private final List<AbstractBullet> bullets = new ArrayList<>();
    private int bulletCount = 0;
    

    public static void main(String[] args) { new FlyingObjects(); }
    public FlyingObjects() {
        this.setName("FlyingObjects");
        this.setTitle("FlyingObjects");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);

        GLCanvas canvas = new GLCanvas();
        canvas.addGLEventListener(this);
        canvas.addKeyListener(this);
        this.add(canvas);
        
        this.setVisible(true);
        
        FPSAnimator anim = new FPSAnimator(canvas, 100, true);
        anim.start();
    }
    
    @Override
    public void init(GLAutoDrawable glad) {
        GL2 gl = glad.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        gl.glShadeModel(GLLightingFunc.GL_FLAT); 
        
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_NORMALIZE);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GLLightingFunc.GL_COLOR_MATERIAL);

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION,lightPos, 0);

        gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_AMBIENT, ambReflection, 0);
        gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_DIFFUSE, diffReflection, 0);
        gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_SPECULAR, specReflection, 0);
        gl.glMaterialfv(GL.GL_FRONT, GLLightingFunc.GL_SHININESS,specExp, 0);
    }

    @Override
    public void dispose(GLAutoDrawable glad) { }

    @Override
    public void display(GLAutoDrawable glad) {
        GL2 gl = glad.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT + GL.GL_DEPTH_BUFFER_BIT);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        rotateCam(gl, elev, azim);
        gl.glTranslated(0, -10, 0);
        gl.glColor3d(1, 1, 0);
        gl.glPushMatrix();
        gl.glRotated(45, 0, 1, 0);
        Draw.axes3d(gl);
        gl.glPopMatrix();
        
        if (bulletCount++  % bulletDelay == 0) {
            if (objectType.equals(Type.Quader))
                bullets.add(new Quader(startPos.clone(), aV0.clone(), v0, w, quaderSize));
            else
                bullets.add(new Torrus(startPos.clone(), aV0.clone(), v0, w, torrusSize));
        }
        
        Iterator<AbstractBullet> it = bullets.iterator();
        while(it.hasNext()) {
            AbstractBullet b = it.next();
            b.update(dt);
            b.draw(gl);
            if (b.getLiveTime() == 0) it.remove();
        }
        
    }
    
    private void rotateCam(GL2 gl, double elev, double azim) {
        gl.glRotated(elev, 1, 0, 0);
        gl.glRotated(-azim, 0, 1, 0);
    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {	
        GL2 gl = glad.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        double aspect = (double) height / width;
        double left = -viewportWidth;
        double right = viewportWidth;
        double bottom = left * aspect;
        double top = right * aspect;
        double near = -viewportWidth*2, far = viewportWidth*2;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(left, right, bottom, top, near, far);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SHIFT:
                shift = true;
                break;
            case KeyEvent.VK_1:
                objectType = Type.Quader;
                break;
            case KeyEvent.VK_2:
                objectType = Type.Torrus;
                break;
            case KeyEvent.VK_UP:
                elev += 2;
                elev %= 360;
                break;
            case KeyEvent.VK_DOWN:
                elev -= 2;
                elev %= 360;
                break;
            case KeyEvent.VK_LEFT:
                azim += 2;
                azim %= 360;
                break;
            case KeyEvent.VK_RIGHT:
                azim -= 2;
                azim %= 360;
                break;
            case KeyEvent.VK_V:
                if (shift) v0 += 0.5;
                else       v0 -= 0.5;
                if (v0 < 0) v0 = 0;
                break;
            case KeyEvent.VK_W:
                if (shift) w += 2;
                else       w -= 2;
                w %= 360;
                break;
            case KeyEvent.VK_A:
                if (shift) quaderSize.x += 1;
                else       quaderSize.x -= 1;
                if (quaderSize.x < 0) quaderSize.x = 0;
                break;
            case KeyEvent.VK_B:
                if (shift) quaderSize.y += 1;
                else       quaderSize.y -= 1;
                if (quaderSize.y < 0) quaderSize.y = 0;
                break;
            case KeyEvent.VK_C:
                if (shift) quaderSize.z += 1;
                else       quaderSize.z -= 1;
                if (quaderSize.z < 0) quaderSize.z = 0;
                break; 
            case KeyEvent.VK_X:
                if (shift) aV0.x += 2;
                else       aV0.x -= 2;
                if (aV0.x < 0) aV0.x = 0;
                break; 
            case KeyEvent.VK_Y:
                if (shift) aV0.y += 2;
                else       aV0.y -= 2;
                if (aV0.y < 0) aV0.y = 0;
                break; 
            case KeyEvent.VK_Z:
                if (shift) aV0.z += 2;
                else       aV0.z -= 2;
                if (aV0.z < 0) aV0.z = 0;
                break; 
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT)
            shift = false;
    }
    
    @Override
    public void keyTyped(KeyEvent e) { }
    
}
