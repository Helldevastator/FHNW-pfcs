package ch.fhnw.pfcs.janfaessler.assignment_6;

import ch.fhnw.pfcs.janfaessler.util.Draw;
import ch.fhnw.pfcs.janfaessler.util.Vec3;
import java.awt.Color;
import java.util.Arrays;
import javax.media.opengl.GL2;

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
        
        Vec3 A = new Vec3(a/2, 0, b/2);
   	Vec3 B = new Vec3(a/2, 0, -b/2);
   	Vec3 C = new Vec3(a/2, c, -b/2);
   	Vec3 D = new Vec3(a/2, c, b/2);
   	Vec3 E = new Vec3(-a/2, 0, b/2);
   	Vec3 F = new Vec3(-a/2, 0, -b/2);
   	Vec3 G = new Vec3(-a/2, c, b/2);
   	Vec3 H = new Vec3(-a/2, c, -b/2);  
        
        Draw.quad3d(gl, A, D, G, E); // front
        Draw.quad3d(gl, E, F, H, G); // right
        Draw.quad3d(gl, B, C, H, F); // back
        Draw.quad3d(gl, A, B, C, D); // left
        Draw.quad3d(gl, D, C, H, G); // top
        Draw.quad3d(gl, A, B, F, E); // bottom
        
        super.finishDraw(gl);
    }

    private double[] getDullness() {
        double[] res = {f * (b2 + c2), f * (a2 + c2), f * (a2 + b2)};
        Arrays.toString(res);
        return res;
    }
    
}
