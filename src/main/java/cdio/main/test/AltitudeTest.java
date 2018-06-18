package cdio.main.test;

import cdio.controller.DroneCommander;
import cdio.controller.interfaces.IDroneCommander;
import cdio.ui.MainFrame;

public class AltitudeTest {

    private static final IDroneCommander droneCommander = DroneCommander.getInstance();

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame(droneCommander);
        //droneCommander.setMessageListener(mainFrame);

        try {
            droneCommander.startDrone();
            droneCommander.initDrone();
            // droneCommander.takeOffDrone();
            // droneCommander.adjustHeightToCenterFromQR();


            while (true){
                System.out.println(droneCommander.getAltitude());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        } catch (IDroneCommander.DroneCommanderException e) {
            e.printStackTrace();
        }

    }
}
