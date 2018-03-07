package cdio.room;

import java.io.Serializable;

public class CircleAnalyse implements Serializable{

    private static final long serialVersionUID = 42L;

    public final Vectorize v;
    public final double r;

    public CircleAnalyse(Vectorize v, double r) {
        if (!(r > 0)) throw new IllegalArgumentException("Radius must be positive");
        this.v = v;
        this.r = r;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((v == null) ? 0 : v.hashCode());
        long temp;
        temp = Double.doubleToLongBits(r);
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
        CircleAnalyse other = (CircleAnalyse) obj;
        if (v == null) {
            if (other.v != null)
                return false;
        } else if (!v.equals(other.v))
            return false;
        if (Double.doubleToLongBits(r) != Double.doubleToLongBits(other.r))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(v: " + v + ", r: " + r + ")";
    }

}
