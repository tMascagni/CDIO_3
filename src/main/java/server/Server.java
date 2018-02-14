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
        } else {
            printConsole("Drone is not connected!");
        }

        if (drone.getConfigurationManager().isConnected()) {
            printConsole("Drone is connected!");
        } else {
            printConsole("Drone is not connected!");
        }

        /* If the drone has connected succesfully, blink green */
        drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_GREEN, 3, 10);

        //drone.getCommandManager().flatTrim();

        /* Take off */
        drone.takeOff();

        /* Hover the drone */
        drone.hover();

        drone.getNavDataManager().addAttitudeListener(new AttitudeListener() {
            @Override
            public void attitudeUpdated(float pitch, float roll, float yaw) {
                printConsole("Pitch: " + pitch + " Roll: " + roll + " Yaw: " + yaw);
            }

            @Override
            public void attitudeUpdated(float pitch, float roll) {

            }

            @Override
            public void windCompensation(float pitch, float roll) {

            }
        });

        drone.getNavDataManager().addBatteryListener(new BatteryListener() {
            @Override
            public void batteryLevelChanged(int percentage) {
                printConsole("Battery: " + percentage + " %");
            }

            @Override
            public void voltageChanged(int voltage) {
                printConsole("Voltage changed: " + voltage);
            }
        });

        drone.addExceptionListener(exc -> exc.printStackTrace());

        drone.addSpeedListener(speed -> printConsole("Speed: " + speed));

        drone.getCommandManager().animate(FlightAnimation.THETA_DANCE);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        drone.landing();

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