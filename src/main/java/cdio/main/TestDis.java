package cdio.main;

import cdio.cv.QRDetector;
import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.handler.QRCodeHandler;
import cdio.handler.interfaces.IQRCodeHandler;
import cdio.ui.MainFrame;

import javax.swing.*;

public final class TestDis {

    private final static IDroneCommander droneCommander = DroneCommander.getInstance();

    private static QRDetector qrDetector = new QRDetector();

    public static void main(String[] args) throws IQRCodeHandler.QRCodeHandlerException, IDroneCommander.DroneCommanderException {

        IQRCodeHandler qrCodeHandler = QRCodeHandler.getInstance();
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneCommander);
        });


               /* droneCommander.startDrone();
                droneCommander.initDrone();

                droneCommander.takeOffDrone();

                droneCommander.hoverDrone(8000);
*/

        int timer = 200;
        while(droneCommander.getLatestReceivedImage() == null);

        while (qrCodeHandler.scanImageForAll(droneCommander.getLatestReceivedImage(), droneCommander).size() == 0) {
            timer--;
            if (timer == 0) {
                System.out.println("venter på qr kode");
                timer = 100;
            }
        }

        double dist = qrCodeHandler.scanImageForBest(droneCommander.getLatestReceivedImage(), droneCommander).getDistance();
        while (dist > 70) {
            droneCommander.flyForward(200);
            droneCommander.hoverDrone(200);
            droneCommander.addMessage("Dist: " + dist);
            dist = qrCodeHandler.scanImageForBest(droneCommander.getLatestReceivedImage(), droneCommander).getDistance();
        }

        droneCommander.flyBackward(500);
        droneCommander.hoverDrone(2000);
        droneCommander.landDrone();

        //   } catch (IDroneCommander.DroneCommanderException e) {
        //       e.printStackTrace();
        //   }

    }
}
