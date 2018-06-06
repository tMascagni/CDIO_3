package cdio.main;

import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

public final class MainGUI {

    private static final IDroneCommander droneController = DroneCommander.getInstance();

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame(droneController);

        try {
            droneController.startDrone();
            droneController.initDrone();

            int targetYaw = (int) (droneController.getCorrectYaw(droneController.getYaw()) + 180);
            droneController.addMessage("RAW target yaw: " + targetYaw + "");
            targetYaw = droneController.getCorrectTargetYaw(targetYaw);
            droneController.addMessage("Correct target yaw: " + targetYaw);

        } catch (IDroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        }

    }

}