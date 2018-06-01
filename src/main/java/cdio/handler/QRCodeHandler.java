package cdio.handler;

import cdio.handler.interfaces.IQRCodeHandler;
import cdio.model.QRCodeData;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class QRCodeHandler implements IQRCodeHandler {

    private String qrCodeValue;
    private int orientation;
    private Result detectionResult;
    private int qrCodeWidth;
    private int qrCodeHeight;

    private final QRCodeReader reader = new QRCodeReader();

    private static IQRCodeHandler instance;

    private QRCodeHandler() {

    }

    static {
        try {
            instance = new QRCodeHandler();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate QRCodeHandler Singleton Instance!");
        }
    }

    public static synchronized IQRCodeHandler getInstance() {
        return instance;
    }

    @Override
    public QRCodeData scanImage(final BufferedImage image) throws QRCodeHandlerException {
        /* Try to detect QR code */
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            detectionResult = reader.decode(bitmap);
            qrCodeValue = detectionResult.getText();

            ResultPoint[] points = detectionResult.getResultPoints();
            ResultPoint a = points[1]; // top-left
            ResultPoint b = points[2]; // top-right
            ResultPoint c = points[0]; // bottom-left
            ResultPoint d = null;

            if (points.length == 4)
                d = points[3]; // alignment point (bottom-right) // THIS CAUSES MASSIEV

            // Find the degree of the rotation that is needed
            double z = Math.abs(a.getX() - b.getX());
            double x = Math.abs(a.getY() - b.getY());
            double theta = Math.atan(x / z); // degree in rad (+- PI/2)

            theta = theta * (180 / Math.PI); // convert to degree

            if ((b.getX() < a.getX()) && (b.getY() > a.getY())) { // code turned more than 90 deg clockwise
                theta = 180 - theta;
            } else if ((b.getX() < a.getX()) && (b.getY() < a.getY())) { // code turned more than 180 deg clockwise
                theta = 180 + theta;
            } else if ((b.getX() > a.getX()) && (b.getY() < a.getY())) { // code turned more than 270 clockwise
                theta = 360 - theta;
            }

            orientation = (int) theta;

            qrCodeWidth = (int) b.getX() - (int) a.getX();

            if (d != null)
                qrCodeHeight = (int) d.getX() - (int) c.getX();
            else
                qrCodeHeight = -1;

        } catch (ReaderException e) {
            throw new QRCodeHandlerException("Failed to scan QR Code!");
        }

        return new QRCodeData(qrCodeWidth, qrCodeHeight, qrCodeValue, orientation);
    }

    @Override
    public BufferedImage getImageLocal(String path) throws QRCodeHandlerException {
        BufferedImage img = null;

        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            // e.printStackTrace();
            System.err.println("some shit");
        }

        return img;
    }

    @Override
    public BufferedImage getImageRemote(String url) throws QRCodeHandlerException {
        String path = "qr_code_test/test.jpg";

        saveImage(url, path);

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }

    @Override
    public void saveImage(String imageUrl, String destinationFile) throws QRCodeHandlerException {
        try {
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1)
                os.write(b, 0, length);

            is.close();
            os.close();
        } catch (IOException e) {
            throw new QRCodeHandlerException(e.getMessage());
        }
    }

}