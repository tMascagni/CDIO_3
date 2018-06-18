package cdio.main.test;

import cdio.controller.DroneCommander;
import cdio.controller.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

import javax.swing.*;

public class mainTestcenterfromQR {

    private final static IDroneCommander droneCommander = DroneCommander.getInstance();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneCommander);
        });

        try {
            droneCommander.startDrone();
            droneCommander.takeOffDrone();
            droneCommander.hoverDrone(2000);
            droneCommander.adjustToCenterFromQR(50);
            droneCommander.landDrone();
            droneCommander.stopDrone();
        } catch (IDroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        }
    }
}