package cdio.computervision;

import org.opencv.core.Mat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public interface ICV {

    CVHelper cvHelper = new CVHelper();

    public double angleOfQRCode(QRImg input);
    public ArrayList<QRImg> sortQR(ArrayList<QRImg> src);
    public void findQR(ArrayList<QRImg> dst, Mat scr, ContourTree ct);
    public BufferedImage processSingleImg(Image dstImg);
    public ArrayList<QRImg> processAll(Mat mat);


}
