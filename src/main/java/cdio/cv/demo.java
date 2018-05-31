package cdio.cv;

import org.opencv.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;


/*
    This demo classed is only for reference.
    For picking out QR-codes from images, used QRDetector.java
 */

public class demo {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }


    public static void main(String[] args) {
        CVHelper cvHelper = new CVHelper();

	// write your code here
       //init();
        String path = "C:\\Users\\phenotype\\Documents\\CDIO_3\\qr_code_test\\pil";
           File folder = new File(path);
            int j = 0;
           for(File fileEntry : folder.listFiles()) {
               if(fileEntry.getName().toLowerCase().contains(".jpg")) {

                   QRDetector qr = new QRDetector(fileEntry.getAbsolutePath());
                   ArrayList<Double> var = new ArrayList<>();
                   ArrayList<Mat> res = qr.processAll(var);

/*
                    int i = 0;
                   for(Mat r : res) {
                       System.out.println(j + "" + i + ", " + var.get(i) );
                       Imgcodecs.imwrite("/home/pil/Desktop/ringe/daniel/output/"+ j + i + ".jpeg", r);
                       i++;
                   }

				   */
                   System.out.println("Bin img: " + qr.binImg.type());
                   System.out.println("Org img: " + qr.orgImg.type());

                   // Convert binImg and orgImg to bufferedImages
                   BufferedImage buffBinImg = cvHelper.mat2buf(qr.binImg);
                   BufferedImage buffOrgImg = cvHelper.mat2buf(qr.orgImg);

                   // Show the images
                   displayImage(buffBinImg); // Converted img (black)
                   displayImage(buffOrgImg); // Original img
               }
               j++;
           }
    }

    public static void displayImage(Image img2) {

        //BufferedImage img=ImageIO.read(new File("/HelloOpenCV/lena.png"));
        ImageIcon icon=new ImageIcon(img2);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(img2.getWidth(null)+50, img2.getHeight(null)+50);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
