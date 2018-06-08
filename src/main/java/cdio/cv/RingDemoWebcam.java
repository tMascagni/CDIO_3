package cdio.cv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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

            RingDetector rd = new RingDetector(image);
            ArrayList<RingImg> rings = rd.processAll(rd.orgImg);

            for (RingImg ring : rings) {
                System.out.println("Found a circle!");
                Imgproc.circle(rd.grayImg, ring.getPosition(), 1, new Scalar(255, 100, 250), 3, Imgproc.LINE_AA, 0);
                Imgproc.circle(rd.grayImg, ring.getPosition(), (int) ring.getRadius(), new Scalar(255, 100, 250), 3, Imgproc.LINE_AA, 0);
            }
            ImageIcon icon = new ImageIcon(cvHelper.mat2buf(rd.grayImg));
            label.setIcon(icon);
            webCamView.repaint();
            Thread.sleep(100);

        }

    }
}
