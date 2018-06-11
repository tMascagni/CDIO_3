package cdio.drone;

import cdio.cv.QRImg;
import cdio.cv.RingImg;
import cdio.drone.interfaces.IDroneCommander;
import cdio.handler.QRCodeHandler;
import cdio.handler.interfaces.IQRCodeHandler;
import org.opencv.core.Core;
import yadankdrone.ARDrone;
import yadankdrone.IARDrone;
import yadankdrone.command.CommandManager;
import yadankdrone.command.LEDAnimation;
import yadankdrone.navdata.*;
import yadankdrone.video.ImageListener;
import yadankdrone.video.VideoManager;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public final class DroneCommander implements IDroneCommander {

    /* Native library link for OpenCV. */
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /*
     * Global fields for the DroneCommander.
     */
    private final int MAX_ALTITUDE = 2500;            /* millimeters. */
    private final int MIN_ALTITUDE = 0;            /* millimeters. */

    private final int MAX_SPEED = 100;                /* percentage (%) */
    private final int MIN_SPEED = 10;                 /* percentage (%) */
    private int SPEED_ON_TAKE_OFF = 0;

    private final int INITIAL_SPEED = 20;             /* In %, speed goes from 0 to 100. */
    private final int LANDING_SPEED = 20;

    private float pitch, roll, yaw, altitude;         /* Drone data. */
    private int battery;                              /* Drone battery. */
    private long wifiStrength;

    private final IARDrone drone;                     /* Yadankdrone, drone object. */

    private final CommandManager commandManager;      /* Drone managers. */
    private final VideoManager videoManager;
    private final NavDataManager navDataManager;

    private final IQRCodeHandler qrCodeHandler = QRCodeHandler.getInstance();

    private int targetQrCode = 0;                     /* Current target QR code, 0, 1, 2, 3, ..., 7. */
    private Map<Integer, QRImg> qrCodeMap = new HashMap<>(); /* Code mapping. 0 to 7. */

    private boolean isQRCodeScanningEnabled = false;  /* Always scans for QR codes if true. */
    private boolean isRingScanningEnabled = false;    /* Always scans for rings if true. */

    private BufferedImage latestReceivedImage;        /* Latest received image from the camera. */

    private List<String> messageList = new ArrayList<>(); /* A list (queue) with all the recent
                                                             messages from the commander. */

    private static IDroneCommander instance;              /* Singleton DroneCommander object. */
    QRImg qr;


    /**
     * Static block that instantiates the Singleton instance.
     */
    static {
        try {
            instance = new DroneCommander();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Singleton DroneCommander instance!");
        }
    }

    /**
     * Default constructor. Private since we don't
     * want the commander to be instantiated.
     * <p>
     * Used to initialize commander objects
     * and start various initializing routines.
     */
    private DroneCommander() {
        /* Instantiate Drone object. */
        drone = new ARDrone();
        drone.reset();

        /* Instantiate manager objects. */
        commandManager = drone.getCommandManager();
        videoManager = drone.getVideoManager();
        navDataManager = drone.getNavDataManager();

        /* Start listeners. */
        startAttitudeListener();
        startAltitudeListener();
        startBatteryListener();
        startImageListener();
        startWiFiListener();

        /* Initialize QR map. */
        initQRMap();
    }

    /**
     * Getter for the Singleton commander instance.
     *
     * @return The Singleton IDroneCommander instance.
     */
    public static synchronized IDroneCommander getInstance() {
        return instance;
    }

    /**
     * Starts the drone, establishes connection, etc.
     * <p>
     * NOTICE: Safe method, does not make the take flight.
     */
    @Override
    public final void startDrone() {
        addMessage("Drone starting...");
        setLEDAnimation(LEDAnimation.BLINK_GREEN, 3, 5);

        /* Start the drone */
        drone.start();
        /* Wait to settle for commands... */
        sleep(1000);
        drone.reset();
        sleep(1000);

        setLEDAnimation(LEDAnimation.BLANK, 1, 1);
        addMessage("Drone started!");
    }

    /**
     * Stops the drone.
     * <p>
     * NOTICE: This stops the drone, it does not make the drone fly.
     * DO NOT use this method while the drone is in flight.
     */
    @Override
    public final void stopDrone() {
        addMessage("Drone stopping...");
        setLEDAnimation(LEDAnimation.BLINK_RED, 3, 5);

        drone.stop();
        sleep(1000);

        setLEDAnimation(LEDAnimation.BLANK, 1, 1);
        addMessage("Drone stopped!");
    }

    /**
     * Initializes the drone. Sets the max/min altitudes, speed, etc.
     * <p>
     * NOTICE: Safe method, does not make the drone take flight.
     */
    @Override
    public final void initDrone() {
        addMessage("Drone initializing...");

        drone.setSpeed(INITIAL_SPEED);
        commandManager.setMinAltitude(MIN_ALTITUDE);
        commandManager.setMaxAltitude(MAX_ALTITUDE);

        /* Wait to settle for commands... */
        sleep(1000);

        addMessage("Drone initialized!");
    }

    /**
     * Resets the drone.
     * <p>
     * NOTICE: DO NOT CALL this method while the drone is in flight. It will fall down!
     */
    @Override
    public final void resetDrone() {
        addMessage("Resetting drone...");

        drone.reset();
        sleep(1000);

        addMessage("Drone reset!");
    }

    /**
     * Makes the drone take off. The drone takes flight and stays in its place.
     * <p>
     * NOTICE: The drone will take flight.
     */
    @Override
    public final void takeOffDrone() {
        addMessage("Drone taking off...");
        setLEDAnimation(LEDAnimation.BLINK_GREEN_RED, 3, 5);

        commandManager.setOutdoor(false, true);
        commandManager.flatTrim();
        /* Wait to settle for commands... */
        sleep(1000);
        commandManager.takeOff().doFor(200);

        setLEDAnimation(LEDAnimation.BLANK, 1, 1);
        addMessage("Drone taken off!");
    }

    /**
     * Makes the drone takeoff smoothly.
     * <p>
     * Notice: drone should takeoff smooth.
     */
    @Override
    public final void smoothInit() {
        addMessage("Drone Initializing...");

        int speedOntakeOff = 0;

        do {
            speedOntakeOff++;
            drone.setSpeed(speedOntakeOff);
        } while (speedOntakeOff < 20);

        commandManager.setMinAltitude(MIN_ALTITUDE);
        commandManager.setMaxAltitude(MAX_ALTITUDE);

        /* Wait to settle for commands... */
        sleep(1000);

        addMessage("Drone initialized!");
    }

    /**
     * Lands the drone.
     * <p>
     * NOTICE: This makes the drone land wherever it currently is.
     * Make sure its a safe place!
     */
    @Override
    public final void landDrone() {
        addMessage("Drone landing...");
        setLEDAnimation(LEDAnimation.BLINK_ORANGE, 3, 5);

        setSpeed(LANDING_SPEED);
        sleep(500);
        commandManager.landing().doFor(200);

        setLEDAnimation(LEDAnimation.BLANK, 1, 1);
        addMessage("Drone landed!");
    }

    /**
     * Make the drone hover and stay in its place,
     * for a specific amount of time.
     *
     * @param timeMillis The amount of milliseconds the drone should hover.
     */
    @Override
    public final void hoverDrone(int timeMillis) {
        addMessage("Drone hovering for " + timeMillis + " ms...");

        commandManager.hover().waitFor(timeMillis);

        addMessage("Drone finished hovering!");
    }

    /**
     * Hovers the drone until further commands.
     */
    @Override
    public void hoverDrone() {
        addMessage("Drone hovering...");

        commandManager.hover();

        addMessage("Drone finished hovering!");
    }

    /**
     * Makes the drone fly forward for a specific amount of time.
     *
     * @param timeMillis The amount of milliseconds the drone should fly forward.
     */
    @Override
    public final void flyForward(int timeMillis) {
        addMessage("Drone flying forward for " + timeMillis + " ms...");

        commandManager.forward(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        addMessage("Drone done flying forward!");
    }

    /**
     * Makes the drone fly backward for a specific amount of time.
     *
     * @param timeMillis The amount of milliseconds the drone should fly backward.
     */
    @Override
    public final void flyBackward(int timeMillis) {
        addMessage("Drone flying backward for " + timeMillis + " ms...");

        commandManager.backward(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        addMessage("Drone done flying backward!");
    }

    /**
     * Makes the drone fly upwards for a specific amount of time.
     *
     * @param timeMillis The amount of milliseconds the drone should fly upwards.
     */
    @Override
    public final void flyUp(int timeMillis) {
        addMessage("Drone flying upwards for " + timeMillis + " ms...");

        commandManager.up(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        addMessage("Drone done flying upwards!");
    }

    /**
     * Makes the drone fly downwards for a specific amount of time.
     *
     * @param timeMillis The amount of milliseconds the drone should fly downwards.
     */
    @Override
    public final void flyDown(int timeMillis) {
        addMessage("Drone flying downwards for " + timeMillis + " ms...");

        commandManager.down(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        addMessage("Drone done flying downwards!");
    }

    /**
     * Makes the drone fly left for a specific amount of time.
     *
     * @param timeMillis The amount of milliseconds the drone should fly left.
     */
    @Override
    public final void flyLeft(int timeMillis) {
        addMessage("Drone flying left for " + timeMillis + " ms...");

        commandManager.goLeft(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        addMessage("Drone done flying left!");
    }

    /**
     * Makes the drone fly right for a specific amount of time.
     *
     * @param timeMillis The amount of milliseconds the drone should fly right.
     */
    @Override
    public final void flyRight(int timeMillis) {
        addMessage("Drone flying right for " + timeMillis + " ms...");

        commandManager.goRight(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        addMessage("Drone done flying right!");
    }

    /**
     * Makes the drone fly upwards until it has reached
     * a specific altitude.
     *
     * @param altitude The target altitude for the drone.
     */
    @Override
    public void flyUpToAltitude(int altitude) {
        addMessage("Flying upwards to altitude: " + altitude + "...");

        while (getAltitude() <= altitude)
            flyUp(350);

        addMessage("Done flying upwards to altitude: " + altitude + "!");
    }

    /**
     * Makes the drone fly downwards until it has reached
     * a specific altitude.
     *
     * @param altitude The target altitude for the drone.
     */
    @Override
    public void flyDownToAltitude(int altitude) {
        addMessage("Flying downwards to altitude: " + altitude + "...");

        while (getAltitude() >= altitude)
            flyDown(350);

        addMessage("Done flying downwards to altitude: " + altitude + "!");
    }

    /**
     * Makes the drone rotate 180 degrees right, scanning for QR codes meanwhile,
     * and maps them giving the yaw they've been found at.
     * <p>
     * If the targeted QR code is found, it is returned. If the target QR code is not found,
     * null is returned.
     *
     * @return QRImg if correct QR code is scanned and read, null if not.
     * @throws DroneCommanderException Currently not used.
     */
    @Override
    public final QRImg searchForQRCode() {
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

        /*
         * Here we get a corrected targetYaw, wrapping around 180 and -180.
         *
         */
        addMessage("Start yaw: " + yaw);
        int targetYaw = (int) (getCorrectYaw(yaw) + 180);
        addMessage("TargetYaw: " + targetYaw);
        targetYaw = getCorrectTargetYaw(targetYaw);
        addMessage("Corrected TargetYaw: " + targetYaw);

        int negativeBound = -8;
        int positiveBound = 8;

        while ((yaw = (getCorrectYaw(yaw) - targetYaw)) < negativeBound
                || yaw > positiveBound) { // default -8 og 8 :) // -23 og 23 virker fint.

            if (latestReceivedImage != null) {

                QRImg qrImg = null;
                try {
                    qrImg = qrCodeHandler.scanImageForBest(latestReceivedImage, this);
                } catch (IQRCodeHandler.QRCodeHandlerException ignored) {

                }

                if (qrImg != null && qrImg.isQRCodeRead()) {

                    int qrCodeResult = qrImg.getQrCodeData().getResult();

                    // QR CODE TARGET FOUND!
                    if (isQRCodeTarget(qrCodeResult)) {
                        updateQRMap(qrCodeResult, qrImg);
                        incQRCodeTarget(); // TODO: Do this after the ring has been passed.
                        addMessage("Found correct QR code: " + qrCodeResult + ", Yaw: " + yaw + ", New QR Code target: " + targetQrCode);
                        return qrImg;
                        // TODO: Fly to the code.
                        // clear the map after the drone has flown through the ring.
                        // qrCodeMap.clear();
                    } else {
                        // FOUND QR CODE, BUT NOT TARGET!
                        updateQRMap(qrCodeResult, qrImg);
                        addMessage("Found incorrect QR code: " + qrCodeResult + ", Yaw: " + yaw + ", Correct QR code: " + targetQrCode);
                        continue;
                    }
                }
            }

            commandManager.hover().doFor(20);

            yaw = getCorrectYaw(yaw);
            commandManager.spinRight(80).doFor(40);
            commandManager.spinLeft(80).doFor(10);

            commandManager.hover().doFor(20);
            sleep(500);
        }

        addMessage("Did not find any QR code.");
        return null;
    }


    /**
     * Undefined functionality. :)
     *
     * @throws DroneCommanderException
     */
    @Override
    public final boolean leftSideCheck() {

        boolean lSide;
        qr = qrCodeHandler.detectQR(this);
        Double lTempAngle = qr.getAngle();
        for (int i = 0; i <= 5; i++) {
            flyLeft(i);
            commandManager.spinRight(10).doFor(10);
        }
        lSide = lTempAngle < qr.getAngle();
        return lSide;
    }

    /**
     * Undefined functionality. :)
     *
     * @throws DroneCommanderException
     */
    @Override
    public final boolean rightSideCheck() {

        boolean rSide;
        qr = qrCodeHandler.detectQR(this);
        Double lTempAngle = qr.getAngle();
        for (int i = 0; i <= 5; i++) {
            flyRight(i);
            commandManager.spinLeft(10).doFor(10);
        }
        rSide = lTempAngle < qr.getAngle();
        return rSide;
    }


    /**
     * Undefined functionality. :)
     *
     * @throws DroneCommanderException
     */
    @Override
    public final void circleAroundObject() {
        qr = qrCodeHandler.detectQR(this);
        int centerOfQR = latestReceivedImage.getWidth() / 2;
        Double x = centerOfQR + cos(qr.getAngle()) * qr.getDistance();
        Double y = centerOfQR + sin(qr.getAngle()) * qr.getDistance();
        qr = qrCodeHandler.detectQR(this);
/*        do {
            flyForward(5);
            hoverDrone(10);
        }while (qr.getDistance() <= 70);
        addMessage("fly to point 1");
        do {
            flyRight(x.intValue()+5);
            commandManager.spinLeft(7).doFor(7);
            hoverDrone(10);
        }while (qr.getAngle() <= 20);
        addMessage("Vinkeln er om. 20" + qr.getAngle());
        addMessage("fly to point 2");
        do {
            flyRight(x.intValue()+5);
            commandManager.spinLeft(7).doFor(7);
            hoverDrone(10);
        }while (qr.getAngle() >= 70);
        addMessage("point 3 reached");
        hoverDrone(1000); // fly stealth
        addMessage("start to back to point 2");
        do {
            flyLeft(x.intValue()+5);
            commandManager.spinRight(7).doFor(7);
            hoverDrone(10);
        }while (qr.getAngle() >= 20);
        addMessage("Vinkel er om. 20,  point 2 reached" + qr.getAngle());
        addMessage("start to back to point 1");
        do {
            flyLeft(x.intValue()+5);
            commandManager.spinRight(7).doFor(7);
            hoverDrone(10);
        }while (qr.getAngle() >= 70);
        hoverDrone(1000); // fly stealth
        }*/

/*       addMessage("vinkel"+ qr.getAngle());
        for (int i= 0; i<= 20; i++){
            qr = qrCodeHandler.detectQR(this);
            flyRight(x.intValue()+10);
            addMessage("vinkel"+ qr.getAngle());
            commandManager.spinRight(10).doFor(10);
            if (qr.getAngle() <= 20.01){
                landDrone();
                stopDrone();
            }
        }*/

        if (leftSideCheck() == true) {
            do {
                flyRight(x.intValue());
                flyForward(2);
                hoverDrone(5);
                commandManager.spinRight(10).doFor(10);
            } while (qr.getAngle() < 20.01 && qr.getDistance() < 70.00);
            landDrone();
            stopDrone();
        } else if (rightSideCheck() == true) {
            do {
                flyLeft(x.intValue());
                flyForward(3);
                hoverDrone(5);
                commandManager.spinRight(20).doFor(20);
            } while (qr.getAngle() < 20.01 && qr.getDistance() < 50.00);
            landDrone();
            stopDrone();
        }
    }

    /**
     * Make the drone rotate right until it has reached the target yaw.
     *
     * @param targetYaw The yaw the drone should rotate to.
     * @throws DroneCommanderException Currently not used.
     */
    @Override
    public void rotateDrone(int targetYaw) {
        targetYaw = getCorrectTargetYaw(targetYaw);
        addMessage("Rotating to targetYaw: " + targetYaw + "...");

        if (targetYaw < 0) {
            targetYaw -= 20;
        } else {
            targetYaw += 20;
        }

        targetYaw = getCorrectTargetYaw(targetYaw);

        int negativeBound = -5;
        int positiveBound = 5;

        while ((yaw = (getCorrectYaw(yaw) - targetYaw)) < negativeBound
                || yaw > positiveBound) { // default -8 og 8 :) // -23 og 23 virker fint.
            yaw = getCorrectYaw(yaw);

            commandManager.hover().doFor(50);

            commandManager.spinRight(80).doFor(40);
            commandManager.spinLeft(80).doFor(20);

            commandManager.hover().doFor(50);
        }

        addMessage("Done rotating to targetYaw: " + targetYaw + "!");
    }

    /**
     * Attempts to scan for QR codes, if found, the drone
     * will be centered right across from the QR code.
     *
     * @throws DroneCommanderException Currently not used.
     */
    @Override
    public void adjustToCenterFromQR() {
        addMessage("Centering on QR code...");

        QRImg qrImg = null;
        int centerOfFrameX = -1;

        do {
            while (latestReceivedImage == null) {
                sleep(200);
            }

            if (latestReceivedImage != null) {
                centerOfFrameX = latestReceivedImage.getWidth() / 2;
            }

            do {
                qrImg = qrCodeHandler.detectQR(this);

                if (qrImg == null) {
                    addMessage("Failed to detect QR code!");
                }

                if (latestReceivedImage != null && qrImg != null) {
                    centerOfFrameX = latestReceivedImage.getWidth() / 2;
                }

                sleep(200);
            } while (qrImg == null);

            if (qrImg.getPosition().x > centerOfFrameX) {
                flyRight(200);
            } else {
                flyLeft(200);
            }

            commandManager.hover().waitFor(100);
            sleep(400);

        } while (qrImg.getPosition().x <= centerOfFrameX - 50 || qrImg.getPosition().x >= centerOfFrameX + 50);

        if (qrImg.isQRCodeRead()) {
            addMessage("Centered on QR code! QR code read: " + qrImg.getQrCodeData().getResult());
        } else {
            addMessage("Centered on QR code! QR code not read.");
        }
    }

    /**
     * Makes the drone fly forward until it is right infront of a scanned QR code.
     *
     * @param centerOnTheWay Should the drone center infront of the QR code?
     * @return True if infront of QR code, false if not.
     * @throws DroneCommanderException Thrown if any errors occur.
     */
    @Override
    public boolean flyToTargetQRCode(boolean centerOnTheWay) {
        QRImg qrImg = null;
        int count = 0;

        while (qrImg == null) {

            while (latestReceivedImage == null) {
                sleep(200);
            }

            try {
                qrImg = qrCodeHandler.scanImageForBest(getLatestReceivedImage(), this);
            } catch (IQRCodeHandler.QRCodeHandlerException e) {
                // did not read QR code
            }

            count++;
            if (count >= 1000) {
                addMessage("Could not find any QR code to fly to!");
                return false;
            }
        }

        double dist = qrImg.getDistance();

        while (dist > 90) {
            addMessage("Flying to QR code. Distance: " + dist);

            flyForward(50);
            sleep(100);

            if (centerOnTheWay) {
                adjustToCenterFromQR();
                commandManager.hover().waitFor(300);
            }

            commandManager.hover().waitFor(200);

            do {
                try {
                    qrImg = qrCodeHandler.scanImageForBest(getLatestReceivedImage(), this);
                    dist = qrImg.getDistance();
                    addMessage("New distance: " + dist);
                } catch (IQRCodeHandler.QRCodeHandlerException ignored) {
                    qrImg = null;
                }
            } while (qrImg == null);

            if (dist < 60) {
                flyBackward(200);
                sleep(100);
            }
        }

        if (centerOnTheWay) {
            adjustToCenterFromQR();
            commandManager.hover().waitFor(200);
        }

        if (qrImg.isQRCodeRead()) {
            addMessage("Reached QR code: " + qrImg.getQrCodeData().toString() + ". Distance: " + dist);
        } else {
            addMessage("Reached QR code. Not read. Distance: " + dist);
        }

        commandManager.hover().waitFor(200);

        return true;
    }

    /**
     * Makes the drone adjust to the center of the ring right in front.
     *
     * @throws DroneCommanderException Thrown if any errors occur.
     */
    @Override
    public void centerOnRing() {
        QRImg qr = null;
        RingImg ring = null;
        BufferedImage image = null;
        int target = 0;

        int tries = 0;
        int range = 50;

        do {
            while (ring == null) {
                image = getLatestReceivedImage();
                if (image != null) {
                    target = image.getHeight() / 2;
                    qr = qrCodeHandler.detectQR(this);
                    ring = qrCodeHandler.findRing(image, qr);
                }
                tries++;
                if (tries > 200) {
                    return;
                }
                sleep(200);
            }
            if (target > ring.getPosition().y) {
                addMessage("Higher");
            } else {
                addMessage("Lower");
            }

            sleep(1000);
        } while (true);
        //while (target + range > ring.getPosition().y || target - range < ring.getPosition().y);


    }

    /******************************************************
     *                     MESSAGES
     ******************************************************/

    /**
     * Adds a message to the GUI message queue list.
     *
     * @param msg The message to be added to the queue.
     */
    @Override
    public void addMessage(String msg) {
        messageList.add(msg);
    }

    /**
     * Get all the new messages from the queue.
     *
     * @return All the messages that haven't been shown to the GUI so far.
     */
    @Override
    public List<String> getNewMessages() {
        List<String> msgs = new ArrayList<>(messageList);
        messageList.clear();
        return msgs;
    }

    /******************************************************
     *                 GETTERS & SETTERS
     ******************************************************/

    /**
     * Returns the current speed of the drone.
     *
     * @return Current speed of the drone.
     */
    @Override
    public int getSpeed() {
        return drone.getSpeed();
    }

    /**
     * Sets the speed of the drone. (10-100%)
     *
     * @param speed Desired speed of the drone.
     */
    @Override
    public void setSpeed(int speed) {
        if (speed > MAX_SPEED || speed < MIN_SPEED) {
            addMessage("Attempted to set illegal drone speed: " + speed + "!");
            return;
        }

        drone.setSpeed(speed);
        addMessage("New drone speed: " + speed + "!");
    }

    /**
     * Getter for the yadankdrone object.
     *
     * @return yadankdrone object.
     * @throws DroneCommanderException Thrown if the object is null.
     */
    @Override
    public final IARDrone getDrone() throws DroneCommanderException {
        if (drone == null)
            throw new DroneCommanderException("Drone object is null!");
        return drone;
    }

    /**
     * @return True if scanning for QR codes, false if not scanning.
     */
    @Override
    public boolean isQRCodeScanningEnabled() {
        return isQRCodeScanningEnabled;
    }

    /**
     * Sets whether the drone is scanning for QR codes or not.
     *
     * @param isQRCodeScanningEnabled True if scanning, false if not.
     */
    @Override
    public void setQRCodeScanningEnabled(boolean isQRCodeScanningEnabled) {
        this.isQRCodeScanningEnabled = isQRCodeScanningEnabled;
    }

    /**
     * @return True if scanning for rings, false if not.
     */
    @Override
    public boolean isRingScanningEnabled() {
        return isRingScanningEnabled;
    }

    /**
     * @param isRingScanningEnabled True if scanning for rings, false if not.
     */
    @Override
    public void setRingScanningEnabled(boolean isRingScanningEnabled) {
        this.isRingScanningEnabled = isRingScanningEnabled;
    }

    /**
     * @return True if video is connected, false if not.
     */
    @Override
    public boolean isVideoConnected() {
        return videoManager.isConnected();
    }

    /**
     * @return True if nav manager is connected, false if not.
     */
    @Override
    public boolean isNavManagerConnected() {
        return navDataManager.isConnected();
    }

    /**
     * @return Get current pitch of the drone.
     */
    @Override
    public float getPitch() {
        return pitch;
    }

    /**
     * @return Get current roll of the drone.
     */
    @Override
    public float getRoll() {
        return roll;
    }

    /**
     * Correct a target yaw, to make sure it does not overflow.
     *
     * @param targetYaw The targetYaw to correct.
     * @return The corrected targetYaw.
     */
    @Override
    public int getCorrectTargetYaw(int targetYaw) {
        if (targetYaw >= 180) {
            targetYaw = targetYaw - 360;
        } else if (targetYaw <= -180) {
            targetYaw = 360 - targetYaw;
        }
        return targetYaw;
    }

    /**
     * Correct a yaw, to make sure it does not overflow.
     *
     * @param yaw The yaw to correct.
     * @return The corrected yaw.
     */
    @Override
    public float getCorrectYaw(float yaw) {
        if (yaw >= 180)
            yaw = yaw - 360;
        else if (yaw <= -180)
            yaw = 360 - yaw;

        return yaw;
    }

    /**
     * @return The current yaw of the drone.
     */
    @Override
    public float getYaw() {
        return yaw;
    }

    /**
     * @return The current altitude of the drone.
     */
    @Override
    public float getAltitude() {
        return altitude;
    }

    /**
     * @return The maximum allowed altitude.
     */
    @Override
    public int getMaxAltitude() {
        return MAX_ALTITUDE;
    }

    /**
     * @return The minimum allowed altitude.
     */
    @Override
    public int getMinAltitude() {
        return MIN_ALTITUDE;
    }

    /**
     * @return The current battery of the drone.
     */
    @Override
    public int getBattery() {
        return battery;
    }

    /**
     * @return The current WiFi strength.
     */
    @Override
    public long getWiFiStrength() {
        return wifiStrength;
    }

    /**
     * @return The latest received image from the camera.
     */
    public BufferedImage getLatestReceivedImage() {
        return latestReceivedImage;
    }

    /******************************************************
     *                   QR CODE MAPPING
     ******************************************************/

    /**
     * Updates the QR code map with qrNumber and qrImg.
     *
     * @param qrNumber QR code (ring) number
     * @param qrImg    The actual QR code data
     */
    @Override
    public void updateQRMap(int qrNumber, QRImg qrImg) {
        /*
         * If the qrCodeMap already contains QR Code data at the given mapNumber,
         * do not update it, since we don't want to overwrite it.
         *
         * Only overwrite it, if the angle of the new QRImg is smaller than the
         * previous QRImg, since that means we have a new, more centered, image of the
         * same QRCode.
         */
        if (qrCodeMap.get(qrNumber) != null) {
            // If the angle of the new image is smaller than the
            // previous image angle, then update it, since it is a better picture.
            if (qrImg.getAngle() < qrCodeMap.get(qrNumber).getAngle()) {
                qrCodeMap.put(qrNumber, qrImg);
            }
        }

        /*
         * If the map does not contain it already, then put it into the map.
         */
        qrCodeMap.putIfAbsent(qrNumber, qrImg);
    }

    /**
     * @return The current known tallest mapped QR code.
     */
    @Override
    public QRImg getTallestQRCode() {
        int index = -1;
        double greatestHeight = -1;

        for (int key : qrCodeMap.keySet()) {
            QRImg qrImg = qrCodeMap.get(key);

            if (qrImg == null || qrImg.getQrCodeData() == null)
                continue;

            if (qrImg.getH() > greatestHeight) {
                greatestHeight = qrImg.getH();
                index = key;
            }
        }

        if (greatestHeight == -1 || index == -1) {
            return null;
        }

        return qrCodeMap.get(index);
    }

    /**
     * @return The current wanted QR code. (0-7.)
     */
    @Override
    public int getTargetQRCode() {
        return targetQrCode;
    }

    /**
     * @return The QR code ring map.
     */
    @Override
    public Map<Integer, QRImg> getQRMap() {
        return qrCodeMap;
    }

    /**
     * Check whether a QR code number is the wanted target.
     *
     * @param possibleTarget The QR code number to be checked.
     * @return True if target, false if not.
     */
    @Override
    public boolean isQRCodeTarget(int possibleTarget) {
        return targetQrCode == possibleTarget;
    }

    /**
     * Increment the current target QR code.
     * Maximum is 7, because there's 7 rings.
     * 0 is first, then 1, 2, 3, ... 7.
     */
    @Override
    public void incQRCodeTarget() {
        if (targetQrCode < 7) {
            targetQrCode++;
            addMessage("New QR code target: " + targetQrCode + "!");
        }
    }

    /**
     * Sleep the code for a specific amount of time.
     *
     * @param timeMillis The amount of milliseconds the code should sleep.
     */
    @Override
    public void sleep(int timeMillis) {
        try {
            Thread.sleep(timeMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException("SLEEP DID NOT WORK!");
        }
    }

    /******************************************************
     *                   PRIVATE METHODS
     ******************************************************/

    /******************************************************
     *               START LISTENER METHODS
     ******************************************************/

    /**
     * Start the attitude listener of the drone,
     * and keep updating pitch, roll and yaw data.
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
     * Start the altitude listener of the drone,
     * and keep updating the altitude data.
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
     * Start the battery listener of the drone,
     * and keep updating the battery data.
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

    /**
     * Start the WiFi listener of the drone,
     * and keep updating the WiFi data.
     */
    private void startWiFiListener() {
        navDataManager.addWifiListener(strength -> this.wifiStrength = strength);
    }

    /**
     * Start the image listener of the drone,
     * and keep updating the latest received image.
     */
    private void startImageListener() {
        videoManager.addImageListener(new ImageListener() {
            int timer = 500;
            final int INITIAL_QR_SCAN_TIMER = 30;
            int qrScanTimer = INITIAL_QR_SCAN_TIMER;

            @Override
            public void imageUpdated(BufferedImage bufferedImage) {

                if (bufferedImage != null) {
                    latestReceivedImage = bufferedImage;
                }

                   /*
                if (bufferedImage != null) {
                    latestReceivedImage = bufferedImage;
                    timer = 500;
                } else {
                    timer--;
                    if (timer == 0) {
                        videoManager.reinitialize();
                        addMessage("Reinitialized video stream!");
                        timer = 500;
                    }
                }
                */

                /*
                qrScanTimer--;
                if (qrScanTimer == 0) {
                    qrScanTimer = INITIAL_QR_SCAN_TIMER;

                    try {
                        QRImg qrImg = qrCodeHandler.scanImageForBest(latestReceivedImage, DroneCommander.this);

                        if (qrImg != null && qrImg.getQrCodeData() != null) {

                            int qrCodeResult = qrImg.getQrCodeData().getResult();

                            // QR CODE TARGET FOUND!
                            if (isQRCodeTarget(qrCodeResult)) {
                                updateQRMap(qrCodeResult, qrImg);
                                incQRCodeTarget(); // TODO: Do this after the ring has been passed.
                                addMessage("Found correct QR code: " + qrCodeResult);
                                addMessage("New Target QR: " + targetQrCode);
                                addMessage(qrCodeMap.toString());
                                QRImg heightQR = getTallestQRCode();
                                addMessage("Greatest height code: " + heightQR.getQrCodeData().getResult() + ", Height: " + heightQR.getH());
                                //return qrImg;
                                // TODO: Fly to the code.
                                // clear the map after the drone has flown through the ring.
                                // qrCodeMap.clear();
                            } else {
                                // FOUND QR CODE, BUT NOT TARGET!
                                updateQRMap(qrCodeResult, qrImg);
                                addMessage("Found incorrect QR code: " + qrCodeResult + ", Target: " + targetQrCode);
                                addMessage(qrCodeMap.toString());
                                QRImg heightQR = getTallestQRCode();
                                addMessage("Greatest height code: " + heightQR.getQrCodeData().getResult() + ", Height: " + heightQR.getH());
                                //continue;
                            }
                        }

                    } catch (IQRCodeHandler.QRCodeHandlerException ignored) {
                        // no qr detected which is fine.
                    }

                }
            */

            }
        });
    }

    /******************************************************
     *               OTHER PRIVATE METHODS
     ******************************************************/

    /**
     * Initialize the QR code map with 8 objects, (0-7)
     * since there's 7 rings. No more entries should ever be created.
     */
    private void initQRMap() {
        for (int qrNumber = 0; qrNumber <= 7; qrNumber++)
            qrCodeMap.put(qrNumber, null);
    }

    /**
     * Start a cool LED animation!
     *
     * @param ledAnimation The LED animation.
     * @param freq         The frequency of the animation.
     * @param duration     The duration of the animation.
     */
    private void setLEDAnimation(LEDAnimation ledAnimation, int freq, int duration) {
        commandManager.setLedsAnimation(ledAnimation, freq, duration);
    }

}