package cdio.cv;

import cdio.cv.interfaces.ICV;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class QRDetector implements ICV {

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
        for (int i = 5; i < 240; i += 10) {
            thresholding(i);

            ContourTree con = getContours();

            ArrayList<QRImg> qr_codes_tmp = new ArrayList<>();
            findQR(qr_codes_tmp, grayImg, con);
            qr_codes.addAll(sortQR(qr_codes_tmp));
        }

        return qr_codes;
        //return qr_codes;
    }

    public QRImg findBest(ArrayList<QRImg> qrImgs) {
        if (qrImgs.size() == 1) {
            return qrImgs.get(0);
        }
        // Check if any of the codes have been read succefully
        // If only one have, return that, if multiple have, return largest
        QRImg returnCandidate = null;
        for (QRImg qrImg : qrImgs) {
            if (qrImg.getQrCodeData() != null) {
                if (returnCandidate == null) {
                    returnCandidate = qrImg;
                } else {
                    if (Imgproc.contourArea(qrImg.getContour()) >
                            Imgproc.contourArea(returnCandidate.getContour())) {
                        returnCandidate = qrImg;
                    }
                }
            }
        }
        if (returnCandidate != null) {
            return returnCandidate;
        }

        // No qrCodes were read.
        // Find the average position and area
        double avg_x = 0, avg_y = 0, avg_a = 0;
        int count = 0;
        for (QRImg qrImg : qrImgs) {
            avg_x += qrImg.getPosition().x;
            avg_y += qrImg.getPosition().y;
            avg_a += Imgproc.contourArea(qrImg.getContour());
            count++;
        }
        avg_x /= count;
        avg_y /= count;
        avg_a /= count;

        // Remove outliers
        ArrayList<QRImg> toRemove = new ArrayList<>();
        for (QRImg qrImg : qrImgs) {

            // Distance for center of qr to average center
            double diff_x = avg_x - qrImg.getPosition().x;
            double diff_y = avg_y - qrImg.getPosition().y;
            double dist = Math.sqrt(diff_x * diff_x + diff_y * diff_y);

            // If that distance is larger than the area, discard the qr code
            if (dist > Math.sqrt(avg_a / Math.PI)) {
                toRemove.add(qrImg);
            }
        }
        qrImgs.removeAll(toRemove);

        if (qrImgs.size() == 0) {
            return null;
        }

        // Calculate average qr
        double avg_h = 0;
        double avg_w = 0;
        double avg_angle = 0;
        double avg_distance = 0;
        avg_x = 0;
        avg_y = 0;
        count = 0;
        for (QRImg qrImg : qrImgs) {
            avg_h += qrImg.getH();
            avg_w += qrImg.getW();
            avg_angle += qrImg.getAngle();
            avg_distance += qrImg.getDistance();
            avg_x += qrImg.getPosition().x;
            avg_y += qrImg.getPosition().y;
            count++;
        }
        avg_h /= count;
        avg_w /= count;
        avg_angle /= count;
        avg_distance /= count;
        avg_x /= count;
        avg_y /= count;

        QRImg averageQr = new QRImg(qrImgs.get(0).getImg(), avg_h, avg_w);
        averageQr.setAngle(avg_angle);
        averageQr.setContour(qrImgs.get(0).getContour());
        averageQr.setPosition(new Point(avg_x, avg_y));
        averageQr.setQrCodeData(null);
        averageQr.setDistance(avg_distance);
        return averageQr;
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
        try {
            Imgproc.findContours(binImg, contours, hir, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        } catch (Exception ignored) {
            System.out.println("getContours EXCEPTION!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }

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

        if (ct != null) {
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

        for (int count = 0; count < src.size(); count++) {
            src.get(count).setDistance(distanceFromHeight(src.get(count).getH()));
            src.get(count).setAngle(angleOfQRCode(src.get(count)));
            if (!Double.isNaN(src.get(count).getAngle())) {
                qrkoder.add(src.get(count));
            }
        }
        return qrkoder;
    }

    public double angleOfQRCode(QRImg input) {
        double ratio = 0.8;
        double width = input.getW();
        double height = input.getH();
        double widthNorm = width / height;
        double s = Math.toDegrees(Math.acos(widthNorm / ratio)); // 1 radian = 57.2957795 grader
        return s - 15;
    }

    public double distanceFromHeight(double qrCodeHeight) {
        return 0.8 * 48722 * Math.pow(qrCodeHeight, -1.021);
    }

}


