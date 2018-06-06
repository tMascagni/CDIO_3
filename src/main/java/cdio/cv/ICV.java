package cdio.cv;

import org.opencv.core.Mat;

import java.util.ArrayList;

public interface ICV {
    ArrayList<QRImg> processAll(Mat mat);
}