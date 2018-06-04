package cdio.computervision;

import org.opencv.core.Mat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public interface ICV {

    CVHelper cvHelper = new CVHelper();

    public double angleOfQRCode(Mat input);
    public ArrayList<Mat> sortQR(ArrayList<Mat> src);
    public void findQR(ArrayList<Mat> dst, Mat scr, ContourTree ct);
    public BufferedImage processSingleImg(Image dstImg);
    public ArrayList<Mat> processAll(Mat mat);


}
