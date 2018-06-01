package cdio.algorithms;

import cdio.drone.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

import javax.swing.*;

public final class Algorithms {

    private Algorithms() {

    }

    public static void runSingleRing(IDroneCommander droneCommander) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneCommander);
        });

        /* ######### TEST ######### */
        try {
            droneCommander.startDrone();
            droneCommander.initDrone();

            droneCommander.takeOffDrone();

            droneCommander.hoverDrone(5000);

            droneCommander.searchForQRCode();

            droneCommander.hoverDrone(5000);
            droneCommander.landDrone();
            droneCommander.stopDrone();

        } catch (IDroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        }
    }

    public static void runAllRings(IDroneCommander droneCommander) {

    }

}