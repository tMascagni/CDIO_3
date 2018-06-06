package cdio.computervision;
import cdio.handler.QRCodeException;
import cdio.handler.QRCodeHandler;
import cdio.model.QRCodeData;
import org.opencv.core.*;
import org.opencv.video.*;
import org.opencv.videoio.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class WebcamDemo2 {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }


    public static void main(String[] args) throws InterruptedException {

        VideoCapture cam = new VideoCapture(0);
        QRDetector qrDetector = new QRDetector();
        CVHelper cvHelper = new CVHelper();
        ArrayList<JFrame> windows = new ArrayList<>();
        JFrame webCamView = new JFrame();
        webCamView.setLayout(new FlowLayout());
        JLabel label = new JLabel();
        webCamView.add(label);
        webCamView.setVisible(true);
        QRCodeHandler qrCodeHandler = new QRCodeHandler();
        while(true) {
            Mat image = new Mat();
            cam.read(image);
            //ArrayList<QRImg> qrImgs = qrDetector.processAll(image);
            qrDetector.orgImg = image;
            qrDetector.getGray();
            qrDetector.thresholding();
            ContourTree ct = qrDetector.getContours();
            ArrayList<QRImg> qr_codes = new ArrayList<>();
            qrDetector.findQR(qr_codes, qrDetector.grayImg, ct);
            qr_codes = qrDetector.sortQR(qr_codes);

            if(ct != null) {
                ct.drawRectIfChildren(image, 3, 0);
            }

            if(qr_codes.size() > 0) {
                for(JFrame frame : windows) {
                    frame.setVisible(false);
                }
                windows = new ArrayList<>();
                for(QRImg img : qr_codes) {
                    //windows.add(cvHelper.displayImage(cvHelper.mat2buf(img.getImg())));
                    System.out.println("Angle: " + qrDetector.angleOfQRCode(img) +
                            "\t Ratio: " + (img.getW() / img.getH()));

                    Imgproc.drawContours(image, Arrays.asList(img.contour), -1, new Scalar(255, 50, 100), 4);
                    try {
                        QRCodeData qrCodeData = qrCodeHandler.scanImage(cvHelper.mat2buf(img.getImg()));
                        System.out.println(qrCodeData.toString());
                    } catch (QRCodeException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                //System.out.println("Empty");
            }


            ImageIcon icon = new ImageIcon(cvHelper.mat2buf(image));
            label.setIcon(icon);
            webCamView.repaint();
            Thread.sleep(100);
        }

    }
}
