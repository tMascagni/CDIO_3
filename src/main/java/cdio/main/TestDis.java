package cdio.main;

import cdio.computervision.QRDetector;
import cdio.controller.DisCal;
import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;
import cdio.ui.MainFrame;

import java.awt.*;

public final class TestDis {

    private final static IDroneController droneController = DroneController.getInstance();

    public static void main(String[] args) {

        MainFrame mainFrame = new MainFrame(droneController);
        droneController.setMessageListener(mainFrame);

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

                while (droneController.getQrData().getWidth() < 10){
                    droneController.flyDroneTest(100);
                    droneController.hoverDrone(100);
                    System.out.println(disCal.disCal(droneController.getQrData().getWidth()) + "cm");
                }

            } catch (IDroneController.DroneControllerException e) {
                e.printStackTrace();
            }
        });
        /* ######### TEST ######### */

    }

}