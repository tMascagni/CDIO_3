package cdio.handler.interfaces;

import cdio.cv.QRImg;
import cdio.drone.interfaces.IDroneCommander;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public interface IQRCodeHandler {

    ArrayList<QRImg> scanImageForAll(final BufferedImage image, IDroneCommander droneCommander) throws QRCodeHandlerException;

    QRImg scanImageForBest(final BufferedImage image, IDroneCommander droneCommander) throws QRCodeHandlerException;
    BufferedImage getImageLocal(String path) throws QRCodeHandlerException;
    BufferedImage getImageRemote(String url) throws QRCodeHandlerException;
    void saveImage(String imageUrl, String destinationFile) throws QRCodeHandlerException;

    class QRCodeHandlerException extends Exception {

        public QRCodeHandlerException(String msg) {
            super(msg);
        }

    }

}