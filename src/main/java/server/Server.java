package server;

import controller.DroneController;
import controller.DroneControllerException;
import controller.IDroneController;
import de.yadrone.base.video.ImageListener;

import javax.swing.*;
import java.awt.image.BufferedImage;

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


        droneController.landDrone();
        droneController.stopDrone();


        //droneController.hoverDrone(10000);

        //droneController.landDrone();
        //droneController.stopDrone();

        /* #### #### #### #### #### #### #### */
        /* #### #### #### #### #### #### #### */



    }


}