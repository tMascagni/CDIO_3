package cdio.computervision;
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

public class WebcamDemo {

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
       while(true) {
           Mat image = new Mat();
           cam.read(image);
           //ArrayList<QRImg> qrImgs = qrDetector.processAll(image);
           qrDetector.orgImg = image;
           qrDetector.getGray();
           ArrayList<QRImg> qr_codes = new ArrayList<>();
           for(int i = 50; i < 200; i += 20) {
               qrDetector.thresholding(i);
               ContourTree ct = qrDetector.getContours();
               if(ct != null) {
                   ct.drawRectIfChildren(image, 3, 0);
               }
               ArrayList<QRImg> qr_codes2 = new ArrayList<>();
               qrDetector.findQR(qr_codes2, qrDetector.grayImg, ct);
               qr_codes.addAll(qr_codes2);
           }
           qr_codes = qrDetector.sortQR(qr_codes);



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
                   Imgproc.drawMarker(image, img.position, new Scalar(255, 10, 100), 0, 20, 5, 8);
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
