package cdio.main.test;

import cdio.controller.DroneCommander;
import cdio.controller.interfaces.IDroneCommander;
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
            droneCommander.hoverDrone(2000);

            droneCommander.circleAroundObject();
            droneCommander.addMessage("CIRCLE AROUND OBJECT V2 DONE #######################################");
            //droneCommander.lockOn();

            droneCommander.hoverDrone(2000);
            droneCommander.landDrone();
            droneCommander.stopDrone();

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
