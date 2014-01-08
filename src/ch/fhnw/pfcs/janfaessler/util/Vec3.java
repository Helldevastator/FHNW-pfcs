package ch.fhnw.pfcs.janfaessler.util;

public class Vec3 {
    
    public double x;
    public double y;
    public double z;
    
    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public Vec3 clone() {
        return new Vec3(x, y, z);
    }
    
    public Vec3 norm() {
        double abs = Math.sqrt(x * x + y * y + z * z);
        return new Vec3(x/abs, y/abs, z/abs);
    }
    
    @Override
    public String toString() {
        return "Vec3[x: "+x+" y: "+y+" z: "+z+"]";
    }
}
