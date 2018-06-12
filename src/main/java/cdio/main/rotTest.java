package cdio.main;

import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

public class rotTest {


    private static final IDroneCommander droneCommander = DroneCommander.getInstance();

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame(droneCommander);
        //droneCommander.setMessageListener(mainFrame);

        try {
            droneCommander.startDrone();
            droneCommander.initDrone();
            droneCommander.takeOffDrone();
            droneCommander.circleAroundObjectV2();
            droneCommander.landDrone();

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
