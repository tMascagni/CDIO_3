package cdio.cv.CVTest;

import cdio.cv.CVHelper;
import cdio.cv.QRDetector;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Demo2 {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        CVHelper cvHelper = new CVHelper();

        try {
            BufferedImage img = ImageIO.read(new File("qr_code_test/pil/1.jpg"));

            cvHelper.displayImage(img);

            QRDetector qr = new QRDetector(img);
            BufferedImage newImg = qr.processSingleImg(img);

            cvHelper.displayImage(img);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}