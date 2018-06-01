package cdio.routing.room;

import java.io.Serializable;

import static java.lang.Math.atan2;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.StrictMath.cos;

public class Vectorize implements Serializable{

    private static final long serialVersionUID = 42L;

    public static final Vectorize NULL = new Vectorize(0, 0);
    public static final Vectorize X = new Vectorize(1, 0);
    public static final Vectorize Y = new Vectorize(0, 1);

    public final double x;
    public final double y;

    public Vectorize(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vectorize add(Vectorize a) {
        return new Vectorize(x + a.x, y + a.y);
    }

    public Vectorize sub(Vectorize a) {
        return new Vectorize(x - a.x, y - a.y);
    }

    public Vectorize neg() {
        return new Vectorize(-x, -y);
    }

    public Vectorize scale(double a) {
        return new Vectorize(a * x, a * y);
    }

    public double dot(Vectorize a) {
        return x * a.x + y * a.y;
    }

    public double modSquared() {
        return dot(this);
    }

    public double mod() {
        return sqrt(modSquared());
    }

    public Vectorize normalize() {
        return scale(1 / mod());
    }

    public Vectorize rotPlus90() {
        return new Vectorize(-y, x);
    }

    public Vectorize rotMinus90() {
        return new Vectorize(y, -x);
    }

    public double angle() {
        return atan2(y, x);
    }

    public static Vectorize fromAngle(double ang) {
        return new Vectorize(cos(ang), sin(ang));
    }

    public static Vectorize fromPolar(double ang, double mod) {
        return new Vectorize(mod * cos(ang), mod * sin(ang));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vectorize other = (Vectorize) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + x + ", " + y + ")";
    }

}
