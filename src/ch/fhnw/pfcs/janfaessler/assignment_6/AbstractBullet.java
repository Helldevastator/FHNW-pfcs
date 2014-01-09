package ch.fhnw.pfcs.janfaessler.assignment_6;

import ch.fhnw.pfcs.janfaessler.util.Draw;
import ch.fhnw.pfcs.janfaessler.util.Dynamics;
import ch.fhnw.pfcs.janfaessler.util.Quaternion;
import ch.fhnw.pfcs.janfaessler.util.Vec3;

import java.awt.Color;

import javax.media.opengl.GL2;

public abstract class AbstractBullet {
    
    private final Vec3 a = new Vec3(0, -Draw.getGravity(), 0);
    
    private final Vec3 pos;       // position
    private Vec3 wv;              // angle speed
    private final Vec3 v;         // speed
    private Quaternion o;   // orientation
    private Quaternion q;         // calculation quaternion
    
    private final RotateDynamics rotateDyn;
    Color color;
    private int livetime = 10000;
    
    public AbstractBullet(Vec3 position, Vec3 angleSpeed, double v0, double angle) {
        // class variables
        pos = position;
        wv = angleSpeed;
        
        angle = Math.toRadians(angle);
        v = new Vec3(Math.cos(angle) * v0, Math.sin(angle) * v0, 0);
        
        // quaternion
        q = new Quaternion(1, 0, 0, 0);
        
        // orientation
        o = new Quaternion(0, 0, 0, 0);
        
        // dynamics
        rotateDyn = new RotateDynamics();
    }
    
    protected void setDullness(double[] dullness) {
        rotateDyn.I = dullness;
    }
    
    public abstract void draw(GL2 gl);
    	
    protected void prepareDraw(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslated(pos.x, pos.y, pos.z);
        gl.glRotated(Math.toDegrees(o.scal), o.vec.x, o.vec.y, o.vec.z);
        gl.glColor3d(color.getRed()/255.0, color.getGreen()/255.0,color.getBlue()/255.0);
    }
    
    protected void finishDraw(GL2 gl) {
        gl.glPopMatrix();
    }
    
    public void update(double dt) {
        // rotate
        double[] rotate = {wv.x, wv.y, wv.z, q.scal, q.vec.x, q.vec.y, q.vec.z};
        rotate = rotateDyn.move(rotate, dt);     
        wv = new Vec3(rotate[0], rotate[1], rotate[2]);
        q = new Quaternion(rotate[3], rotate[4], rotate[5], rotate[6]);
        q = q.norm();
        o = new Quaternion(Math.acos(q.scal) * 2, q.vec.norm());
        
        // fly
        pos.x += v.x * dt;
        pos.y += v.y * dt;
        pos.z += v.z * dt;
        v.x += a.x * dt;
        v.y += a.y * dt;
        v.z += a.z * dt;
        
        livetime--;
    }

    public int getLiveTime() {
        return livetime;
    }
    
    private class RotateDynamics extends Dynamics {
               
        private double[] I;
        
        public RotateDynamics() { }
        

        @Override
        public double[] f(double[] x) {
            double[] w = {x[0], x[1], x[2]};
            double[] q = {x[3], x[4], x[5], x[6]};

            double w1 = 1.0 / I[0] * (I[1] - I[2]) * w[1]  * w[2];
            double w2 = 1.0 / I[1] * (I[2] - I[0]) * w[2]  * w[0];
            double w3 = 1.0 / I[2] * (I[0] - I[1]) * w[0]  * w[1];

            double q0 = -1.0 / 2.0 * (q[1] * w[0] + q[2] * w[1] + q[3] * w[2]);
            double q1 = 1.0 / 2.0 * (q[0] * w[0] + q[2] * w[2] - q[3] * w[1]);
            double q2 = 1.0 / 2.0 * (q[0] * w[1] + q[3] * w[0] - q[1] * w[2]);
            double q3 = 1.0 / 2.0 * (q[0] * w[2] + q[1] * w[1] - q[2] * w[0]);

            double[] res = {w1, w2, w3, q0, q1, q2, q3};
            return res;
        }
    }
}
