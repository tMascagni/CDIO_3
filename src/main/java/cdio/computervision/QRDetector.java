package cdio.computervision;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.LineSegmentDetector;


import java.util.ArrayList;

public class QRDetector {
    private static final double CV_PI = 3.14159;
    public Mat orgImg, grayImg, binImg;

    public  QRDetector(String filePath) {
        orgImg = Imgcodecs.imread(filePath);
    }

    public  QRDetector(Mat inputImage) {
        orgImg = inputImage.clone();
    }

    // Do everything and return the qr codes as images
    public ArrayList<Mat> processAll(ArrayList<Double> variance) {
        getGray();
        thresholding();
        ContourTree con = getContours();

        ArrayList<Mat> qr_codes = new ArrayList<>();
        findQR(qr_codes, orgImg, con);
        sortQR(qr_codes, variance);


        return qr_codes;
        //return qr_codes;
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
        Imgproc.findContours(binImg, contours, hir, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE );
        if(contours.size() > 0) {
            ct = new ContourTree(hir, contours, 0);
        }

       return ct;
    }

    public void getGray() {
        // Convert to HSV
        Mat tmpImg = orgImg.clone();
        Imgproc.cvtColor(orgImg, tmpImg, Imgproc.COLOR_BGR2HSV);

        // Split HSV channels to seperat mats
        ArrayList<Mat> tmpMats = new ArrayList<>();
        Core.split(tmpImg, tmpMats);

        // Get only V channel
        grayImg = tmpMats.get(2).clone();

        //Imgproc.threshold(binImg, binImg, 180, 255,Imgproc.THRESH_BINARY);
    }

    public void edgeDetection() {
        int threshold = 100;
        int ratio = 3;
        int blurAmout = 2;
        binImg = grayImg.clone();
        Imgproc.blur(grayImg, binImg, new Size(blurAmout,blurAmout));
        Imgproc.Canny(binImg, binImg, threshold, threshold*ratio);
    }

    public void thresholding() {
        binImg = new Mat();
        int thres = 190;
        Imgproc.threshold(grayImg, binImg, thres, 255,Imgproc.THRESH_BINARY);
    }

    public void addLines(Mat dst, Mat scr) {
        LineSegmentDetector lsd = Imgproc.createLineSegmentDetector();
        Mat lines = new Mat();
        lsd.detect(scr, lines);
    }

    // Find the QR codes in the image.
    // We use the contour tree, and select all with a certain depth.
    // These are not guaranteed to be QR codes, but they are likely.
    // The images are then transformed to a rectangle for further processing.
    public void findQR(ArrayList<Mat> dst, Mat scr, ContourTree ct) {
       ArrayList<MatOfPoint2f> points = ct.findRectIfChildren(3);
       for(MatOfPoint2f scr_point : points) {
           Mat new_img = scr.clone();
            Point p[] = {
                    new Point(0, 0),
                    new Point(new_img.width(), 0),
                    new Point(new_img.width(), new_img.height()),
                    new Point(0, new_img.height())
            };
           MatOfPoint2f dest_points = new MatOfPoint2f(p);
           Mat perspectiveTransform = Imgproc.getPerspectiveTransform(scr_point, dest_points);
           Imgproc.warpPerspective(scr, new_img, perspectiveTransform, new_img.size());

           dst.add(new_img);
       }
    }

    public void sortQR(ArrayList<Mat> src, ArrayList<Double> deviations) {
        int maxCount = 20000;
        int channel = 1;
        double deviation = 0.1;
        for(int count = 0; count < src.size(); count ++) {
            Mat img = src.get(count).clone();
            Imgproc.cvtColor(src.get(count), img, Imgproc.COLOR_BGR2HSV);
            double avg = 0;
            for (int i = 0; i < maxCount; i++) {
                int x = (int) (Math.random() * img.width());
                int y = (int) (Math.random() * img.height());
                double[] p = img.get(y,x);
                avg += p[channel];
            }

            avg /= maxCount;

            double sum = 0;
            for (int i = 0; i < maxCount; i++) {
                int x = (int) (Math.random() * img.width());
                int y = (int) (Math.random() * img.height());
                double[] p = img.get(y,x);

                sum += (p[channel] - avg) * (p[channel] - avg);
            }

            deviations.add(sum/maxCount);

        }
    }

}
