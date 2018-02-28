package cdio.main;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;
import cdio.ui.jconsole.MainFrame;

import java.awt.*;

public final class Main {

    private final static IDroneController droneController = DroneController.getInstance();

    public static void main(String[] args) {

        /* ######### TEST ######### */

        EventQueue.invokeLater(() -> {
            try {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                droneController.startDrone();
                droneController.initDrone();

                droneController.takeOffDrone();

                droneController.hoverDrone(10000);

             //   droneController.flyUp(3000);
            //    droneController.flyDown(2000);

               // droneController.flyUp(2000);

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