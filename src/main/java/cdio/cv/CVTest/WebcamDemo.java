package cdio.cv.CVTest;

import cdio.cv.CVHelper;
import cdio.cv.QRDetector;
import cdio.model.QRImg;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class WebcamDemo {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    //private static IDroneCommander droneCommander = DroneCommander.getInstance();

    public static void main(String[] args) throws InterruptedException {

        VideoCapture cam = new VideoCapture(0);
        QRDetector qrDetector = new QRDetector();
        CVHelper cvHelper = new CVHelper();
        ArrayList<JFrame> windows = new ArrayList<>();
        JFrame webCamView = new JFrame();
        webCamView.setLayout(new FlowLayout());
        JLabel label = new JLabel();
        webCamView.add(label);
        webCamView.setSize(720, 480);
        webCamView.setVisible(true);
        while (true) {
            Mat image = new Mat();
            cam.read(image);

            ArrayList<QRImg> qrImgs = qrDetector.processAll(image);


            for (QRImg qr : qrImgs) {
                Imgproc.drawContours(image, Arrays.asList(qr.getContour()), -1, new Scalar(15, 250, 200), 3);
                Imgproc.drawMarker(image, qr.getPosition(), new Scalar(10, 250, 200), 0, 20, 5, 8);
            }

            QRImg qrImg = qrDetector.findBest(qrImgs);
            if (qrImg != null) {
                Imgproc.drawContours(image, Arrays.asList(qrImg.getContour()), -1, new Scalar(255, 50, 10), 4);
                Imgproc.drawMarker(image, qrImg.getPosition(), new Scalar(255, 10, 0), 0, 20, 5, 8);
                System.out.println("H:" + qrImg.getH() + "\t D: " + qrImg.getDistance());

            }
            ImageIcon icon = new ImageIcon(cvHelper.mat2buf(image));
            label.setIcon(icon);
            webCamView.repaint();
            Thread.sleep(100);
        }

    }
}
