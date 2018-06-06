package cdio.computervision;

import org.opencv.core.Mat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public interface ICV {

    public ArrayList<QRImg> processAll(Mat mat);

}
