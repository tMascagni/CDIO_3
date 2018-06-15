package cdio.main;

import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

public class RejeHopTest {

    private static final IDroneCommander droneCommander = DroneCommander.getInstance();

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame(droneCommander);

        try {
            droneCommander.startDrone();
            droneCommander.initDrone();
            droneCommander.takeOffDrone();
            droneCommander.hoverDrone(5000);
            droneCommander.rejeHop();
            droneCommander.hoverDrone(2000);
            droneCommander.landDrone();

            /*
            int targetYaw = (int) (droneCommander.getCorrectYaw(droneCommander.getYaw()) + 180);
            droneCommander.addMessage("RAW target yaw: " + targetYaw + "");
            targetYaw = droneCommander.getCorrectTargetYaw(targetYaw);
            droneCommander.addMessage("Correct target yaw: " + targetYaw);
*/
        } catch (IDroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        }

    }


}
