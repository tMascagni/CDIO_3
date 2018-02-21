package server;

import controller.DroneController;
import controller.DroneControllerException;
import controller.IDroneController;

public final class Server {

    private static Server instance;

    private final IDroneController droneController = DroneController.getInstance();

    static {
        try {
            instance = new Server();
        } catch (Exception e) {
            throw new RuntimeException("Error instantiating Singleton Server instance!");
        }
    }

    private Server() {

    }

    public static synchronized Server getInstance() {
        return instance;
    }


    public final void initDrone() throws DroneControllerException {

        /* #### #### #### TEST #### #### #### */
        /* #### #### #### #### #### #### #### */

        droneController.startDrone();
        droneController.takeOffDrone();

        droneController.hoverDrone(25000);

        droneController.landDrone();
        droneController.stopDrone();


        //droneController.hoverDrone(10000);

        //droneController.landDrone();
        //droneController.stopDrone();

        /* #### #### #### #### #### #### #### */
        /* #### #### #### #### #### #### #### */
    }

}