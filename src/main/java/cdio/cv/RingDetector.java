package cdio.cv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class RingDetector {
    public Mat orgImg, grayImg, binImg;
    private CVHelper cvHelper = new CVHelper();

    public RingDetector() {

    }

    public RingDetector(Mat image) {
        orgImg = image.clone();
    }

    public RingDetector(String filePath) {
        orgImg = Imgcodecs.imread(filePath);
    }

    public ArrayList<RingImg> processAll(Mat mat) {
        ArrayList<RingImg> rings = new ArrayList<>();
        orgImg = mat;
        getGray();

        Imgproc.blur(grayImg, grayImg, new Size(3, 3));
        edgeDetection();
        //thresholding(200);
        Dilate(1);
        Erode(1);
        Dilate(2);
        Erode(2);
        Dilate(3);

        Mat newImg = grayImg.clone();

        Mat circleMat = new Mat();
        Imgproc.HoughCircles(newImg, circleMat, Imgproc.HOUGH_GRADIENT, 1,
                newImg.rows() / 16, 200, 60, 10, 0);

        for (int i = 0; i < circleMat.cols(); i++) {
            double[] c = circleMat.get(0, i);
            RingImg ring = new RingImg();
            ring.setPosition(new Point(c[0], c[1]));
            ring.setRadius(c[2]);
            rings.add(ring);
        }

        return rings;
    }

    private void Dilate(int size) {
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                new Size(2 * size + 1, 2 * size + 1),
                new Point(size, size));
        Imgproc.dilate(binImg, binImg, element);
    }

    private void Erode(int size) {
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
                new Size(2 * size + 1, 2 * size + 1),
                new Point(size, size));
        Imgproc.erode(binImg, binImg, element);
    }


    private void edgeDetection() {
        Mat img = grayImg.clone();
        binImg = new Mat();
        Imgproc.blur(img, img, new Size(5, 5));
        int thres = 50;
        Imgproc.Canny(img, binImg, thres, thres * 3);
    }

    private void thresholding(int thres) {
        binImg = new Mat();
        Imgproc.threshold(grayImg, binImg, thres, 255, Imgproc.THRESH_BINARY);
    }

    private void getGray() {
        Mat hsvImg = orgImg.clone();
        ArrayList<Mat> channels = new ArrayList<>();
        //Imgproc.cvtColor(orgImg, hsvImg, Imgproc.COLOR_BGR2HSV);

        Core.split(hsvImg, channels);
        grayImg = channels.get(1);
    }
}
