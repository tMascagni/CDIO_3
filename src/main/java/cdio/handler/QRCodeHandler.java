package cdio.handler;

import cdio.model.QRCodeData;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.qrcode.QRCodeReader;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

public class QRCodeHandler {

    private String qrCodeValue;
    private int orientation;
    private Result detectionResult;
    private int qrCodeWidth;
    private int qrCodeHeight;

    private final QRCodeReader reader;

    public QRCodeHandler() {
        reader = new QRCodeReader();
    }

    public QRCodeData scanImage(final BufferedImage image) throws QRCodeException {
        /* Try to detect QR code */
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Map<DecodeHintType, Object> hintTypeMap = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);

        hintTypeMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
     //   hintTypeMap.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);
   //     hintTypeMap.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);

        try {
            //reader.setHints(hintTypeMap);
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
            throw new QRCodeException("Failed to scan QR Code!");
        }

        QRCodeData data = new QRCodeData(qrCodeWidth, qrCodeHeight, qrCodeValue, orientation);

        System.out.println("Data: " + data);

        return data;
    }

    private static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1)
            os.write(b, 0, length);

        is.close();
        os.close();
    }

    public static BufferedImage getImageLocal(String path) {
        BufferedImage img = null;

        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
           // e.printStackTrace();
            System.err.println("some shit");
        }

        return img;
    }

    public static BufferedImage getImageRemote(String url) {
        String path = "qr_code_test/test.jpg";

        try {
            saveImage(url, path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }

}