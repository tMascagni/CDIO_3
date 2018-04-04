package cdio.computervision;

import org.opencv.core.*;

import java.io.File;


/*
    This demo classed is only for reference.
    For picking out QR-codes from images, used QRDetector.java
 */

public class demo {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }


    public static void main(String[] args) {
	// write your code here
       //init();
           String path = "/home/pil/Desktop/ringe/daniel";
           File folder = new File(path);

           for(File fileEntry : folder.listFiles()) {
               if(fileEntry.getName().toLowerCase().contains(".jpg")) {
                   QRDetector qr = new QRDetector(fileEntry.getAbsolutePath());
                   qr.processAll();
               }
           }
    }
}
