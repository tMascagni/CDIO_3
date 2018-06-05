package cdio.computervision;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

public class QRImg {

    private Mat img;
    private double h;
    private double w;
    public MatOfPoint contour;


    public QRImg(Mat img, double h, double w) {
        this.img = img;
        this.h = h;
        this.w = w;
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
}
