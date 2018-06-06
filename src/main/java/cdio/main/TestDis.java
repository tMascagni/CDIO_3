package cdio.main;

import cdio.controller.DisCal;
import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

import javax.swing.*;

public final class TestDis {

    private final static IDroneCommander droneCommander = DroneCommander.getInstance();

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneCommander);
        });

        DisCal disCal = new DisCal();

               /* droneCommander.startDrone();
                droneCommander.initDrone();

                droneCommander.takeOffDrone();

                droneCommander.hoverDrone(8000);
*/

        int timer = 200;
        while (droneCommander.getQrImgs().isEmpty()) {
            timer--;
            if (timer == 0) {
                System.out.println("venter på qr kode");
                timer = 100;
            }
        }

        while (disCal.distanceFromHeight(droneCommander.getQrImgs().get(0).getH()) <= 100) {
            // droneCommander.flyForward(200);
            // droneCommander.hoverDrone(200);
            droneCommander.addMessage("Height af QR kode: " + droneCommander.getQrImgs().get(0).getH());
        }

        //       droneCommander.flyBackward(500);
        //      droneCommander.hoverDrone(2000);
        //      droneCommander.landDrone();

        //   } catch (IDroneCommander.DroneCommanderException e) {
        //       e.printStackTrace();
        //   }


    }
}
