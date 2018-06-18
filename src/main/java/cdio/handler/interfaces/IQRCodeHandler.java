package cdio.handler.interfaces;

import cdio.controller.interfaces.IDroneCommander;
import cdio.model.QRImg;
import cdio.model.RingImg;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public interface IQRCodeHandler {

    ArrayList<QRImg> scanImageForAll(final BufferedImage image, IDroneCommander droneCommander) throws QRCodeHandlerException;

    RingImg findRing(BufferedImage image, QRImg qr);
    QRImg scanImageForBest(final BufferedImage image, IDroneCommander droneCommander) throws QRCodeHandlerException;
    BufferedImage getImageLocal(String path) throws QRCodeHandlerException;
    BufferedImage getImageRemote(String url) throws QRCodeHandlerException;
    void saveImage(String imageUrl, String destinationFile) throws QRCodeHandlerException;

    QRImg detectQR(IDroneCommander droneCommander);


    class QRCodeHandlerException extends Exception {

        public QRCodeHandlerException(String msg) {
            super(msg);
        }

    }

}