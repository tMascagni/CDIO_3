package cdio.drone;

import cdio.drone.interfaces.IDroneCommander;
import cdio.handler.QRCodeHandler;
import cdio.handler.interfaces.IQRCodeHandler;
import cdio.model.QRCodeData;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.LEDAnimation;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.navdata.*;
import de.yadrone.base.video.VideoManager;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DroneCommander implements IDroneCommander {

    private final int MAX_ALTITUDE = 2500; /* millimeters. */
    private final int MIN_ALTITUDE = 1000; /* millimeters. */

    private final int MAX_SPEED = 100; /* percentage (%) */
    private final int MIN_SPEED = 10;  /* percentage (%) */

    private final int INITIAL_SPEED = 20; /* In %, speed goes from 0 to 100. */
    private final int LANDING_SPEED = 20;

    private float pitch, roll, yaw;
    private float altitude;
    private int battery;

    private final IARDrone drone;

    private final CommandManager commandManager;
    private final VideoManager videoManager;
    private final NavDataManager navDataManager;
    private final ConfigurationManager configManager;

    private final IQRCodeHandler qrCodeHandler = QRCodeHandler.getInstance();
    private BufferedImage latestReceivedImage;

    private List<String> messageList = new ArrayList<>();

    private static IDroneCommander instance;

    static {
        try {
            instance = new DroneCommander();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Singleton DroneCommander instance!");
        }
    }

    private DroneCommander() {
        /* Instantiate Drone object */
        drone = new ARDrone();
        drone.reset();

        /* Instantiate manager objects */
        commandManager = drone.getCommandManager();
        videoManager = drone.getVideoManager();
        navDataManager = drone.getNavDataManager();
        configManager = drone.getConfigurationManager();

        /* Start listeners */
        startAcceleroListener();
        startAttitudeListener();
        startAltitudeListener();
        startBatteryListener();
        startImageListener();
    }

    /**
     * Singleton instance getter method.
     */
    public static synchronized IDroneCommander getInstance() {
        return instance;
    }

    /**
     * Method to start the drone.
     * NOTICE: This method does not make the drone fly, only turns it on.
     */
    @Override
    public final void startDrone() throws DroneCommanderException {
        addMessage("Drone starting...");

        setLEDAnimation(LEDAnimation.BLINK_GREEN, 3, 10);
        /* Start the drone */
        drone.start();
        /* Wait to settle for commands... */
        sleep(2000);

        addMessage("Drone started!");
    }

    /**
     * Method to initialize the drone.
     * This method should be called before flying.
     */
    @Override
    public final void initDrone() throws DroneCommanderException {
        addMessage("Drone initializing...");

        setSpeed(INITIAL_SPEED);
        commandManager.setMinAltitude(MIN_ALTITUDE);
        commandManager.setMaxAltitude(MAX_ALTITUDE);
        /* Wait to settle for commands... */
        sleep(2000);

        addMessage("Drone initialized!");
    }

    /**
     * Method to stop the drone.
     * NOTICE: This method does NOT land the drone. It only stops it/turns it off.
     */
    @Override
    public final void stopDrone() throws DroneCommanderException {
        addMessage("Drone stopping...");

        setLEDAnimation(LEDAnimation.BLINK_RED, 3, 10);
        drone.stop();
        sleep(1000);

        addMessage("Drone stopped!");
    }

    /**
     * Method to make the drone take off.
     */
    @Override
    public final void takeOffDrone() throws DroneCommanderException {
        addMessage("Drone taking off...");

        setLEDAnimation(LEDAnimation.BLINK_GREEN_RED, 3, 10);
        commandManager.setOutdoor(false, true);
        commandManager.flatTrim();
        /* Wait to settle for commands... */
        sleep(1000);
        commandManager.takeOff().doFor(200);

        addMessage("Drone taken off!");
    }

    /**
     * Method to make the drone land.
     */
    @Override
    public final void landDrone() throws DroneCommanderException {
        addMessage("Drone landing...");

        setLEDAnimation(LEDAnimation.BLINK_GREEN_RED, 3, 10);
        setSpeed(LANDING_SPEED);
        sleep(500);
        commandManager.landing().doFor(200);

        addMessage("Drone landed!");
    }

    /**
     * Method to make the drone hover for timeMillis ms.
     */
    @Override
    public final void hoverDrone(int timeMillis) throws DroneCommanderException {
        addMessage("Drone hovering for " + timeMillis + " ms...");

        commandManager.hover().waitFor(timeMillis);

        addMessage("Drone finished hovering!");
    }

    /**
     * Method to make the drone hover.
     */
    @Override
    public void hoverDrone() throws DroneCommanderException {
        addMessage("Drone hovering...");

        commandManager.hover();

        addMessage("Drone finished hovering!");
    }

    Map<String, Float> map = new HashMap<>();

    /**
     * Method to make the drone rotate to a target yaw.
     */
    @Override
    public final QRCodeData searchForQRCode() throws DroneCommanderException {
        addMessage("Searching for a QR code...");
        /*
         * TargetYaw er den vinkel som dronen skal dreje hen til. Altså ikke
         * hvor meget den skal dreje. Det er den vinkel den skal opnå at pege
         * hen til med dens front.
         *
         * Vi adderer dronens nuværende yaw med 180 (grader), for altid at dreje 180 grader
         * når denne metode bliver kørt.
         *
         * Måske burde dette laves om til at den altid drejer 360 heletiden?
         */
        int targetYaw = (int) (getCorrectedYaw() + 180);

        if (targetYaw > 179) {
            if (getCorrectedYaw() > 0) {
                targetYaw = targetYaw - 360;
                addMessage("CALCULATED TargetYaw: " + targetYaw);
            }
        } else if (targetYaw < -179) {
            targetYaw = 360 + targetYaw;
            addMessage("CALCULATED TargetYaw: " + targetYaw);
        }

        int negativeBound = -8;
        int positiveBound = 8;

        while ((yaw = (getCorrectedYaw() - targetYaw)) < negativeBound || yaw > positiveBound) { // default -8 og 8 :) // -23 og 23 virker fint.

            if (yaw > 179)
                yaw = 360 - yaw;
            else if (yaw < -179)
                yaw = 360 + yaw;

            if (yaw > 0) {
                commandManager.spinLeft(80).doFor(40);
                commandManager.spinRight(80).doFor(10);
            } else {
                commandManager.spinRight(80).doFor(40);
                commandManager.spinLeft(80).doFor(10);
            }

            // do some scanning for QRCode
            commandManager.hover().doFor(50);

            try {

                QRCodeData qrCodeData = qrCodeHandler.scanImage(latestReceivedImage);
                // qr detected

                /*
                 * Vi skal have en hjælpefunktion der tager gennemsnittet
                 * af alle i en pågældende gruppe.
                 *
                 * For at finde den rigtige path.
                 */
                map.put(qrCodeData.getResult(), getCorrectedYaw());
                System.out.println("#############  QR CODE DETECTED #############");
                System.out.println(map);

                addMessage("Found QR code! Stopping.");
                return qrCodeData;
            } catch (IQRCodeHandler.QRCodeHandlerException ignored) {
                // no qr detected which is fine.
            }

            commandManager.hover().doFor(50);
            sleep(500);
        }

        return null;
    }

    @Override
    public final void circleAroundObject() throws DroneCommanderException {

    }

    /**
     * Method to make the drone fly forwards.
     */
    @Override
    public final void flyForward(int timeMillis) throws DroneCommanderException {
        addMessage("Drone flying forward for " + timeMillis + " ms...");

        commandManager.forward(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        addMessage("Drone done flying forward!");
    }

    public void flyDroneTest(double dist) {
        while (dist > 50) {
            try {
                commandManager.forward(INITIAL_SPEED);
                dist = dist - 100; // Dronen er flyttet ca. 1 meter
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {

            }
        }
    }

    /**
     * Method to make the drone fly backwards.
     */
    @Override
    public final void flyBackward(int timeMillis) throws DroneCommanderException {
        addMessage("Drone flying backward for " + timeMillis + " ms...");

        commandManager.backward(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        addMessage("Drone done flying backward!");
    }

    /**
     * Method to make the drone fly upwards.
     */
    @Override
    public final void flyUp(int timeMillis) throws DroneCommanderException {
        addMessage("Drone flying upwards for " + timeMillis + " ms...");

        commandManager.up(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        addMessage("Drone done flying upwards!");
    }

    /**
     * Method to make the drone fly downwards.
     */
    @Override
    public final void flyDown(int timeMillis) throws DroneCommanderException {
        addMessage("Drone flying downwards for " + timeMillis + " ms...");

        commandManager.down(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        addMessage("Drone done flying downwards!");
    }

    /**
     * Method to make the drone fly left.
     */
    @Override
    public final void flyLeft(int timeMillis) throws DroneCommanderException {
        addMessage("Drone flying left for " + timeMillis + " ms...");

        commandManager.goLeft(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        addMessage("Drone done flying left!");
    }

    /**
     * Method to make the drone fly right.
     */
    @Override
    public final void flyRight(int timeMillis) throws DroneCommanderException {
        addMessage("Drone flying right for " + timeMillis + " ms...");

        commandManager.goRight(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        addMessage("Drone done flying right!");
    }

    @Override
    public void setSpeed(int speed) throws DroneCommanderException {

        if (speed > MAX_SPEED || speed < MIN_SPEED) {
            addMessage("Attempted to set illegal drone speed: " + speed + "!");
            return;
        }

        drone.setSpeed(speed);

        addMessage("Sat drone speed: " + speed + "!");
    }

    @Override
    public int getSpeed() throws DroneCommanderException {
        return drone.getSpeed();
    }

    /**
     * Method to reset the drone.
     */
    @Override
    public final void resetDrone() throws DroneCommanderException {
        addMessage("Resetting drone...");

        drone.reset();
        sleep(1000);

        addMessage("Drone reset!");
    }

    /**
     * Method to get the controllers internal drone object.
     */
    @Override
    public final IARDrone getDrone() throws DroneCommanderException {
        if (drone == null)
            throw new DroneCommanderException("Drone object is null!");
        return drone;
    }

    /**
     * Method to start the attitude listener.
     */
    private void startAttitudeListener() {
        navDataManager.addAttitudeListener(new AttitudeListener() {
            @Override
            public void attitudeUpdated(float pitch, float roll, float yaw) {
                DroneCommander.this.pitch = pitch;
                DroneCommander.this.roll = roll;
                DroneCommander.this.yaw = (int) yaw / 1000;
            }

            @Override
            public void attitudeUpdated(float pitch, float roll) {
                DroneCommander.this.pitch = pitch;
                DroneCommander.this.roll = roll;
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
                DroneCommander.this.altitude = altitude;
            }

            @Override
            public void receivedExtendedAltitude(Altitude altitude) {
                DroneCommander.this.altitude = altitude.getRaw();
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
                DroneCommander.this.battery = battery;
            }

            @Override
            public void voltageChanged(int voltage) {

            }
        });
    }

    private void startImageListener() {
        videoManager.addImageListener(bufferedImage -> latestReceivedImage = bufferedImage);
    }

    private void startAcceleroListener() {
        navDataManager.addAcceleroListener(new AcceleroListener() {
            @Override
            public void receivedRawData(AcceleroRawData acceleroRawData) {

            }

            @Override
            public void receivedPhysData(AcceleroPhysData acceleroPhysData) {

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

    /**
     * Helper method to set a LED animation of the drone.
     */
    private void setLEDAnimation(LEDAnimation ledAnimation, int freq, int duration) {
        commandManager.setLedsAnimation(ledAnimation, freq, duration);
    }

    private float getCorrectedYawIntern() {
        float yawCorrected = yaw; // + yawCorrection;

        if (yawCorrected >= 180)
            yawCorrected = 359 - yawCorrected;
        else if (yawCorrected <= -180)
            yawCorrected = 359 + yawCorrected;

        return yawCorrected;
    }

    @Override
    public void addMessage(String msg) {
        messageList.add(msg);
    }

    @Override
    public List<String> getNewMessages() {
        return messageList;
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public float getRoll() {
        return roll;
    }

    @Override
    public float getYaw() {
        return yaw;
    }

    @Override
    public float getCorrectedYaw() {
        return -1.0F;
    }

    @Override
    public float getAltitude() {
        return altitude;
    }

    @Override
    public int getBattery() {
        return battery;
    }

}