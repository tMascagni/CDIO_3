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
        long getGreyStartTime = System.nanoTime();
        getGray();
        long thresholdingStartTime = System.nanoTime();
        ArrayList<QRImg> qr_codes = new ArrayList<>();
        for(int i = 50; i < 200; i += 20) {
            thresholding(i);
            //edgeDetection();
            long contourtreeStartTime = System.nanoTime();
            ContourTree con = getContours();

            long findQR = System.nanoTime();
            ArrayList<QRImg> qr_codes_tmp = new ArrayList<>();
            findQR(qr_codes_tmp, grayImg, con);
            qr_codes.addAll(sortQR(qr_codes_tmp));
        }
        long timeEnd = System.nanoTime();

       /* System.out.println("time to grey: " + (thresholdingStartTime-getGreyStartTime));
        System.out.println("time to threshold: " + (contourtreeStartTime-thresholdingStartTime));
        System.out.println("time to contour: " + (findQR-contourtreeStartTime));
        System.out.println("time to findQR: " + (timeEnd-findQR));
*/
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

/*
    HighGUI no longer exists in java, and we need to use swing instead to display
    images. The computervision lib shouldn't display images either.
    These are kept for reference

    public void write(String path) {
        Imgcodecs.imwrite(path + ".jpg", orgImg);
        Imgcodecs.imwrite(path + "_gray.jpg", grayImg);
        Imgcodecs.imwrite(path + "_edge.jpg", binImg);
        System.out.println("Out: " + path);
    }

    public void draw() {
        Imgcodecs.namedWindow(name, HighGui.WINDOW_AUTOSIZE);
        Imgcodecs.imshow(name, orgImg);
    }
    public void drawEdge() {
        HighGui.namedWindow(name+"_edge", HighGui.WINDOW_AUTOSIZE);
        HighGui.imshow(name+"_edge", binImg);
    }

    public void drawGray() {
        HighGui.namedWindow(name+"_gray", HighGui.WINDOW_AUTOSIZE);
        HighGui.imshow(name+"_gray", grayImg);
    }
    */

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
        /* HSV method
        // Convert to HSV
        Mat tmpImg = orgImg.clone();
        Imgproc.cvtColor(tmpImg, tmpImg, Imgproc.COLOR_BGR2HSV);

        // Split HSV channels to seperat mats
        ArrayList<Mat> tmpMats = new ArrayList<>();
        Core.split(tmpImg, tmpMats);

        // Get only V channel
        grayImg = tmpMats.get(2);

        */
        grayImg = new Mat();
        Imgproc.cvtColor(orgImg, grayImg, Imgproc.COLOR_BGR2GRAY);
        //Imgproc.threshold(binImg, binImg, 180, 255,Imgproc.THRESH_BINARY);
    }

    private void edgeDetection() {
        int threshold = 100;
        int ratio = 3;
        int blurAmout = 3;
        Imgproc.cvtColor(orgImg, grayImg, Imgproc.COLOR_BGR2GRAY);
        binImg = grayImg.clone();
        Mat out = new Mat();
        Imgproc.bilateralFilter(grayImg, out,11, 17, 17 );
        Imgproc.blur(out, binImg, new Size(blurAmout, blurAmout));
        Imgproc.Canny(binImg, binImg, 30,200);
    }

    public void thresholding(int thres) {
        binImg = new Mat();
        Imgproc.threshold(grayImg, binImg, thres, 255, Imgproc.THRESH_BINARY);
    }

    public void thresholding() {
        int thres = 120;
        binImg = new Mat();
        Imgproc.threshold(grayImg, binImg, thres, 255, Imgproc.THRESH_BINARY);
    }

    private void addLines(Mat dst, Mat scr) {
        LineSegmentDetector lsd = Imgproc.createLineSegmentDetector();
        Mat lines = new Mat();
        lsd.detect(scr, lines);
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
                qrImg.position = src_point.center;
                qrImg.contour = new MatOfPoint(points);
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

        int maxCount = 10000;
        int acceptanceLimit = maxCount/2;
        int channel = 0;
        for(int count = 0; count < src.size(); count ++) {
            int outOfRangeCount = 0;
            Mat img = src.get(count).getImg().clone();
            //Imgproc.cvtColor(src.get(count).getImg(), img, Imgproc.COLOR_BGR2GRAY);
            double avg = 0;
            for (int i = 0; i < maxCount; i++) {
                int x = (int) (Math.random() * img.width());
                int y = (int) (Math.random() * img.height());
                double[] p = img.get(y,x);

                int colorValue = (int) p[channel];

                if(colorValue > 75 && colorValue < 180){
                    outOfRangeCount++;
                }
            }
            double ratio = src.get(count).getW() / ((double) src.get(count).getH());
            if(!Double.isNaN(angleOfQRCode(src.get(count)) )) {//if(outOfRangeCount < acceptanceLimit){
                qrkoder.add(src.get(count));
                //System.out.println("Acceptable! " + outOfRangeCount + " / " + acceptanceLimit +
                        //"\t Angle: " + angleOfQRCode(src.get(count)) +
                        //"\t Ratio: " + ratio);
                //cvHelper.displayImage(cvHelper.mat2buf(src.get(count).getImg()));
                //cvHelper.displayImage(cvHelper.mat2buf(img));

            }
            else {
                //qrkoder.add(src.get(count));
                //System.out.println("Unacceptable! " + outOfRangeCount + " / " + acceptanceLimit);
                //cvHelper.displayImage(cvHelper.mat2buf(img));
            }

        }

        return qrkoder;
    }

    public double angleOfQRCode(QRImg input){
        double A3Ratio = 1.414;
        double oldRatio = 0.8;
        double width = input.getW();
        double height = input.getH();
        double widthNorm = width/height;
        double s = 0;

        s = Math.toDegrees(Math.acos(widthNorm/oldRatio)); // 1 radian = 57.2957795 grader

        return s;
    }
}


