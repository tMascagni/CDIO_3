package cdio.main;

import cdio.controller.DroneCommander;
import cdio.controller.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

public final class MainGUI {

    private static final IDroneCommander droneCommander = DroneCommander.getInstance();

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame(droneCommander);

        try {
            droneCommander.startDrone();
            droneCommander.initDrone();
        } catch (IDroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        }

    }

}