package controller;

import controlcenter.console.Console;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.LEDAnimation;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.navdata.NavDataManager;
import de.yadrone.base.video.VideoManager;

public final class DroneController implements IDroneController {

    private final IARDrone drone;

    private final CommandManager commandManager;
    private final VideoManager videoManager;
    private final NavDataManager navDataManager;
    private final ConfigurationManager configManager;

    private final Console console = Console.getInstance();

    private static IDroneController instance;

    static {
        try {
            instance = new DroneController();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize DroneController Singleton instance!");
        }
    }

    private DroneController() {
        /* Instantiate Drone object */
        drone = new ARDrone();

        /* Instantiate manager objects */
        commandManager = drone.getCommandManager();
        videoManager = drone.getVideoManager();
        navDataManager = drone.getNavDataManager();
        configManager = drone.getConfigurationManager();
    }

    public static synchronized IDroneController getInstance() {
        return instance;
    }

    /**
     * Method to start the drone.
     * NOTICE: This method does not make the drone fly, only turns it on.
     */
    @Override
    public void startDrone() throws DroneControllerException {
        console.log(this, "Starting drone...");
        drone.start();
        commandManager.setLedsAnimation(LEDAnimation.BLINK_GREEN, 3, 10);
    }

    /**
     * Method to initialize the drone.
     * This method should be called before flying. (??)
     */
    @Override
    public void initDrone() throws DroneControllerException {
        console.log(this, "Initializing drone.");
    }

    /**
     * Method to stop the drone.
     * NOTICE: This method does NOT land the drone. It only stops it/turns it off.
     */
    @Override
    public void stopDrone() throws DroneControllerException {
        console.log(this, "Stopping drone...");
        commandManager.setLedsAnimation(LEDAnimation.BLINK_RED, 3, 10);
        drone.stop();
    }

    /**
     * Method to make the drone take off.
     */
    @Override
    public void takeOffDrone() throws DroneControllerException {
        console.log(this, "Taking off drone...");

        // Flyver indendørs, med beskyttelse på
        commandManager.setOutdoor(false, true);

        commandManager.flatTrim();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }

        drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_ORANGE, 3, 10);
        commandManager.takeOff();
    }

    /**
     * Method to make the drone land.
     */
    @Override
    public void landDrone() throws DroneControllerException {
        console.log(this, "Landing drone...");
        commandManager.setLedsAnimation(LEDAnimation.BLINK_GREEN_RED, 3, 10);
        drone.setSpeed(38);
        commandManager.landing().waitFor(3000);
    }

    /**
     * Method to make the drone hover.
     */
    @Override
    public void hoverDrone(int timeMillis) throws DroneControllerException {
        console.log(this, "Hovering drone for " + timeMillis + " milliseconds...");
        commandManager.hover().waitFor(timeMillis);
    }

    /**
     * Method to get the controllers internal drone object.
     */
    @Override
    public IARDrone getDrone() throws DroneControllerException {
        return drone;
    }

}