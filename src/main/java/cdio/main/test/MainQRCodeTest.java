package cdio.main.test;

import cdio.handler.QRCodeException;
import cdio.handler.QRCodeHandler;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class MainQRCodeTest {

    public static void main(String[] args) {
        QRCodeHandler handler = new QRCodeHandler();


        System.out.println("Starting QRCode TEST!\n");

        /* REAL IMAGE */
        //   BufferedImage imgLocal0 = QRCodeHandler.getImageLocal("qr_code_test/1.jpg0_fixed.jpg");
        BufferedImage imgLocal01 = null;
        try {
            imgLocal01 = optimize(QRCodeHandler.getImageLocal("qr_code_test/1.jpg0.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //     BufferedImage imgLocal1 = QRCodeHandler.getImageLocal("qr_code_test/1.jpg1.jpg");
        //     BufferedImage imgLocal2 = QRCodeHandler.getImageLocal("qr_code_test/2.jpg0.jpg");
        //    BufferedImage imgLocal3 = QRCodeHandler.getImageLocal("qr_code_test/2.jpg1.jpg");
        //   BufferedImage imgLocal4 = QRCodeHandler.getImageLocal("qr_code_test/2.jpg2.jpg");
        //    BufferedImage imgLocal5 = QRCodeHandler.getImageLocal("qr_code_test/5.jpg0.jpg");


        /* WEB IMAGE
        BufferedImage imgRemote0 = QRCodeHandler.getImageRemote("http://static.jeffbullas.com/wp-content/uploads/2012/05/Jeff-Bullas-scantogram1.jpg");
        BufferedImage imgRemote1 = QRCodeHandler.getImageRemote("https://thumbor.forbes.com/thumbor/960x0/smart/https%3A%2F%2Fb-i.forbesimg.com%2Frogerdooley%2Ffiles%2F2013%2F10%2Fqr-kittens.jpg");
        BufferedImage imgRemote2 = QRCodeHandler.getImageRemote("https://lh3.googleusercontent.com/-zp1P6fiT9F80i7uTbnWO84h4fWmoFXOxFavpqq63MKLzJzUeVwpIzz29G8drleQlg=h310");
        BufferedImage imgRemote3 = QRCodeHandler.getImageRemote("https://i2.wp.com/farm6.static.flickr.com/5051/5535825483_ce9c8fc321_z.jpg?resize=480%2C640&ssl=1");
        BufferedImage imgRemote4 = QRCodeHandler.getImageRemote("http://www.labeljoy.com/images/how-to/best-practices/image009.png");
        BufferedImage imgRemote5 = QRCodeHandler.getImageRemote("https://richmediadp.files.wordpress.com/2011/08/qr-code.jpg");
        */



        System.out.println("REAL IMAGES:");
        try {
            System.out.println(handler.scanImage(imgLocal01));
        } catch (QRCodeException e) {
            e.printStackTrace();
        }
/*
        try {
            System.out.println(handler.scanImage(imgLocal01));
        } catch (QRCodeException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(handler.scanImage(imgLocal1));
        } catch (QRCodeException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(handler.scanImage(imgLocal2));
        } catch (QRCodeException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(handler.scanImage(imgLocal3));
        } catch (QRCodeException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(handler.scanImage(imgLocal4));
        } catch (QRCodeException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(handler.scanImage(imgLocal5));
        } catch (QRCodeException e) {
            e.printStackTrace();
        }
        */


/*
        System.out.println("WEB IMAGES:");
        try {
            System.out.println(handler.scanImage(imgRemote0));
        } catch (QRCodeException e) {
            e.printStackTrace();
        }
        try {
            long start = System.currentTimeMillis();
            System.out.println(handler.scanImage(imgRemote1));
            long end = System.currentTimeMillis();
            System.out.println("Time: " + (end - start));
        } catch (QRCodeException e) {
            e.printStackTrace();
        }
        try {
            long start = System.currentTimeMillis();
            System.out.println(handler.scanImage(imgRemote2));
            long end = System.currentTimeMillis();
            System.out.println("Time: " + (end - start));
        } catch (QRCodeException e) {
            e.printStackTrace();
        }
        try {
            long start = System.currentTimeMillis();
            System.out.println(handler.scanImage(imgRemote3));
            long end = System.currentTimeMillis();

            System.out.println("Time: " + (end - start));

        } catch (QRCodeException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(handler.scanImage(imgRemote4));
        } catch (QRCodeException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(handler.scanImage(imgRemote5));
        } catch (QRCodeException e) {
            e.printStackTrace();
        }
*/


    }


    private static BufferedImage optimize(BufferedImage img) throws IOException {
        Mat con = bufferedImageToMat(img);

        int erosion_size = 5;
        Mat dis = new Mat();
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(2*erosion_size + 1, 2*erosion_size+1));
        Imgproc.erode(con, dis, element);

        return Mat2BufferedImage(dis);
    }

    public static Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }

    public static BufferedImage Mat2BufferedImage(Mat matrix)throws IOException {
        MatOfByte mob=new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, mob);
        return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
    }
}
