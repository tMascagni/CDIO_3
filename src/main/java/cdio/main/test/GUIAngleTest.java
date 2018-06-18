package cdio.main.test;

import cdio.controller.DroneCommander;
import cdio.controller.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

public class GUIAngleTest {

    private static final IDroneCommander droneCommander = DroneCommander.getInstance();

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame(droneCommander);
        //droneCommander.setMessageListener(mainFrame);

        try {
            droneCommander.startDrone();

        } catch (IDroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        }

    }


}
