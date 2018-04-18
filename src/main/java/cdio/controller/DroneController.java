package cdio.controller;

import cdio.controller.interfaces.IDroneController;
import cdio.handler.QRCodeException;
import cdio.handler.QRCodeHandler;
import cdio.model.QRCodeData;
import cdio.ui.interfaces.MessageListener;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.LEDAnimation;
import de.yadrone.base.configuration.ConfigurationManager;
import de.yadrone.base.navdata.*;
import de.yadrone.base.video.ImageListener;
import de.yadrone.base.video.VideoManager;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public final class DroneController implements IDroneController {

    private final int MAX_ALTITUDE = 2500; /* millimeters. */
    private final int MIN_ALTITUDE = 1000; /* millimeters. */

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

    private MessageListener messageListener;
    private final QRCodeHandler qrCodeHandler;

    private BufferedImage latestReceivedImage;

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
        drone.reset();

        /* Instantiate manager objects */
        commandManager = drone.getCommandManager();
        videoManager = drone.getVideoManager();
        navDataManager = drone.getNavDataManager();
        configManager = drone.getConfigurationManager();

        qrCodeHandler = new QRCodeHandler();

        /* Start listeners */
        startAcceleroListener();
        startAttitudeListener();
        startAltitudeListener();
        startBatteryListener();
        startImageListener();
    }

    private void startAcceleroListener() {
        navDataManager.addAcceleroListener(new AcceleroListener() {
            @Override
            public void receivedRawData(AcceleroRawData acceleroRawData) {
                System.out.println("Accelero data: " + acceleroRawData.getRawAccs().toString());
            }

            @Override
            public void receivedPhysData(AcceleroPhysData acceleroPhysData) {
                System.out.println("Accelero data: " + acceleroPhysData.getPhysAccs().toString());
            }
        });
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
        messageListener.messageCommandStartEventOccurred("Start");
        messageListener.messageCommandEventOccurred(this, "Drone starting...");

        setLEDAnimation(LEDAnimation.BLINK_GREEN, 3, 10);
        /* Start the drone */
        drone.start();
        /* Wait to settle for commands... */
        sleep(2000);

        messageListener.messageCommandEventOccurred(this, "Drone started!");
        messageListener.messageCommandEndEventOccurred();
    }

    /**
     * Method to initialize the drone.
     * This method should be called before flying.
     */
    @Override
    public final void initDrone() throws DroneControllerException {
        messageListener.messageCommandStartEventOccurred("Init");
        messageListener.messageCommandEventOccurred(this, "Drone initializing...");

        setSpeed(INITIAL_SPEED);
        commandManager.setMinAltitude(MIN_ALTITUDE);
        commandManager.setMaxAltitude(MAX_ALTITUDE);
        /* Wait to settle for commands... */
        sleep(2000);

        messageListener.messageCommandEventOccurred(this, "Drone initialized!");
        messageListener.messageCommandEndEventOccurred();
    }

    /**
     * Method to stop the drone.
     * NOTICE: This method does NOT land the drone. It only stops it/turns it off.
     */
    @Override
    public final void stopDrone() throws DroneControllerException {
        messageListener.messageCommandStartEventOccurred("Stop");
        messageListener.messageCommandEventOccurred(this, "Drone stopping...");

        setLEDAnimation(LEDAnimation.BLINK_RED, 3, 10);
        drone.stop();
        sleep(1000);

        messageListener.messageCommandEventOccurred(this, "Drone stopped!");
        messageListener.messageCommandEndEventOccurred();
    }

    /**
     * Method to make the drone take off.
     */
    @Override
    public final void takeOffDrone() throws DroneControllerException {
        messageListener.messageCommandStartEventOccurred("Take Off");
        messageListener.messageCommandEventOccurred(this, "Drone taking off...");

        setLEDAnimation(LEDAnimation.BLINK_GREEN_RED, 3, 10);
        commandManager.setOutdoor(false, true);
        commandManager.flatTrim();
        /* Wait to settle for commands... */
        sleep(1000);
        commandManager.takeOff().doFor(200);

        messageListener.messageCommandEventOccurred(this, "Drone taken off!");
        messageListener.messageCommandEndEventOccurred();
    }

    /**
     * Method to make the drone land.
     */
    @Override
    public final void landDrone() throws DroneControllerException {
        messageListener.messageCommandStartEventOccurred("Land");
        messageListener.messageCommandEventOccurred(this, "Drone landing...");

        setLEDAnimation(LEDAnimation.BLINK_GREEN_RED, 3, 10);
        setSpeed(LANDING_SPEED);
        sleep(500);

        commandManager.landing().doFor(200);

        messageListener.messageCommandEventOccurred(this, "Drone landed!");
        messageListener.messageCommandEndEventOccurred();
    }

    /**
     * Method to make the drone hover for timeMillis ms.
     */
    @Override
    public final void hoverDrone(int timeMillis) throws DroneControllerException {
        messageListener.messageCommandStartEventOccurred("Hover");
        messageListener.messageCommandEventOccurred(this, "Drone hovering for " + timeMillis + " milliseconds...");

        commandManager.hover().waitFor(timeMillis);

        messageListener.messageCommandEventOccurred(this, "Drone finished hovering!");
        messageListener.messageCommandEndEventOccurred();
    }

    /**
     * Method to make the drone hover.
     */
    @Override
    public void hoverDrone() throws DroneControllerException {
        messageListener.messageCommandStartEventOccurred("Hover");
        messageListener.messageCommandEventOccurred(this, "Drone hovering...");

        commandManager.hover();

        messageListener.messageCommandEventOccurred(this, "Drone finished hovering!");
        messageListener.messageCommandEndEventOccurred();
    }

    Map<String, Float> map = new HashMap<String, Float>();

    /**
     * Method to make the drone rotate to a target yaw.
     */
    @Override
    public final void doSearchRotation() throws DroneControllerException {
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
                System.out.println("CALCULATED TargetYaw: " + targetYaw);
            }
        } else if (targetYaw < -179) {
            targetYaw = 360 + targetYaw;
            System.out.println("CALCULATED TargetYaw: " + targetYaw);
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

            } catch (QRCodeException ignored) {
                // no qr detected
            }

            commandManager.hover().doFor(50);
            sleep(500);
        }

    }

    @Override
    public final void circleAroundObject() throws DroneControllerException {

    }

    /**
     * Method to make the drone fly forwards.
     */
    @Override
    public final void flyForward(int timeMillis) throws DroneControllerException {
        messageListener.messageCommandStartEventOccurred("Forward");
        messageListener.messageCommandEventOccurred(this, "Drone flying forward...");

        commandManager.forward(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        messageListener.messageCommandEventOccurred(this, "Drone finished flying forward!");
        messageListener.messageCommandEndEventOccurred();
    }

    /**
     * Method to make the drone fly backwards.
     */
    @Override
    public final void flyBackward(int timeMillis) throws DroneControllerException {
        messageListener.messageCommandStartEventOccurred("Backward");
        messageListener.messageCommandEventOccurred(this, "Drone flying backward...");

        commandManager.backward(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        messageListener.messageCommandEventOccurred(this, "Drone finished flying backward!");
        messageListener.messageCommandEndEventOccurred();
    }

    /**
     * Method to make the drone fly upwards.
     */
    @Override
    public final void flyUp(int timeMillis) throws DroneControllerException {
        messageListener.messageCommandStartEventOccurred("Up");
        messageListener.messageCommandEventOccurred(this, "Drone flying up...");

        commandManager.up(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        messageListener.messageCommandEventOccurred(this, "Drone finished flying up!");
        messageListener.messageCommandEndEventOccurred();
    }

    /**
     * Method to make the drone fly downwards.
     */
    @Override
    public final void flyDown(int timeMillis) throws DroneControllerException {
        messageListener.messageCommandStartEventOccurred("Down");
        messageListener.messageCommandEventOccurred(this, "Drone flying down...");

        commandManager.down(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        messageListener.messageCommandEventOccurred(this, "Drone finished flying down!");
        messageListener.messageCommandEndEventOccurred();
    }

    /**
     * Method to make the drone fly left.
     */
    @Override
    public final void flyLeft(int timeMillis) throws DroneControllerException {
        messageListener.messageCommandStartEventOccurred("Left");
        messageListener.messageCommandEventOccurred(this, "Drone flying left...");

        commandManager.goLeft(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        messageListener.messageCommandEventOccurred(this, "Drone finished flying left!");
        messageListener.messageCommandEndEventOccurred();
    }

    /**
     * Method to make the drone fly right.
     */
    @Override
    public final void flyRight(int timeMillis) throws DroneControllerException {
        messageListener.messageCommandStartEventOccurred("Right");
        messageListener.messageCommandEventOccurred(this, "Drone flying right...");

        commandManager.goRight(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        messageListener.messageCommandEventOccurred(this, "Drone finished flying right!");
        messageListener.messageCommandEndEventOccurred();
    }

    @Override
    public void setSpeed(int speed) throws DroneControllerException {
        messageListener.messageCommandStartEventOccurred("Speed");

        if (speed > 100 || speed < 10) {
            messageListener.messageCommandEventOccurred(this, "Illegal speed: " + speed);
            messageListener.messageCommandEndEventOccurred();
            return;
        }

        drone.setSpeed(speed);
        //DroneController.this.messageListener.setSpeed(speed);

        messageListener.messageCommandEventOccurred(this, "Setting drone speed: " + speed);
        messageListener.messageCommandEndEventOccurred();
    }

    @Override
    public int getSpeed() throws DroneControllerException {
        return drone.getSpeed();
    }

    /**
     * Method to reset the drone.
     */
    @Override
    public final void resetDrone() throws DroneControllerException {
        messageListener.messageCommandStartEventOccurred("Reset");
        messageListener.messageCommandEventOccurred(this, "Resetting drone...");

        drone.reset();
        sleep(1000);

        messageListener.messageCommandEventOccurred(this, "Drone has been reset!");
        messageListener.messageCommandEndEventOccurred();
    }

    /**
     * Method to get the controllers internal drone object.
     */
    @Override
    public final IARDrone getDrone() throws DroneControllerException {
        if (drone == null)
            throw new DroneControllerException("Drone object is null!");
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

                //DroneController.this.messageListener.setRoll((int) roll);
                //DroneController.this.messageListener.setPitch((int) pitch);
                //DroneController.this.messageListener.setYaw((int) yaw);
                //DroneController.this.messageListener.setCorrectedYaw(getCorrectedYaw());
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
                messageListener.setAltitude(altitude);
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
                //DroneController.this.messageListener.setBattery(battery);
            }

            @Override
            public void voltageChanged(int voltage) {

            }
        });
    }

    private void startImageListener() {
        videoManager.addImageListener(new ImageListener() {
            final int INITIAL_QR_SCAN_TIMER = 30;
            int qrScanTimer = INITIAL_QR_SCAN_TIMER;

            @Override
            public void imageUpdated(BufferedImage bufferedImage) {
                latestReceivedImage = bufferedImage;
                qrScanTimer--;
                if (qrScanTimer == 0) {
                    qrScanTimer = INITIAL_QR_SCAN_TIMER;
                    scanImageForQRCode(bufferedImage);
                }
            }
        });

    }

    private void scanImageForQRCode(BufferedImage bufferedImage) {
        try {
            QRCodeData qrData = qrCodeHandler.scanImage(bufferedImage);
            messageListener.messageCommandStartEventOccurred("QR Code Scanned");
            messageListener.messageCommandEventOccurred(this, "Result: " + qrData.getResult() + ", Width: " + qrData.getWidth() + ", Height: " + qrData.getHeight() + ", Orientation: " + qrData.getOrientation());
            messageListener.messageCommandEndEventOccurred();
        } catch (QRCodeException ignored) {

        }
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

    private float getCorrectedYaw() {
        float yawCorrected = yaw; // + yawCorrection;

        if (yawCorrected >= 180)
            yawCorrected = 359 - yawCorrected;
        else if (yawCorrected <= -180)
            yawCorrected = 359 + yawCorrected;

        return yawCorrected;
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

}