package server;

import de.yadrone.base.ARDrone;
import de.yadrone.base.command.FlightAnimation;
import de.yadrone.base.command.LEDAnimation;
import de.yadrone.base.navdata.AttitudeListener;
import de.yadrone.base.navdata.BatteryListener;

public final class Server implements IServer {

    private ARDrone drone;

    private static IServer instance;

    static {
        try {
            instance = new Server();
        } catch (Exception e) {
            throw new RuntimeException("Error instantiating Singleton Server instance!");
        }
    }

    private Server() {

    }

    public static synchronized IServer getInstance() {
        return instance;
    }

    /**
     * Method used to initialize the drone at every startup.
     */
    @Override
    public final void initDrone() {
        /* Instantiate drone object if null */
        if (drone == null)
            drone = new ARDrone();

        /* Print connection message to console */
        printConsole("Connecting to drone...");
        /* and start the drone */
        drone.start();

        /*
         * Skal serveren eller controlleren have de forskellige managers?
         * Såsom NavDataManager, CommandManager, etc etc.
         *
         *
         * Skal severen eller controlleren implementere de forskellige listeners?
         * Dette vil jeg mene er en god ide!
         * -- Daniel
         */

        /* #### #### #### TEST #### #### #### */
        /* #### #### #### #### #### #### #### */

        if (drone.getNavDataManager().isConnected()) {
            printConsole("Drone is connected!");
            /* If the drone has connected succesfully, blink green */
            drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_GREEN_RED, 3, 10);
        } else {
            printConsole("Drone is not connected!");
            drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_RED, 5, 10);
        }

        if (drone.getConfigurationManager().isConnected()) {
            printConsole("Drone is connected!");
            /* If the drone has connected succesfully, blink green */
            drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_GREEN_RED, 3, 10);
        } else {
            printConsole("Drone is not connected!");
            drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_RED, 5, 10);
        }


        stopDrone();

        /* #### #### #### #### #### #### #### */
        /* #### #### #### #### #### #### #### */
    }

    /**
     * Method used to stop the drone.
     */
    @Override
    public final void stopDrone() {
        if (drone != null) {
            drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_RED, 3, 10);
            drone.stop();
        }
    }

    @Override
    public final ARDrone getDrone() {
        return drone;
    }

}