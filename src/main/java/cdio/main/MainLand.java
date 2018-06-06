package cdio.main;

import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;

import java.awt.*;

public final class MainLand {

    private final static IDroneCommander droneCommander = DroneCommander.getInstance();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                droneCommander.landDrone();
                droneCommander.stopDrone();
            } catch (IDroneCommander.DroneCommanderException e) {
                e.printStackTrace();
            }
        });
    }

}
