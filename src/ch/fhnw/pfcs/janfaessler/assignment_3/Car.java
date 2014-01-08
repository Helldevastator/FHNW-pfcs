package ch.fhnw.pfcs.janfaessler.assignment_3;

import ch.fhnw.pfcs.janfaessler.util.Draw;
import ch.fhnw.pfcs.janfaessler.util.Vec2;
import java.awt.Color;
import java.awt.geom.Point2D;

import javax.media.opengl.GL2;

public class Car {
	
    private final GL2 graphics;							   

    // car default properties
    private Color color;
    
    // position and speed
    private Vec2 position;
    private double speed;
    
    // angles
    private double angleWheel = 0;
    private double angleCar = 0;
    private double angleLeft = 0;
    private double angleRight = 0;
        
    private double carLenght;  
    private double carWidth;
    private double axisOffsetX;
    private double axisLenght;
    private double wheelLenght;
    private double wheelWidth;
    private double radius;
	
    public Car(GL2 gl, Vec2 start, Color color) {               this(gl, start, 10, 0, color); }
    public Car(GL2 gl, Vec2 start, double size) {               this(gl, start, size, 0, Color.RED); }
    public Car(GL2 gl, Vec2 start, double size, double angle) { this(gl, start, size, angle, Color.RED); }
    public Car(GL2 gl, Vec2 start, double size, Color color) {  this(gl, start, size, 0, color); }
    public Car(GL2 gl, Vec2 start, double size, double angle, Color color) {

        // add class cariables
        this.graphics = gl;
        this.position = (Vec2) start.clone();
        this.angleCar = angle;
        this.color = color;

        // calculate size
        carLenght = size/2;   // the half of the car length for less calculations
        carWidth = carLenght/2;  // the half of the car width for less calculations

        // calculate axis
        axisOffsetX = size/3;
        axisLenght = carWidth + carWidth/3; // the half of the axis width for less calculations

        // calculate wheels
        wheelLenght = size/10;
        wheelWidth = wheelLenght/2;

        draw();
    }
	
    public void update() {

            double dt = Draw.getTimeStep();

            double angle = Math.toRadians(angleCar);
            double angleW = Math.toRadians(angleWheel);

            if (angleWheel == 0) {

                    // drive straight on
                    position.x += speed * Math.cos(angle) * dt;
                    position.y += speed * Math.sin(angle) * dt;

            } else {

                    double d = 2 * axisOffsetX;  // Abstand Achsen
                    double b = axisLenght;       // Halbe Breite

                    if (angleWheel > 0) {

                            // turn left
                            radius = b + d / Math.tan(angleW);
                            angleLeft = angleWheel;
                            angleRight  = Math.toDegrees(Math.tan(d / (radius - b)));

                    } else {

                            // turn right
                            radius = -b + d / Math.tan(angleW);
                            angleLeft = Math.toDegrees(Math.tan(d / (radius - b)));
                            angleRight = angleWheel;

                    }

                    double deltaPhi = speed * dt / radius;

                    position.x += radius * (Math.sin(angle+ deltaPhi) - Math.sin(angle));
                    position.y -= radius * (Math.cos(angle + deltaPhi) - Math.cos(angle));

                    angleCar = (angleCar + Math.toDegrees(deltaPhi)) % 360;
            }

            draw();
    }

    public final double getCentripetalForce() {
            double value = Math.abs(speed * speed / radius);
            return value == Double.POSITIVE_INFINITY ? 0 : value;
    }

    public final Color getColor() { return color; }
    public final void setColor(Color color) { this.color = color; } 

    public final Vec2 getPosition() { return position; }
    public final void setPosition(Vec2 location) { position = location; }

    public final double getWheelAngle() { return angleWheel; }
    public final void setWheelAngle(double angle) { this.angleWheel = angle; }

    public final double getAngle() { return angleCar; }
    public final void setAngle(double angle) { this.angleCar = angle; }

    public final double getSpeed() { return speed; }
    public final void setSpeed(double speed) { this.speed = speed; }

    public String toString() {
            return        "angleCar: " + String.format("%.2f",Math.abs(angleCar)) + ", "
                                    + "angleWheel: " + String.format("%.2f",angleWheel) + ", "
                            + "speed: " + speed + ", "
                        + "centripetal force: " + getCentripetalForce();
    }

    private void draw() {
            graphics.glPushMatrix();
            graphics.glColor3d(color.getRed()/255, color.getGreen()/255, color.getBlue()/255);

            // translate and rotate
            graphics.glTranslated(position.x, position.y, 0);
            graphics.glRotated(angleCar, 0, 0, 1);

            // draw body
            Draw.rect2d(graphics, new Point2D.Double(-carLenght, carWidth), new Point2D.Double(carLenght, -carWidth), true);

            // draw axis
            Draw.line2d(graphics, new Vec2(-axisOffsetX, axisLenght), new Vec2(-axisOffsetX, -axisLenght));
            Draw.line2d(graphics, new Vec2(axisOffsetX, axisLenght),  new Vec2(axisOffsetX, -axisLenght));

            // draw weels
            drawWeel(new Point2D.Double(-axisOffsetX,  axisLenght+wheelWidth),           0);
            drawWeel(new Point2D.Double(-axisOffsetX, -axisLenght-wheelWidth),           0);
            drawWeel(new Point2D.Double(axisOffsetX,   axisLenght+wheelWidth),   angleLeft);
            drawWeel(new Point2D.Double(axisOffsetX,  -axisLenght-wheelWidth),   angleRight);

            graphics.glPopMatrix();
    }

    private void drawWeel(Point2D.Double pos, double angle) {
            graphics.glPushMatrix();

            // translate and rotate
            graphics.glTranslated(pos.x, pos.y, 0);
            graphics.glRotated(angle, 0, 0, 1);

            // draw weel
            Draw.rect2d(graphics, new Point2D.Double(-wheelLenght, wheelWidth), new Point2D.Double(wheelLenght, -wheelWidth), true);

            graphics.glPopMatrix();
    }

}
