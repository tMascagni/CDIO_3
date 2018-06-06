package cdio.cv.interfaces;

import cdio.cv.QRImg;
import org.opencv.core.Mat;

import java.util.ArrayList;

public interface ICV {
    ArrayList<QRImg> processAll(Mat mat);
}