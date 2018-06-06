package cdio.computervision;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.LineSegmentDetector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class QRDetector implements ICV{

    private static final double CV_PI = 3.14159;

    private CVHelper cvHelper = new CVHelper();

    public Mat orgImg, grayImg, binImg;

    public QRDetector() {
    }

    public QRDetector(String filePath) {
        orgImg = Imgcodecs.imread(filePath);
    }

    public QRDetector(Mat inputImage) {
        orgImg = inputImage.clone();
    }

    public QRDetector(BufferedImage inputImage) {
        orgImg = cvHelper.buf2mat(inputImage);
    }

    // Do everything and return the qr codes as images
    public ArrayList<QRImg> processAll(Mat mat) {
        orgImg = mat;
        getGray();
        ArrayList<QRImg> qr_codes = new ArrayList<>();
        for(int i = 50; i < 200; i += 20) {
            thresholding(i);

            ContourTree con = getContours();

            ArrayList<QRImg> qr_codes_tmp = new ArrayList<>();
            findQR(qr_codes_tmp, grayImg, con);
            qr_codes.addAll(sortQR(qr_codes_tmp));
        }

        return qr_codes;
        //return qr_codes;
    }

    // Do everything and return the qr codes as images (USED FOR THE GUI)
    public BufferedImage processSingleImg(Image dstImg) {
        getGray();
        thresholding(120);
        ContourTree con = getContours();

        findQRDraw(orgImg, con);
        return cvHelper.mat2buf(orgImg);
    }

    public ContourTree getContours() {
        ContourTree ct = null;
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hir = new Mat();
        Imgproc.findContours(binImg, contours, hir, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        if (contours.size() > 0) {
            ct = new ContourTree(hir, contours, 0);
        }

        return ct;
    }

    public void getGray() {
        grayImg = new Mat();
        Imgproc.cvtColor(orgImg, grayImg, Imgproc.COLOR_BGR2GRAY);
    }

    public void thresholding(int thres) {
        binImg = new Mat();
        Imgproc.threshold(grayImg, binImg, thres, 255, Imgproc.THRESH_BINARY);
    }

    // Find the QR codes in the image.
    // We use the contour tree, and select all with a certain depth.
    // These are not guaranteed to be QR codes, but they are likely.
    // The images are then transformed to a rectangle for further processing.
    public void findQR(ArrayList<QRImg> dst, Mat scr, ContourTree ct) {

        if(ct != null) {
            ArrayList<RotatedRect> rects = ct.findRectIfChildren(0, 4);

            for (RotatedRect src_point : rects) {
                src_point.size.width *= 1.2;
                src_point.size.height *= 1.2;
                Mat new_img = scr.clone();
                Point p[] = {
                        new Point(0, 0),
                        new Point(328, 0),
                        new Point(328, 483),
                        new Point(0, 483)
                };

                MatOfPoint2f dest_points = new MatOfPoint2f(p);
                Point[] points = new Point[4];
                src_point.points(points);
                points = ct.orderPoints(points);

                Mat perspectiveTransform = Imgproc.getPerspectiveTransform(new MatOfPoint2f(points), dest_points);
                Imgproc.warpPerspective(scr, new_img, perspectiveTransform, new_img.size());

                QRImg qrImg = new QRImg(new_img, src_point.boundingRect().height, src_point.boundingRect().width);
                qrImg.setPosition(src_point.center);
                qrImg.setContour(new MatOfPoint(points));
                dst.add(qrImg);
            }
        }
    }

    // Find the QR codes in the image.
    // We use the contour tree, and select all with a certain depth.
    // These are not guaranteed to be QR codes, but they are likely.
    // The images are then transformed to a rectangle for further processing.
    public void findQRDraw(Mat scr, ContourTree ct) {
        //ct.drawRectIfChildren(scr, 3);
    }

    public ArrayList<QRImg> sortQR(ArrayList<QRImg> src) {
        ArrayList<QRImg> qrkoder = new ArrayList<>();

        for(int count = 0; count < src.size(); count ++) {
            src.get(count).setAngle(angleOfQRCode(src.get(count)));
            if(!Double.isNaN(src.get(count).getAngle())) {
                qrkoder.add(src.get(count));
            }
        }
        return qrkoder;
    }

    public double angleOfQRCode(QRImg input){
        double ratio = 0.8;
        double width = input.getW();
        double height = input.getH();
        double widthNorm = width/height;
        double s = Math.toDegrees(Math.acos(widthNorm/ratio)); // 1 radian = 57.2957795 grader
        return s-15;
    }
}


