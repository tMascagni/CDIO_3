package cdio.main;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;

import java.awt.*;

public final class MainLand {

    private final static IDroneController droneController = DroneController.getInstance();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                droneController.landDrone();
                droneController.stopDrone();
            } catch (IDroneController.DroneControllerException e) {
                e.printStackTrace();
            }
        });
    }

}
