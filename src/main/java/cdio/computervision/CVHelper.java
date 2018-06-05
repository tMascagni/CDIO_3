package cdio.computervision;

import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import static org.opencv.core.CvType.CV_8U;
import static org.opencv.core.CvType.CV_8UC3;

public class CVHelper {

   public Mat buf2mat(BufferedImage in) {
        Mat out = null;
        if (in.getType() == BufferedImage.TYPE_3BYTE_BGR) {
            out = Mat.zeros(in.getHeight(), in.getWidth(), CV_8UC3);
        } else {
            out = Mat.zeros(in.getHeight(), in.getWidth(), CV_8U);
        }
        byte[] pixels = ((DataBufferByte) in.getRaster().getDataBuffer()).getData();
        out.put(0, 0, pixels);
        return out;
    }

   public BufferedImage mat2buf(Mat in) {
        BufferedImage out = null;

        if (in.type() == CV_8UC3) {
            out = new BufferedImage(in.width(), in.height(), BufferedImage.TYPE_3BYTE_BGR);
        } else {
            out = new BufferedImage(in.width(), in.height(), BufferedImage.TYPE_BYTE_GRAY);
        }

        byte[] data = new byte[in.height() * in.width() * (int) in.elemSize()];
        in.get(0, 0, data);
        out.getRaster().setDataElements(0, 0, in.width(), in.height(), data);
        return out;
    }

    public JFrame displayImage(Image img2) {
        //BufferedImage img=ImageIO.read(new File("/HelloOpenCV/lena.png"));
        ImageIcon icon = new ImageIcon(img2);
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(img2.getWidth(null) + 50, img2.getHeight(null) + 50);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

}