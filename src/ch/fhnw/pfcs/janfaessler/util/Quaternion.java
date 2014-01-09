package ch.fhnw.pfcs.janfaessler.util;

public class Quaternion {
    public double scal;
    public Vec3 vec;
    
    public Quaternion(double s, double v1, double v2, double v3) {
        this(s, new Vec3(v1, v2, v3));
    }
    
    public Quaternion(double s, Vec3 v) {
        scal = s;
        vec = v;
    }
    
    public Quaternion norm() {
        double sum = scal * scal + vec.x * vec.x + vec.y * vec.y + vec.z * vec.z;
        double abs = Math.sqrt(sum);        
        return new Quaternion(scal/abs, vec.x/abs, vec.y/abs, vec.z/abs);
    }
    
    @Override
    public String toString() {
        return "q["+scal+","+vec+"]";
    }
}
