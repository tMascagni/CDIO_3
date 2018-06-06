package cdio.cv;

import org.opencv.core.Core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class RingDemo {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) {
        CVHelper cvHelper = new CVHelper();

	// write your code here
       //init();

        String path = "qr_code_test/pil";
        File folder = new File(path);
        int j = 0;
        for(File fileEntry : folder.listFiles()) {
            if(fileEntry.getName().toLowerCase().contains(".jpg")) {

                RingDetector r = new RingDetector(fileEntry.getAbsolutePath());
                ArrayList<RingImg> res = r.processAll(r.orgImg);

                for(RingImg ring : res) {
                    BufferedImage buffOrgImg = cvHelper.mat2buf(ring.getImg());
                    cvHelper.displayImage(buffOrgImg); // Original img
                }

            }
        }

    }
}
