package controller;

public class WallMarking {

    int nummer;
    double x;
    double y;

    public int getNummer() {
        return nummer;
    }

    public void setNummer(int nummer) {
        this.nummer = nummer;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public WallMarking(int nummer, double x, double y) {
        this.nummer = nummer;
        this.x = x;
        this.y = y;
    }
}
