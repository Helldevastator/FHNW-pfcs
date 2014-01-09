package ch.fhnw.pfcs.janfaessler.assignment_6;

import ch.fhnw.pfcs.janfaessler.util.Vec3;

import java.awt.Color;
import java.util.Arrays;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

public class Quader extends AbstractBullet {
    
    private static final double M = 10;
    private static final double f = 1.0 / 12.0 * M;
    private final double a, b, c;
    private final double a2, b2, c2;

    public Quader(Vec3 pos, Vec3 angleSpeed, double v0, double a0, Vec3 size) {
        super(pos, angleSpeed, v0, a0);
        a = size.x;
        b = size.y;
        c = size.z;
        a2 = a * a;
        b2 = b * b;
        c2 = c * c;

        super.color = Color.RED;
        super.setDullness(getDullness());
    }

    @Override
    public void draw(GL2 gl) {
        super.prepareDraw(gl);
        gl.glScaled(a, b, c);
        (new GLUT()).glutSolidCube(1);
        
        super.finishDraw(gl);
    }

    private double[] getDullness() {
        double[] res = {f * (b2 + c2), f * (a2 + c2), f * (a2 + b2)};
        Arrays.toString(res);
        return res;
    }
    
}
