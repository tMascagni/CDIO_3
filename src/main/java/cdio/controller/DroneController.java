package cdio.controller;

import cdio.controller.interfaces.IDroneController;
import cdio.ui.interfaces.MessageListener;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.LEDAnimation;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.navdata.*;
import de.yadrone.base.video.VideoManager;

public final class DroneController implements IDroneController {

    private final int MAX_ALTITUDE = 2500; /* millimeters. */
    private final int MIN_ALTITUDE = 1000; /* millimeters. */

    private final int INITIAL_SPEED = 30; /* In %, speed goes from 0 to 100. */
    private final int LANDING_SPEED = 15;

    private float pitch, roll, yaw;
    private float altitude;
    private int battery;

    private final IARDrone drone;

    private final CommandManager commandManager;
    private final VideoManager videoManager;
    private final NavDataManager navDataManager;
    private final ConfigurationManager configManager;

    private MessageListener messageListener;

    private static IDroneController instance;

    static {
        try {
            instance = new DroneController();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Singleton DroneController instance!");
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
        startBatteryListener();
    }

    /**
     * Singleton instance getter method.
     */
    public static synchronized IDroneController getInstance() {
        return instance;
    }

    /**
     * Method to start the drone.
     * NOTICE: This method does not make the drone fly, only turns it on.
     */
    @Override
    public final void startDrone() throws DroneControllerException {
        messageListener.messageEventOccurred(this, "Drone starting...");
        setLEDAnimation(LEDAnimation.BLINK_GREEN, 3, 10);
        /* Start the drone */
        drone.start();
        /* Wait to settle for commands... */
        sleep(2000);
        messageListener.messageEventOccurred(this, "Drone started!");
    }

    /**
     * Method to initialize the drone.
     * This method should be called before flying.
     */
    @Override
    public final void initDrone() throws DroneControllerException {
        messageListener.messageEventOccurred(this, "Drone initializing...");
        drone.setSpeed(INITIAL_SPEED);
        commandManager.setMinAltitude(MIN_ALTITUDE);
        commandManager.setMaxAltitude(MAX_ALTITUDE);
        /* Wait to settle for commands... */
        sleep(2000);
        messageListener.messageEventOccurred(this, "Drone initialized!");
    }

    /**
     * Method to stop the drone.
     * NOTICE: This method does NOT land the drone. It only stops it/turns it off.
     */
    @Override
    public final void stopDrone() throws DroneControllerException {
        messageListener.messageEventOccurred(this, "Drone stopping...");
        setLEDAnimation(LEDAnimation.BLINK_RED, 3, 10);
        drone.stop();
        messageListener.messageEventOccurred(this, "Drone stopped!");
    }

    /**
     * Method to make the drone take off.
     */
    @Override
    public final void takeOffDrone() throws DroneControllerException {
        messageListener.messageEventOccurred(this, "Drone taking off...");
        setLEDAnimation(LEDAnimation.BLINK_ORANGE, 3, 10);
        commandManager.setOutdoor(false, true);
        commandManager.flatTrim();
        /* Wait to settle for commands... */
        sleep(1000);
        commandManager.takeOff();
        messageListener.messageEventOccurred(this, "Drone taken off!");
    }

    /**
     * Method to make the drone land.
     */
    @Override
    public final void landDrone() throws DroneControllerException {
        messageListener.messageEventOccurred(this, "Drone landing...");
        setLEDAnimation(LEDAnimation.BLINK_ORANGE, 3, 10);
        drone.setSpeed(LANDING_SPEED);
        commandManager.landing();
        messageListener.messageEventOccurred(this, "Drone landed!");
    }

    /**
     * Method to make the drone hover.
     */
    @Override
    public final void hoverDrone(int timeMillis) throws DroneControllerException {
        messageListener.messageEventOccurred(this, "Drone hovering for " + timeMillis + " milliseconds...");
        commandManager.hover().waitFor(timeMillis);
        messageListener.messageEventOccurred(this, "Drone finished hovering!");
    }

    /**
     * Method to make the drone rotate to a target yaw.
     */
    @Override
    public final void searchRotation() throws DroneControllerException {
        int targetYaw = 300;
        float yaw = this.yaw - targetYaw;

        //tui.log(this, "Dronen drejes: " + yaw + " grader. Target Yaw: " + targetYaw);
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
            } catch (InterruptedException ignored) {

            }
        }

    }

    @Override
    public final void circleAroundObject() throws DroneControllerException {

    }

    /**
     * Method to make the drone fly forwards.
     */
    @Override
    public final void flyForward(int distanceMilli) throws DroneControllerException {
        messageListener.messageEventOccurred(this, "Drone flying forward...");
        commandManager.forward(INITIAL_SPEED).waitFor(distanceMilli);
        commandManager.hover();
        messageListener.messageEventOccurred(this, "Drone finished flying forward!");
    }

    /**
     * Method to make the drone fly backwards.
     */
    @Override
    public final void flyBackward(int distanceMilli) throws DroneControllerException {
        messageListener.messageEventOccurred(this, "Drone flying backward...");
        commandManager.backward(INITIAL_SPEED).waitFor(distanceMilli);
        commandManager.hover();
        messageListener.messageEventOccurred(this, "Drone finished flying backward!");
    }

    /**
     * Method to make the drone fly upwards.
     */
    @Override
    public final void flyUp(int distanceMilli) throws DroneControllerException {
        messageListener.messageEventOccurred(this, "Drone flying up...");
        commandManager.up(INITIAL_SPEED).waitFor(distanceMilli);
        commandManager.hover();
        messageListener.messageEventOccurred(this, "Drone finished flying up!");
    }

    /**
     * Method to make the drone fly downwards.
     */
    @Override
    public final void flyDown(int distanceMilli) throws DroneControllerException {
        messageListener.messageEventOccurred(this, "Drone flying down...");
        commandManager.down(INITIAL_SPEED).waitFor(distanceMilli);
        commandManager.hover();
        messageListener.messageEventOccurred(this, "Drone finished flying down!");
    }

    /**
     * Method to make the drone fly left.
     */
    @Override
    public final void flyLeft(int distanceMilli) throws DroneControllerException {
        messageListener.messageEventOccurred(this, "Drone flying left...");
        commandManager.goLeft(INITIAL_SPEED).waitFor(distanceMilli);
        commandManager.hover();
        messageListener.messageEventOccurred(this, "Drone finished flying left!");
    }

    /**
     * Method to make the drone fly right.
     */
    @Override
    public final void flyRight(int distanceMilli) throws DroneControllerException {
        messageListener.messageEventOccurred(this, "Drone flying right...");
        commandManager.goRight(INITIAL_SPEED).waitFor(distanceMilli);
        commandManager.hover();
        messageListener.messageEventOccurred(this, "Drone finished flying right!");
    }

    /**
     * Method to reset the drone.
     */
    @Override
    public final void resetDrone() throws DroneControllerException {
        messageListener.messageEventOccurred(this, "Resetting drone...");
        drone.reset();
        messageListener.messageEventOccurred(this, "Drone has been reset!");
    }

    /**
     * Method to get the controllers internal drone object.
     */
    @Override
    public final IARDrone getDrone() throws DroneControllerException {
        return drone;
    }

    @Override
    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    /**
     * Method to start the attitude listener.
     */
    private void startAttitudeListener() {
        navDataManager.addAttitudeListener(new AttitudeListener() {
            @Override
            public void attitudeUpdated(float pitch, float roll, float yaw) {
                DroneController.this.pitch = pitch;
                DroneController.this.roll = roll;
                DroneController.this.yaw = (int) yaw / 1000;
                System.out.println("Pitch: " + pitch + ", Roll: " + roll + ", Yaw: " + yaw);
            }

            @Override
            public void attitudeUpdated(float pitch, float roll) {
                DroneController.this.pitch = pitch;
                DroneController.this.roll = roll;
            }

            @Override
            public void windCompensation(float v, float v1) {

            }
        });
    }

    /**
     * Method to start the altitude listener.
     */
    private void startAltitudeListener() {
        navDataManager.addAltitudeListener(new AltitudeListener() {
            @Override
            public void receivedAltitude(int altitude) {
                DroneController.this.altitude = altitude;
            }

            @Override
            public void receivedExtendedAltitude(Altitude altitude) {
                DroneController.this.altitude = altitude.getRaw();
            }
        });
    }

    /**
     * Method to start the battery listener.
     */
    private void startBatteryListener() {
        navDataManager.addBatteryListener(new BatteryListener() {
            @Override
            public void batteryLevelChanged(int battery) {
                DroneController.this.battery = battery;
                System.out.println("Battery: " + battery);
            }

            @Override
            public void voltageChanged(int voltage) {

            }
        });
    }

    private void startMagnetoListener() {
        navDataManager.addMagnetoListener(magnetoData -> {

        });
    }

    private void startVideoListener() {
        videoManager.addImageListener(bufferedImage -> {

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

    /**
     * Helper method to set a LED animation of the drone.
     */
    private void setLEDAnimation(LEDAnimation ledAnimation, int freq, int duration) {
        commandManager.setLedsAnimation(ledAnimation, freq, duration);
    }

}