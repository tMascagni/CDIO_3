package cdio.cv.CVTest;

import cdio.cv.CVHelper;
import cdio.cv.QRDetector;
import cdio.cv.QRImg;
import org.opencv.core.Core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;


/*
    This demo classed is only for reference.
    For picking out QR-codes from images, used QRDetector.java
 */

public class demo {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }


    public static void main(String[] args) {
        CVHelper cvHelper = new CVHelper();

        // write your code here
        //init();

        String path = "qr_code_test/pil";
        File folder = new File(path);
        int j = 0;
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().toLowerCase().contains(".jpg")) {

                QRDetector qr = new QRDetector(fileEntry.getAbsolutePath());
                ArrayList<QRImg> res = qr.processAll(qr.orgImg);

                int i = 0;

                for (QRImg r : res) {
                    // Imgcodecs.imwrite("/home/pil/Desktop/ringe/daniel/output/"+ i + fileEntry.getName(), r);
                    //  double angle = qr.angleOfQRCode(r);
                    // System.out.println("Angle: " + angle);
                    BufferedImage buffOrgImg = cvHelper.mat2buf(r.getImg());
                    cvHelper.displayImage(buffOrgImg); // Original img
                    i++;
                }

                   /*
                   System.out.println("Bin img: " + qr.binImg.type());
                   System.out.println("Org img: " + qr.orgImg.type());

                   // Convert binImg and orgImg to bufferedImages
                   BufferedImage buffBinImg = cvHelper.mat2buf(qr.binImg);

                   // Show the images
                   displayImage(buffBinImg); // Converted img (black)
                   */
            }
            j++;
        }

    }

}