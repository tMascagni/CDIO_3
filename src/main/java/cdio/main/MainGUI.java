package cdio.main;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;
import cdio.ui.MainFrame;

import java.awt.*;

public final class MainGUI {

    private static final IDroneController droneController = DroneController.getInstance();

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame(droneController);
        droneController.setMessageListener(mainFrame);

        /* ######### TEST ######### */
        EventQueue.invokeLater(() -> {
            try {
                droneController.startDrone();
                droneController.initDrone();

                droneController.takeOffDrone();

                droneController.hoverDrone(8000);

                droneController.landDrone();
                droneController.stopDrone();
            } catch (IDroneController.DroneControllerException e) {
                e.printStackTrace();
            }
        });
        /* ######### TEST ######### */
    }

}
