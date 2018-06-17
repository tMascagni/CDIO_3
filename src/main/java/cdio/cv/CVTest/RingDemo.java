package cdio.cv.CVTest;

import cdio.cv.CVHelper;
import cdio.cv.RingDetector;
import cdio.cv.RingImg;
import org.opencv.core.Core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class RingDemo {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }


    public static void main(String[] args) {
        CVHelper cvHelper = new CVHelper();


        String path = "qr_code_test/pil";
        File folder = new File(path);
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.getName().toLowerCase().contains(".jpg")) {

                RingDetector ringDetector = new RingDetector(fileEntry.getAbsolutePath());
                ArrayList<RingImg> res = ringDetector.processAll(ringDetector.orgImg);

                BufferedImage buffOrgImg = cvHelper.mat2buf(ringDetector.orgImg);
                cvHelper.displayImage(buffOrgImg); // Original img
                cvHelper.displayImage(cvHelper.mat2buf(ringDetector.binImg));
                cvHelper.displayImage(cvHelper.mat2buf(ringDetector.grayImg));

            }
        }

    }

}
