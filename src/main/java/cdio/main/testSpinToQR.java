package cdio.main;

import cdio.cv.QRDetector;
import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

import javax.swing.*;

public class testSpinToQR {
    private final static IDroneCommander droneCommander = DroneCommander.getInstance();

    private static QRDetector qrDetector = new QRDetector();

    public static void main(String[] args) throws IDroneCommander.DroneCommanderException {

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneCommander);
        });

        droneCommander.startDrone();
        droneCommander.initDrone();

        droneCommander.takeOffDrone();

        droneCommander.hoverDrone(8000);
        droneCommander.spinToQR();
        droneCommander.landDrone();

    }
}
