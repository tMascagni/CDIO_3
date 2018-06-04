package cdio.main;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;
import cdio.ui.MainFrame;

public class GUIAngleTest {

    private static final IDroneController droneController = DroneController.getInstance();

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame(droneController);
        //droneController.setMessageListener(mainFrame);

        try {
            droneController.startDrone();

        } catch (IDroneController.DroneControllerException e) {
            e.printStackTrace();
        }

    }


}
