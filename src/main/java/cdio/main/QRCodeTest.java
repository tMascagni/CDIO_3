package cdio.main;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class QRCodeTest {

    private String qrCodeValue;
    private String orientation;
    private Result detectionResult;

    public static void main(String[] args) {

        QRCodeTest testObj = new QRCodeTest();
        BufferedImage img1 = testObj.getImg("qr_2.jpg");
        testObj.scanImageForQR(img1);

        /*
        testObj.startIMG("https://www.qrstuff.com/images/sample.png");
        testObj.startIMG("http://static.jeffbullas.com/wp-content/uploads/2012/05/Jeff-Bullas-scantogram1.jpg");
        testObj.startIMG("https://thumbor.forbes.com/thumbor/960x0/smart/https%3A%2F%2Fb-i.forbesimg.com%2Frogerdooley%2Ffiles%2F2013%2F10%2Fqr-kittens.jpg");
        testObj.startIMG("https://lh3.googleusercontent.com/-zp1P6fiT9F80i7uTbnWO84h4fWmoFXOxFavpqq63MKLzJzUeVwpIzz29G8drleQlg=h310");
        testObj.startIMG("https://i2.wp.com/farm6.static.flickr.com/5051/5535825483_ce9c8fc321_z.jpg?resize=480%2C640&ssl=1");
        testObj.startIMG("http://www.labeljoy.com/images/how-to/best-practices/image009.png");
        testObj.startIMG("https://richmediadp.files.wordpress.com/2011/08/qr-code.jpg");
        */
    }

    public BufferedImage getImg(String path) {

        BufferedImage img = null;

        try {
            img = ImageIO.read(new File(path));
        } catch (IOException ignored) {

        }

        return img;
    }

    public String scanImageForQR(final BufferedImage image) {
        // try to detect QR code
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        // decode the barcode (if only QR codes are used, the QRCodeReader might be a better choice)
        MultiFormatReader reader = new MultiFormatReader();

        try {
            detectionResult = reader.decode(bitmap);

            qrCodeValue = detectionResult.getText();

            // System.out.println("QRCode Text: " + result.getText());

            // for (int i=0; i < points.length; i++)
            // {
            // System.out.println("QRCode Point # " + i + ": " +
            // points[i].getX() + "/" + points[i].getY());
            // }

            ResultPoint[] points = detectionResult.getResultPoints();
            ResultPoint a = points[1]; // top-left
            ResultPoint b = points[2]; // top-right
            ResultPoint c = points[0]; // bottom-left
            ResultPoint d = points[3]; // alignment point (bottom-right)

            // Find the degree of the rotation that is needed

            double z = Math.abs(a.getX() - b.getX());
            double x = Math.abs(a.getY() - b.getY());
            double theta = Math.atan(x / z); // degree in rad (+- PI/2)

            theta = theta * (180 / Math.PI); // convert to degree

            if ((b.getX() < a.getX()) && (b.getY() > a.getY())) { // code turned more than 90� clockwise
                theta = 180 - theta;
            } else if ((b.getX() < a.getX()) && (b.getY() < a.getY())) { // code turned more than 180� clockwise
                theta = 180 + theta;
            } else if ((b.getX() > a.getX()) && (b.getY() < a.getY())) { // code turned more than 270 clockwise
                theta = 360 - theta;
            }

            orientation = (int) theta + " grader";

            int qrCodeWidth = (int) b.getX() - (int) a.getX();

            System.out.println("###############################################");
            System.out.println("QR Code Width: " + qrCodeWidth);
        } catch (ReaderException e) {
            // no code found.
            detectionResult = null;
            orientation = "n/a";
            qrCodeValue = "n/a";
        }

        System.out.println("DetectionResult: " + detectionResult + ", Orientation: " + orientation + ", QrCodeValue: " + qrCodeValue);
        System.out.println("###############################################");

        return qrCodeValue;
    }

    /*
    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }
    */

}

