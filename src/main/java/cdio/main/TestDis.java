package cdio.main;

import cdio.cv.QRDetector;
import cdio.cv.QRImg;
import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.handler.QRCodeHandler;
import cdio.handler.interfaces.IQRCodeHandler;
import cdio.ui.MainFrame;

import javax.swing.*;

public final class TestDis {

    private final static IDroneCommander droneCommander = DroneCommander.getInstance();

    private static QRDetector qrDetector = new QRDetector();

    public static void main(String[] args) throws IDroneCommander.DroneCommanderException {

        IQRCodeHandler qrCodeHandler = QRCodeHandler.getInstance();
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneCommander);
        });

        try {
            droneCommander.startDrone();
            droneCommander.initDrone();

            droneCommander.takeOffDrone();

            droneCommander.hoverDrone(8000);


            droneCommander.adjustToCenterFromQR(50);


            int timer = 200;
            while(droneCommander.getLatestReceivedImage() == null);
            while (qrCodeHandler.scanImageForAll(droneCommander.getLatestReceivedImage(), droneCommander).size() == 0) {
                timer--;
                if (timer == 0) {
                    System.out.println("venter på qr kode");
                    timer = 100;
                }
            }

            QRImg qrImg = null;
            while (qrImg == null) {
                qrImg = qrCodeHandler.scanImageForBest(droneCommander.getLatestReceivedImage(), droneCommander);
            }
            double dist = qrImg.getDistance();

            while (dist > 80) {
                droneCommander.flyForward(400);
                droneCommander.adjustToCenterFromQR(50);
                droneCommander.hoverDrone(200);
                droneCommander.addMessage("Dist: " + dist);
                QRImg qrImg2 = null;
                while (qrImg2 == null) {
                    qrImg2 = qrCodeHandler.scanImageForBest(droneCommander.getLatestReceivedImage(), droneCommander);
                }
                dist = qrImg2.getDistance();
            }


            while (droneCommander.getAltitude() <= 1450) {
                droneCommander.flyUp(350);
            }
            droneCommander.flyForward(3000);
            droneCommander.flyBackward(1000);
            droneCommander.hoverDrone(2000);
            droneCommander.landDrone();

        } catch (IQRCodeHandler.QRCodeHandlerException e) {
            e.printStackTrace();
        }

        //   } catch (IDroneCommander.DroneCommanderException e) {
        //       e.printStackTrace();
        //   }
/*
        double dist = 0.0;
        while (true) {
            while (droneCommander.getLatestReceivedImage() == null);
            QRImg qrImg = qrCodeHandler.scanImageForBest(droneCommander.getLatestReceivedImage(), droneCommander);
            if(qrImg != null) {
                dist = qrImg.getDistance();
                droneCommander.addMessage("Dist: " + dist);
                System.out.println("Dist: " + dist);
            }
        }

        */
    }
}
