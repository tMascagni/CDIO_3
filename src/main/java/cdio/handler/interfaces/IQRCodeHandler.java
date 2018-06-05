package cdio.handler.interfaces;

import cdio.drone.interfaces.IDroneCommander;
import cdio.model.QRCodeData;

import java.awt.image.BufferedImage;

public interface IQRCodeHandler {

    QRCodeData scanImage(final BufferedImage image, IDroneCommander droneCommander) throws QRCodeHandlerException;
    BufferedImage getImageLocal(String path) throws QRCodeHandlerException;
    BufferedImage getImageRemote(String url) throws QRCodeHandlerException;
    void saveImage(String imageUrl, String destinationFile) throws QRCodeHandlerException;

    class QRCodeHandlerException extends Exception {

        public QRCodeHandlerException(String msg) {
            super(msg);
        }

    }

}