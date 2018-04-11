package cdio.computervision;

import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import static org.opencv.core.CvType.*;

public class CVHelper {
    Mat buf2mat(BufferedImage in) {
        Mat out = null;
        if(in.getType() == BufferedImage.TYPE_3BYTE_BGR) {
            out = Mat.zeros(in.getHeight(), in.getWidth(), CV_8UC3);
        } else {
            out = Mat.zeros(in.getHeight(), in.getWidth(), CV_8U);
        }
        byte[] pixels = ((DataBufferByte) in.getRaster().getDataBuffer()).getData();
        out.put(0, 0, pixels);
        return out;
    }

    BufferedImage mat2buf(Mat in) {
        BufferedImage out = null;

        if(in.type() == CV_8UC3) {
            out = new BufferedImage(in.width(), in.height(), BufferedImage.TYPE_3BYTE_BGR);
        } else {
            out = new BufferedImage(in.width(), in.height(), BufferedImage.TYPE_BYTE_GRAY);
        }

        byte[] data = new byte[in.height() * in.width() * (int)in.elemSize()];
        in.get(0, 0, data);
        out.getRaster().setDataElements(0, 0, in.width(), in.height(), data);
        return out;
    }
}
