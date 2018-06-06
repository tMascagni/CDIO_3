package cdio.main;

import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

public class GUIAngleTest {

    private static final IDroneCommander droneController = DroneCommander.getInstance();

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame(droneController);
        //droneController.setMessageListener(mainFrame);

        try {
            droneController.startDrone();

        } catch (IDroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        }

    }


}