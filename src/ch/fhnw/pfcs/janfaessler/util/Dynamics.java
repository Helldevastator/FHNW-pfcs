package ch.fhnw.pfcs.janfaessler.util;

public abstract class Dynamics {
    
    public static double g = Draw.getGravity();
    
    public static enum Type { Euler, Runge }
    
    public double[] move(double[] x, double dt) {
        return move(x, dt, Type.Runge);
    }
    
    public double[] move(double[] x, double dt, Type t) {
        switch(t) {
            case Euler:
                return euler(x, dt);
            case Runge:
            default:
                return runge(x, dt);
        }
    }
	 
    public double[] euler(double[] x, double dt) {
        double[] y = this.f(x);
        double[] result = new double[x.length];
        for (int i = 0; i < x.length; i++)
            result[i] = x[i] + y[i] * dt;

        return result;
    }

    public double[] runge(double[] x, double dt) {
        double[] result = new double[x.length];

        // first helper vector
        double[] y1 = this.f(x);                     

        // secound helper vector
        for (int i = 0; i < x.length; i++) 
            result[i] = x[i] + y1[i] * dt / 2;
        double[] y2 = this.f(result);                    

        // third helper vector
        for (int i = 0; i < x.length; i++) 
            result[i] = x[i] + y2[i] * dt / 2;
        double[] y3 = this.f(result);                    

        // forth helper vector
        for (int i = 0; i < x.length; i++)
            result[i] = x[i] + y3[i] * dt;
        double[] y4 = this.f(result);                    

        // avarage vector
        double[] ym = new double[x.length];          
        for (int i = 0; i < x.length; i++) 
            ym[i] = (y1[i] + 2 * y2[i] + 2 * y3[i] + y4[i]) / 6;

        // result Vektor
        for (int i = 0; i < x.length; i++)
            result[i] = x[i] + ym[i] * dt;

        return result;
    }

    public abstract double[] f(double[] x);
}