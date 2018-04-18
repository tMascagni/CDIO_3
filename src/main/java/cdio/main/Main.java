package cdio.main;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;
import cdio.ui.MainFrame;

import java.awt.*;

public final class Main {

    private final static IDroneController droneController = DroneController.getInstance();

    public static void main(String[] args) {

        MainFrame mainFrame = new MainFrame(droneController);
        droneController.setMessageListener(mainFrame);


        /* ######### TEST ######### */
        EventQueue.invokeLater(() -> {
            try {
                droneController.startDrone();
                droneController.initDrone();

                droneController.takeOffDrone();

                droneController.hoverDrone(5000);

                droneController.doSearchRotation();
                droneController.hoverDrone(7000);
                droneController.doSearchRotation();

                droneController.hoverDrone(10000);
                droneController.landDrone();
                droneController.stopDrone();

            } catch (IDroneController.DroneControllerException e) {
                e.printStackTrace();
            }
        });
        /* ######### TEST ######### */

    }

}