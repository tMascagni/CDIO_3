package cdio.computervision;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.ArrayList;


/*
    This demo classed is only for reference.
    For picking out QR-codes from images, used QRDetector.java
 */

public class demo {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }


    public static void main(String[] args) {
	// write your code here
       //init();
           String path = "/home/pil/Desktop/ringe/mine";
           File folder = new File(path);
            int j = 0;
           for(File fileEntry : folder.listFiles()) {
               if(fileEntry.getName().toLowerCase().contains(".jpg")) {
                   QRDetector qr = new QRDetector(fileEntry.getAbsolutePath());
                   ArrayList<Double> var = new ArrayList<>();
                   ArrayList<Mat> res = qr.processAll(var);


                    int i = 0;
                   for(Mat r : res) {
                       System.out.println(j + "" + i + ", " + var.get(i) );
                       Imgcodecs.imwrite("/home/pil/Desktop/ringe/daniel/output/"+ j + i + ".jpeg", r);
                       i++;
                   }
               }
               j++;
           }
    }
}
