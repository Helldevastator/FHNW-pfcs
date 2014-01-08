package ch.fhnw.pfcs.janfaessler.assignment_5;

import ch.fhnw.pfcs.janfaessler.util.Dynamics;

public class FlowDynamics extends Dynamics {
    
    private final double r2;
    private final double r4;
	
    public FlowDynamics(double r) {
        r2 = r * r;
        r4 = r2 * 2;
    }

    @Override
    public double[] f(double[] x) {
        assert x.length == 2 : "Vektor muss 2 Komponenten enthalten";
        double xy2 = x[0] * x[0] + x[1] * x[1];
        double xy22 = xy2 * xy2;
        double X = 1 + r2 / xy2 - (r4 * x[0] * x[0]) / xy22;
        double Y = - (r4 * x[0] * x[1]) / xy22;
        double[] result = {X, Y};
        return result;
    }
}
