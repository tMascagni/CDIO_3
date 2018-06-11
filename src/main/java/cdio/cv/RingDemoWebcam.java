package cdio.cv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class RingDemoWebcam {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws InterruptedException {
        CVHelper cvHelper = new CVHelper();

        VideoCapture cam = new VideoCapture(0);
        // write your code here
        //init();
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

            QRDetector qrDetector = new QRDetector(image);
            QRImg qr = qrDetector.findBest(qrDetector.processAll(image));
            if (qr != null) {
                Imgproc.drawContours(image, Arrays.asList(qr.getContour()), -1, new Scalar(15, 250, 200), 3);
                RingDetector rd = new RingDetector(image);
                RingImg ring = rd.findFromQR(image, qr);

                if (ring != null) {
                    System.out.println("Found a circle!");
                    Imgproc.circle(image, ring.getPosition(), 1, new Scalar(255, 100, 250), 3, Imgproc.LINE_AA, 0);
                    Imgproc.circle(image, ring.getPosition(), (int) ring.getRadius(), new Scalar(255, 100, 250), 3, Imgproc.LINE_AA, 0);
                }
            }
            ImageIcon icon = new ImageIcon(cvHelper.mat2buf(image));
            label.setIcon(icon);
            webCamView.repaint();

            Thread.sleep(100);

        }

    }
}
