package cdio.cv;

import cdio.model.QRCodeData;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

public class QRImg {

    private Mat img;
    private double h;
    private double w;
    private MatOfPoint contour;
    private Point position;
    private QRCodeData qrCodeData;
    private double angle;

    @Override
    public String toString() {
        return "QRImg{" +
                ", h=" + h +
                ", w=" + w +
                ", position=" + position +
                ", qrCodeData=" + qrCodeData.toString() +
                ", angle=" + angle +
                '}';
    }

    public QRImg(Mat img, double h, double w) {
        this.img = img;
        this.h = h;
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

    public Mat getImg() {
        return img;
    }

    public void setImg(Mat img) {
        this.img = img;
    }

    public double getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public double getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public QRCodeData getQrCodeData() {
        return qrCodeData;
    }

    public void setQrCodeData(QRCodeData qrCodeData) {
        this.qrCodeData = qrCodeData;
    }
}