package cdio.main;

import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

public class HoverOffsetFix {

    private static final IDroneCommander droneCommander = DroneCommander.getInstance();

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame(droneCommander);

        try {
            droneCommander.startDrone();
            Thread.sleep(200);
            droneCommander.initDrone();
            Thread.sleep(200);
            droneCommander.takeOffDrone();
            Thread.sleep(4000);
            droneCommander.hoverDrone(5000);
            Thread.sleep(200);
            droneCommander.landDrone();

            /*
            int targetYaw = (int) (droneCommander.getCorrectYaw(droneCommander.getYaw()) + 180);
            droneCommander.addMessage("RAW target yaw: " + targetYaw + "");
            targetYaw = droneCommander.getCorrectTargetYaw(targetYaw);
            droneCommander.addMessage("Correct target yaw: " + targetYaw);
*/
        } catch (IDroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
