package cdio.main;

import cdio.controller.DisCal;
import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;
import cdio.ui.MainFrame;

import java.awt.*;

public final class TestDis {

    private final static IDroneController droneController = DroneController.getInstance();

    public static void main(String[] args) {

        MainFrame mainFrame = new MainFrame(droneController);

        DisCal disCal = new DisCal();

        /* ######### TEST ######### */
        EventQueue.invokeLater(() -> {
            try {
                droneController.startDrone();
                droneController.initDrone();

                droneController.takeOffDrone();

                droneController.hoverDrone(100);

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while (droneController.getQrData() == null);

               while (droneController.getQrData().getWidth() < 10){
                    droneController.flyDroneTest(200);
                    droneController.hoverDrone(200);
                    droneController.addMessage("brede af QRkode:" + droneController.getQrData().getWidth());
                }


            } catch (IDroneController.DroneControllerException e) {
                e.printStackTrace();
            }
        });


        /* ######### TEST ######### */

    }

}