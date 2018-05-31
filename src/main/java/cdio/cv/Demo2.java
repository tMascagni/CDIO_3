package cdio.cv;

import org.opencv.core.Core;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Demo2 {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

        try {
            BufferedImage img = ImageIO.read(new File("C:\\Users\\phenotype\\Documents\\CDIO_3\\qr_code_test\\pil\\1.jpg"));

            displayImage(img);

            QRDetector qr = new QRDetector(img);
            BufferedImage newImg = qr.processSingleImg(img);

            displayImage(newImg);

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public static void displayImage(Image img2) {
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
    }

}