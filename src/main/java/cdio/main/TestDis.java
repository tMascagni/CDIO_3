package cdio.main;

import cdio.controller.DisCal;
import cdio.controller.DroneCommander;
import cdio.controller.interfaces.IDroneCommander;
import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

import java.awt.*;

public final class TestDis {

    private final static IDroneCommander droneController = DroneCommander.getInstance();

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


                while (droneController.getQrData() == null) ;

                while (droneController.getQrData().getWidth() < 245) {
                    droneController.flyDroneTest(50);
                    droneController.hoverDrone(50);
                    droneController.addMessage("brede af QRkode:" + droneController.getQrData().getWidth());
                }


            } catch (IDroneCommander.DroneCommanderException e) {
                e.printStackTrace();
            }
        });

        /* ######### TEST ######### */

    }

}