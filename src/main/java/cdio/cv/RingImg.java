package cdio.cv;

import cdio.model.QRCodeData;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

public class RingImg {
    private Mat img;
    private double h;
    private double w;
    private MatOfPoint contour;
    private Point position;
    private double angle;

    public RingImg(Mat Img) {
        img = Img;
    }

    public Mat getImg() {
        return img;
    }

    public void setImg(Mat img) {
        this.img = img;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public MatOfPoint getContour() {
        return contour;
    }

    public void setContour(MatOfPoint contour) {
        this.contour = contour;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
