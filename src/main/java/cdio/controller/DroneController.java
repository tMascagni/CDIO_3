package cdio.controller;

import cdio.controller.interfaces.IDroneController;
import cdio.ui.Tui;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.LEDAnimation;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.navdata.Altitude;
import de.yadrone.base.navdata.AltitudeListener;
import de.yadrone.base.navdata.AttitudeListener;
import de.yadrone.base.navdata.NavDataManager;
import de.yadrone.base.video.VideoManager;

public final class DroneController implements IDroneController {

    private final int MIN_ALTITUDE = 1000; /* mm */
    private final int MAX_ALTITUDE = 2500; /* mm */

    private final int INITIAL_SPEED = 20;
    private final int LANDING_SPEED = 15;

    private float pitch, roll, yaw;
    private float altitude;

    private final IARDrone drone;

    private final CommandManager commandManager;
    private final VideoManager videoManager;
    private final NavDataManager navDataManager;
    private final ConfigurationManager configManager;

    private final Tui tui = Tui.getInstance();

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

        /* Start listeners */
        startAttitudeListener();
        startAltitudeListener();
        //startBatteryListener();
    }

    public static synchronized IDroneController getInstance() {
        return instance;
    }

    /**
     * Method to start the drone.
     * NOTICE: This method does not make the drone fly, only turns it on.
     */
    @Override
    public final void startDrone() throws DroneControllerException {
        tui.log(this, "Starting drone...");
        drone.start();
        sleep(1000);
        commandManager.setLedsAnimation(LEDAnimation.BLINK_GREEN, 3, 10);
        drone.reset();
    }

    /**
     * Method to initialize the drone.
     * This method should be called before flying. (??)
     */
    @Override
    public final void initDrone() throws DroneControllerException {
        tui.log(this, "Initializing drone.");
        drone.setSpeed(INITIAL_SPEED);
        commandManager.setMinAltitude(MIN_ALTITUDE);
        commandManager.setMaxAltitude(MAX_ALTITUDE);
    }

    /**
     * Method to stop the drone.
     * NOTICE: This method does NOT land the drone. It only stops it/turns it off.
     */
    @Override
    public final void stopDrone() throws DroneControllerException {
        tui.log(this, "Stopping drone...");
        commandManager.setLedsAnimation(LEDAnimation.BLINK_RED, 3, 10);
        drone.stop();
    }

    /**
     * Method to make the drone take off.
     */
    @Override
    public final void takeOffDrone() throws DroneControllerException {
        tui.log(this, "Taking off drone...");

        commandManager.setOutdoor(false, true);
        commandManager.flatTrim();

        sleep(500);

        drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_ORANGE, 3, 10);
        commandManager.takeOff();
    }

    /**
     * Method to make the drone land.
     */
    @Override
    public final void landDrone() throws DroneControllerException {
        tui.log(this, "Landing drone...");
        commandManager.setLedsAnimation(LEDAnimation.BLINK_ORANGE, 3, 10);
        drone.setSpeed(LANDING_SPEED);
        commandManager.landing();
    }

    /**
     * Method to make the drone hover.
     */
    @Override
    public final void hoverDrone(int timeMillis) throws DroneControllerException {
        tui.log(this, "Hovering drone for " + timeMillis + " milliseconds...");
        commandManager.hover().waitFor(timeMillis);
    }

    @Override
    public final void searchRotation() throws DroneControllerException {
        tui.log(this, "Search Rotation");

        int targetYaw = 300;
        float yaw = this.yaw - targetYaw;

        tui.log(this, "Dronen drejes: " + yaw + " grader. Target Yaw: " + targetYaw);
        while ((yaw = (yaw - targetYaw)) < -10 || yaw > 10) {
            if (yaw > 179) {
                yaw = targetYaw - yaw;
            } else if (yaw < -179) {
                yaw = targetYaw + yaw;
            }
            if (yaw > 0) {
                commandManager.spinLeft(80).doFor(40);
                commandManager.spinRight(80).doFor(10);
            } else {
                commandManager.spinRight(80).doFor(40);
                commandManager.spinLeft(80).doFor(10);
            }
            commandManager.hover();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public final void circleAroundObject() throws DroneControllerException {

    }

    @Override
    public final void flyForward(int distanceMilli) throws DroneControllerException {
        tui.log(this, "Flying forward for " + distanceMilli + " milliseconds...");
        commandManager.forward(INITIAL_SPEED).waitFor(distanceMilli);
        commandManager.hover();
    }

    @Override
    public final void flyBackward(int distanceMilli) throws DroneControllerException {
        tui.log(this, "Flying backward for " + distanceMilli + " milliseconds...");
        commandManager.backward(INITIAL_SPEED).waitFor(distanceMilli);
        commandManager.hover();
    }

    @Override
    public final void flyUp(int distanceMilli) throws DroneControllerException {
        tui.log(this, "Flying Up for " + distanceMilli + " milliseconds...");
        commandManager.up(INITIAL_SPEED).waitFor(distanceMilli);
        commandManager.hover();
    }

    @Override
    public final void flyDown(int distanceMilli) throws DroneControllerException {
        tui.log(this, "Flying down for " + distanceMilli + " milliseconds...");
        commandManager.down(INITIAL_SPEED).waitFor(distanceMilli);
        commandManager.hover();
    }

    @Override
    public final void flyLeft(int distanceMilli) throws DroneControllerException {
        tui.log(this, "Flying left for " + distanceMilli + " milliseconds...");
        commandManager.goLeft(INITIAL_SPEED).waitFor(distanceMilli);
        commandManager.hover();
    }

    @Override
    public final void flyRight(int distanceMilli) throws DroneControllerException {
        tui.log(this, "Flying right for " + distanceMilli + " milliseconds...");
        commandManager.goRight(INITIAL_SPEED).waitFor(distanceMilli);
        commandManager.hover();
    }

    /**
     * Method to reset the drone. (??)
     */
    @Override
    public final void resetDrone() throws DroneControllerException {
        tui.log(this, "Drone reset!");
        drone.reset();
    }

    /**
     * Method to get the controllers internal drone object.
     */
    @Override
    public final IARDrone getDrone() throws DroneControllerException {
        return drone;
    }

    private void startAttitudeListener() {
        navDataManager.addAttitudeListener(new AttitudeListener() {
            @Override
            public void attitudeUpdated(float pitch, float roll, float yaw) {
                DroneController.this.pitch = pitch;
                DroneController.this.roll = roll;
                DroneController.this.yaw = (int) yaw / 1000;
            }

            @Override
            public void attitudeUpdated(float pitch, float roll) {
                DroneController.this.pitch = pitch;
                DroneController.this.roll = roll;
                System.out.println("Pitch: " + pitch + ", Roll: " + roll + ", Yaw: " + yaw);
            }

            @Override
            public void windCompensation(float v, float v1) {

            }
        });
    }

    private void startAltitudeListener() {
        navDataManager.addAltitudeListener(new AltitudeListener() {
            @Override
            public void receivedAltitude(int i) {
                DroneController.this.altitude = i;
            }

            @Override
            public void receivedExtendedAltitude(Altitude altitude) {
                DroneController.this.altitude = altitude.getRaw();
            }
        });
    }

    /**
     * Helper method used to sleep when neccessary.
     */
    private void sleep(int timeMillis) {
        try {
            Thread.sleep(timeMillis);
        } catch (InterruptedException ignored) {

        }
    }

}