package cdio.main;

import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

public class AltitudeTest {

    private static final IDroneCommander droneCommander = DroneCommander.getInstance();

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame(droneCommander);
        //droneCommander.setMessageListener(mainFrame);

        try {
            droneCommander.startDrone();
            droneCommander.initDrone();
            droneCommander.takeOffDrone();
            droneCommander.adjustHeightToCenterFromQR();

            /*
            while (true){
                System.out.println(droneCommander.getAltitude());
            }
            */

        } catch (IDroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        }

    }
}
