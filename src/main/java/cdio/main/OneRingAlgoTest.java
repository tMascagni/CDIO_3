package cdio.main;

import cdio.cv.QRDetector;
import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.handler.QRCodeHandler;
import cdio.handler.interfaces.IQRCodeHandler;
import cdio.ui.MainFrame;

import javax.swing.*;

public class OneRingAlgoTest {

    private final static IDroneCommander droneCommander = DroneCommander.getInstance();
    private final static IQRCodeHandler qrCodeHandler = QRCodeHandler.getInstance();

    private static QRDetector qrDetector = new QRDetector();

    public static void main(String[] args) throws IDroneCommander.DroneCommanderException {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneCommander);
        });

        droneCommander.startDrone();
        droneCommander.initDrone();
        droneCommander.takeOffDrone();
        droneCommander.hoverDrone(5000);
        while (droneCommander.getLatestReceivedImage() == null) ; // venter på videofeed

        droneCommander.adjustToCenterFromQR();

        droneCommander.flyToTargetQRCode(true); // fly hen til ring

        droneCommander.flyUpToAltitude(1450); // flyv op i højde af ringen

        droneCommander.flyForward(3000); // flyv gennem ringen

        droneCommander.hoverDrone(2000);
        droneCommander.landDrone();

    }
}
