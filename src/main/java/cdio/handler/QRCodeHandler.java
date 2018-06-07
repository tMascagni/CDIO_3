package cdio.handler;

import cdio.cv.CVHelper;
import cdio.cv.QRDetector;
import cdio.cv.QRImg;
import cdio.drone.interfaces.IDroneCommander;
import cdio.handler.interfaces.IQRCodeHandler;
import cdio.model.QRCodeData;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class QRCodeHandler implements IQRCodeHandler {
    private QRDetector qrDetector = new QRDetector();
    private CVHelper helper = new CVHelper();

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
    public ArrayList<QRImg> scanImageForAll(final BufferedImage image, IDroneCommander droneCommander) throws QRCodeHandlerException {
        qrDetector.orgImg = helper.buf2mat(image);
        ArrayList<QRImg> qrCodes;
        qrCodes = qrDetector.processAll(qrDetector.orgImg);

        for (QRImg img : qrCodes) {
            img.setQrCodeData(scanImgForQrCode(img.getImg(), droneCommander));
        }

        return qrCodes;
    }

    public QRImg scanImageForBest(final BufferedImage image, IDroneCommander droneCommander) throws QRCodeHandlerException {
        ArrayList<QRImg> qrCodes = scanImageForAll(image, droneCommander);
        QRImg qrImg = qrDetector.findBest(qrCodes);

        if (qrImg == null)
            throw new QRCodeHandlerException("NO QR Code found!");

        return qrImg;
    }

    private QRCodeData scanImgForQrCode(final Mat mat, IDroneCommander droneCommander) throws QRCodeHandlerException {
        /* Try to detect QR code */
        LuminanceSource source = new BufferedImageLuminanceSource(helper.mat2buf(mat));
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result detectionResult = reader.decode(bitmap);
            String qrCodeValue = detectionResult.getText();

            ResultPoint[] points = detectionResult.getResultPoints();
            ResultPoint a = points[1]; // top-left
            ResultPoint b = points[2]; // top-right
            ResultPoint c = points[0]; // bottom-left

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

            int orientation = (int) theta;

            return new QRCodeData(qrCodeValue, orientation, droneCommander.getCorrectYaw(droneCommander.getYaw()));
        } catch (ReaderException e) {
            return null;
            //throw new QRCodeHandlerException("Failed to scan for QR Code!");
        }
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