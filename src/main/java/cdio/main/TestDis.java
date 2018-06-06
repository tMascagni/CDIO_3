package cdio.main;

import cdio.controller.DisCal;
import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

import javax.swing.*;

public final class TestDis {

    private final static IDroneCommander droneController = DroneCommander.getInstance();

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneController);
        });

        DisCal disCal = new DisCal();

               /* droneController.startDrone();
                droneController.initDrone();

                droneController.takeOffDrone();

                droneController.hoverDrone(8000);
*/

        int timer = 200;
        while (droneController.getQrImgs().isEmpty()) {
            timer--;
            if (timer == 0) {
                System.out.println("venter på qr kode");
                timer = 100;
            }
        }

        while (disCal.distanceFromHeight(droneController.getQrImgs().get(0).getH()) <= 100) {
            // droneController.flyForward(200);
            // droneController.hoverDrone(200);
            droneController.addMessage("Height af QR kode: " + droneController.getQrImgs().get(0).getH());
        }

        //       droneController.flyBackward(500);
        //      droneController.hoverDrone(2000);
        //      droneController.landDrone();

        //   } catch (IDroneCommander.DroneCommanderException e) {
        //       e.printStackTrace();
        //   }


    }
}