package cdio.main;

import cdio.controller.DroneCommander;
import cdio.controller.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

public final class MainGUI {

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