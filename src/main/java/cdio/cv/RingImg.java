package cdio.cv;

import org.opencv.core.Point;

public class RingImg {
    private double radius;
    private Point position;

    public RingImg() {
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

}

