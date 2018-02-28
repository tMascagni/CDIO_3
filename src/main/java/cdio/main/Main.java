package cdio.main;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;
import cdio.ui.jconsole.MainFrame;

public final class Main {

    //private final static IDroneController droneController = DroneController.getInstance();

    public static void main(String[] args) {

        /* ######### TEST ######### */
        /*
        EventQueue.invokeLater(() -> {
            try {
                droneController.startDrone();
                droneController.initDrone();

                droneController.takeOffDrone();

                droneController.hoverDrone(10000);

                droneController.landDrone();
                droneController.stopDrone();
            } catch (IDroneController.DroneControllerException e) {
                e.printStackTrace();
            }
        });
        */
        /* ######### TEST ######### */

        MainFrame main = new MainFrame();
    }

}