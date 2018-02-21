package controller;

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
     * NOTICE: This method should not make the drone fly.
     */
    @Override
    public void startDrone() throws DroneControllerException {
        commandManager.setLedsAnimation(LEDAnimation.GREEN, 3, 10);
        drone.start();
    }

    @Override
    public void initDrone() throws DroneControllerException {

    }

    @Override
    public void stopDrone() throws DroneControllerException {
        commandManager.setLedsAnimation(LEDAnimation.BLINK_GREEN_RED, 3, 10);

    }

    @Override
    public void takeOffDrone() throws DroneControllerException {
        commandManager.setLedsAnimation(LEDAnimation.BLINK_GREEN_RED, 3, 10);
    }

    @Override
    public void landDrone() throws DroneControllerException {

    }

    @Override
    public void hoverDrone(int time) throws DroneControllerException {

    }

    @Override
    public IARDrone getDrone() throws DroneControllerException {
        return null;
    }



}