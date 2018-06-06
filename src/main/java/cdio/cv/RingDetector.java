package cdio.cv;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import static javax.swing.text.StyleConstants.Size;

public class RingDetector {
    private CVHelper cvHelper = new CVHelper();

    public Mat orgImg, grayImg, binImg;

    public RingDetector() {

    }

    public RingDetector(String filePath) {
        orgImg = Imgcodecs.imread(filePath);
    }
    public ArrayList<RingImg> processAll(Mat mat) {
        orgImg = mat;
        getGray();
        ArrayList<RingImg> rings = new ArrayList<>();


        for(int i = 0; i < 5; i++) {

            thresholding(120, 80);
            Erode(1);
            Dilate(2);
            Erode(2);

            Imgproc.putText(binImg, "Passes: " + i, new Point(0, 300),
                Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(200, 50, 170), 1, Core.LINE_8, false);

            rings.add(new RingImg(binImg.clone()));
        }
        rings.add(new RingImg(grayImg.clone()));
        rings.add(new RingImg(orgImg.clone()));

        return rings;
    }

    private void Dilate(int size) {
        Mat element = Imgproc.getStructuringElement( Imgproc.MORPH_RECT,
                new Size( 2*size + 1, 2*size+1 ),
                new Point( size, size ) );
        Imgproc.dilate(binImg, binImg, element);
    }

    private void Erode(int size) {
        Mat element = Imgproc.getStructuringElement( Imgproc.MORPH_RECT,
                new Size( 2*size + 1, 2*size+1 ),
                new Point( size, size ) );
        Imgproc.erode(binImg, binImg, element);
    }



    private void thresholding(int thres, int range) {
        binImg = new Mat();
        Imgproc.threshold(grayImg, binImg, thres, thres+range, Imgproc.THRESH_BINARY);
    }

    private void getGray() {
        Mat hsvImg = new Mat();
        ArrayList<Mat> channels = new ArrayList<>();
        Imgproc.cvtColor(orgImg, hsvImg, Imgproc.COLOR_BGR2HSV);

        Core.split(hsvImg, channels);
        grayImg = channels.get(1);
    }

}
