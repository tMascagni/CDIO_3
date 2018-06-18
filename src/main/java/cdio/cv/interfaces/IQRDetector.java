package cdio.cv.interfaces;

import cdio.model.QRImg;
import org.opencv.core.Mat;

import java.util.ArrayList;

public interface IQRDetector {
    ArrayList<QRImg> processAll(Mat mat);
}