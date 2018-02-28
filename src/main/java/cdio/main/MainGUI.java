package cdio.main;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;
import cdio.ui.MainFrame;

public final class MainGUI {

    private static final IDroneController droneController = DroneController.getInstance();

    public static void main(String[] args) throws IDroneController.DroneControllerException {
        MainFrame mainFrame = new MainFrame(droneController);
        droneController.setMessageListener(mainFrame);

        droneController.takeOffDrone();
    }

}
