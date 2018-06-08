package cdio.main;

import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

import javax.swing.*;

public class CenterFromQRTest {
    private final static IDroneCommander droneCommander = DroneCommander.getInstance();

    public static void main(String[] args) throws IDroneCommander.DroneCommanderException {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneCommander);
        });

        droneCommander.startDrone();
        droneCommander.initDrone();
        droneCommander.takeOffDrone();
        droneCommander.hoverDrone(5000);

        while (!droneCommander.isVideoConnected()) ; // venter på videofeed

        droneCommander.adjustToCenterFromQR();

        droneCommander.hoverDrone(2000);
        droneCommander.landDrone();

    }
}
