package cdio.main;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;
import cdio.ui.jconsole.MainFrame;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;

import java.awt.*;

public final class Main {

    private final static IDroneController droneController = DroneController.getInstance();

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            try {

                droneController.startDrone();
                droneController.initDrone();

                droneController.takeOffDrone();

                droneController.hoverDrone(2000);

                droneController.searchRotation();

                droneController.landDrone();
                droneController.stopDrone();
            } catch (IDroneController.DroneControllerException e) {
                e.printStackTrace();
            }
        });

        /* ######### TEST ######### */

        //MainFrame main = new MainFrame();
    }

}