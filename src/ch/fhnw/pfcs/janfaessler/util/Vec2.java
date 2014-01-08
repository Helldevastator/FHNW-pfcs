package ch.fhnw.pfcs.janfaessler.util;

public class Vec2 {
    public double x;
    public double y;
    
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public Vec2 clone() {
        return new Vec2(x, y);
    }
    
    public Vec2 norm() {
        double abs = Math.sqrt(x * x + y * y);
        return new Vec2(x/abs, y/abs);
    }
    
    @Override
    public String toString() {
        return "Vec3[x: "+x+" y: "+y+"]";
    }
}
