package cdio.main;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;
import cdio.ui.MainFrame;
import cdio.ui.interfaces.MessageListener;

import java.awt.*;

public final class Main {

    private final static IDroneController droneController = DroneController.getInstance();

    public static void main(String[] args) throws IDroneController.DroneControllerException {

       // MainFrame mainFrame = new MainFrame(droneController);
        droneController.setMessageListener(new MessageListener() {
            @Override
            public void messageEventOccurred(Object obj, String text) {

            }
        });

        droneController.startDrone();
        droneController.initDrone();

        droneController.takeOffDrone();

        droneController.hoverDrone(10000);

        //droneController.searchRotation();

        droneController.landDrone();
        droneController.stopDrone();

        /* ######### TEST ######### */
        EventQueue.invokeLater(() -> {

        });
        /* ######### TEST ######### */

    }

}