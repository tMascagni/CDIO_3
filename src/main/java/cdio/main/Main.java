package cdio.main;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;

public final class Main {

    private final static IDroneController droneController = DroneController.getInstance();

    public static void main(String[] args) throws IDroneController.DroneControllerException {

        /* ######### TEST ######### */
        droneController.startDrone();
        droneController.takeOffDrone();

        droneController.hoverDrone(10000);

        droneController.landDrone();
        droneController.stopDrone();
        /* ######### TEST ######### */

    }

}