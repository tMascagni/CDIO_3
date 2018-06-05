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
      while(true) {
          Mat image = new Mat();
          cam.read(image);
          ArrayList<QRImg> qrImgs = qrDetector.processAll(image);
          if(qrImgs.size() > 0) {
             for(JFrame frame : windows) {
                frame.setVisible(false);
              }
              windows = new ArrayList<>();
             for(QRImg img : qrImgs) {
                 windows.add(cvHelper.displayImage(cvHelper.mat2buf(img.getImg())));
                 System.out.println("Angle: " + qrDetector.angleOfQRCode(img) +
                                    "\t Ratio: " + (img.getW() / img.getH()));
             }
          }
          else {
              //System.out.println("Empty");
          }
          Thread.sleep(100);
      }

   }
}
