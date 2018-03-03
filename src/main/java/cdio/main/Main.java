package cdio.main;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;

import java.awt.*;

public final class Main {

    private final static IDroneController droneController = DroneController.getInstance();

    public static void main(String[] args) {

        droneController.setMessageListener((obj, text) -> {

        });

        /* ######### TEST ######### */
        EventQueue.invokeLater(() -> {
            try {
                droneController.startDrone();
                droneController.initDrone();

                droneController.takeOffDrone();

                droneController.hoverDrone(10000);

                //droneController.searchRotation();

                droneController.landDrone();
                droneController.stopDrone();
            } catch (IDroneController.DroneControllerException e) {
                e.printStackTrace();
            }
        });
        /* ######### TEST ######### */

    }

}