package cdio.main.test;

import cdio.handler.QRCodeException;
import cdio.handler.QRCodeHandler;

import java.awt.image.BufferedImage;

public class MainQRCodeTest {

    public static void main(String[] args) throws QRCodeException {
        QRCodeHandler handler = new QRCodeHandler();

        /* REAL IMAGE */
        BufferedImage imgLocal0 = QRCodeHandler.getImageLocal("qr_code_test/qr_real_0.jpg");
        BufferedImage imgLocal1 = QRCodeHandler.getImageLocal("qr_code_test/qr_real_1.jpg");
        BufferedImage imgLocal2 = QRCodeHandler.getImageLocal("qr_code_test/qr_real_2.jpg");
        BufferedImage imgLocal3 = QRCodeHandler.getImageLocal("qr_code_test/qr_real_3.jpg");

        /* WEB IMAGE */
        BufferedImage imgRemote0 = QRCodeHandler.getImageRemote("http://static.jeffbullas.com/wp-content/uploads/2012/05/Jeff-Bullas-scantogram1.jpg");
        BufferedImage imgRemote1 = QRCodeHandler.getImageRemote("https://thumbor.forbes.com/thumbor/960x0/smart/https%3A%2F%2Fb-i.forbesimg.com%2Frogerdooley%2Ffiles%2F2013%2F10%2Fqr-kittens.jpg");
        BufferedImage imgRemote2 = QRCodeHandler.getImageRemote("https://lh3.googleusercontent.com/-zp1P6fiT9F80i7uTbnWO84h4fWmoFXOxFavpqq63MKLzJzUeVwpIzz29G8drleQlg=h310");
        BufferedImage imgRemote3 = QRCodeHandler.getImageRemote("https://i2.wp.com/farm6.static.flickr.com/5051/5535825483_ce9c8fc321_z.jpg?resize=480%2C640&ssl=1");
        BufferedImage imgRemote4 = QRCodeHandler.getImageRemote("http://www.labeljoy.com/images/how-to/best-practices/image009.png");
        BufferedImage imgRemote5 = QRCodeHandler.getImageRemote("https://richmediadp.files.wordpress.com/2011/08/qr-code.jpg");

        System.out.println("Starting QRCode TEST!\n");


        System.out.println("REAL IMAGES:");
        System.out.println(handler.scanImage(imgLocal0));
        System.out.println(handler.scanImage(imgLocal1));
        System.out.println(handler.scanImage(imgLocal2));
        System.out.println(handler.scanImage(imgLocal3));

        System.out.println("WEB IMAGES:");
        System.out.println(handler.scanImage(imgRemote0));
        System.out.println(handler.scanImage(imgRemote1));
        System.out.println(handler.scanImage(imgRemote2));
        System.out.println(handler.scanImage(imgRemote3));
        System.out.println(handler.scanImage(imgRemote4));
        System.out.println(handler.scanImage(imgRemote5));

    }

}
