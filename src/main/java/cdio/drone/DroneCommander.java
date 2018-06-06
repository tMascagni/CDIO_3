package cdio.drone;

import cdio.cv.CVHelper;
import cdio.cv.DisCal;
import cdio.cv.QRDetector;
import cdio.cv.QRImg;
import cdio.drone.interfaces.IDroneCommander;
import cdio.handler.QRCodeHandler;
import cdio.handler.interfaces.IQRCodeHandler;
import cdio.model.QRCodeData;
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

public final class DroneCommander implements IDroneCommander {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

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

    private final IQRCodeHandler qrCodeHandler = QRCodeHandler.getInstance();
    private BufferedImage latestReceivedImage;

    private List<String> messageList = new ArrayList<>();

    private int targetQrCode = 0;
    private Map<Integer, QRCodeData> qrCodeMap = new HashMap<>();

    private static IDroneCommander instance;

    private boolean QRCodeScaningEnabled = true;
    private boolean RingScaningEnabled = false;

    QRDetector qrDetector = new QRDetector();
    CVHelper cvHelper = new CVHelper();

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

        /* Start listeners */
        startAcceleroListener();
        startAttitudeListener();
        startAltitudeListener();
        startBatteryListener();
        startImageListener();

        /* Initialize QR code map */
        initQrCodeMap();
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

    @Override
    public int getCorrectTargetYaw(int targetYaw) {
        if (targetYaw >= 180) {
            targetYaw = targetYaw - 360;
        } else if (targetYaw <= -180) {
            targetYaw = 360 - targetYaw;
        }
        return targetYaw;
    }

    @Override
    public float getCorrectYaw(float yaw) {
        /*
        float yawCorrected = yaw; // + yawCorrection;

        if (yawCorrected >= 180)
            yawCorrected = 360 - yawCorrected;
        else if (yawCorrected <= -180)
            yawCorrected = 360 + yawCorrected;

        return yawCorrected;
        */
        // + yawCorrection;

        if (yaw >= 180)
            yaw = yaw - 360;
        else if (yaw <= -180)
            yaw = 360 - yaw;

        return yaw;
    }


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

            try {
                QRCodeData qrCodeData = qrCodeHandler.scanImage(latestReceivedImage, this);

                // QR CODE TARGET FOUND!
                if (isQrCodeTarget(qrCodeData.getResult())) {
                    updateQrCodeMapData(qrCodeData.getResult(), qrCodeData);
                    incQrCodeTarget(); // TODO: Do this after the ring has been passed.
                    addMessage("Found correct QR code: " + qrCodeData.getResult());
                    return qrCodeData;
                    // TODO: Fly to the code.
                    // clear the map after the drone has flown through the ring.
                    // qrCodeMap.clear();
                } else {
                    // FOUND QR CODE, BUT NOT TARGET!
                    updateQrCodeMapData(qrCodeData.getResult(), qrCodeData);
                    addMessage("Found incorrect QR code: " + qrCodeData.getResult());
                    continue;
                }

            } catch (IQRCodeHandler.QRCodeHandlerException ignored) {
                // no qr detected which is fine.
            }

            commandManager.hover().doFor(50);

            yaw = getCorrectYaw(yaw);
            commandManager.spinRight(80).doFor(40);
            commandManager.spinLeft(80).doFor(20);

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

    private void scanImageForQRCode(BufferedImage bufferedImage) {
        ArrayList<QRImg> qrCodes = qrDetector.processAll(cvHelper.buf2mat(bufferedImage));

        DisCal disCal = new DisCal();

        try {

            QRCodeData qrdata = qrCodeHandler.scanImage(cvHelper.mat2buf(qrCodes.get(0).getImg()), this);
            if (qrdata != null) {
                addMessage("Vinkel på QR kode: " + qrDetector.angleOfQRCode(qrCodes.get(0)));
                System.out.println("Vinkel på QR kode: " + qrDetector.angleOfQRCode(qrCodes.get(0)));

                addMessage("Result: " + qrdata.getResult() + ", Width: " + qrdata.getWidth() + ", Height: " + qrdata.getHeight() + ", Orientation: " + qrdata.getOrientation());
                System.out.println("Result: " + qrdata.getResult() + ", Width: " + qrdata.getWidth() + ", Height: " + qrdata.getHeight() + ", Orientation: " + qrdata.getOrientation());

                addMessage("Distance til QR: " + disCal.disCal(qrdata.getWidth()));
                System.out.println("Distance til QR: " + disCal.disCal(qrdata.getWidth()));


            }

        } catch (IQRCodeHandler.QRCodeHandlerException e) {
            e.printStackTrace();
            addMessage("Vinkel på QR kode: " + qrDetector.angleOfQRCode(qrCodes.get(0)));
            System.out.println("Vinkel på QR kode: " + qrDetector.angleOfQRCode(qrCodes.get(0)));
        }
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
        //videoManager.addImageListener(new QRCodeScanner());
        videoManager.addImageListener(new ImageListener() {
            final int INITIAL_QR_SCAN_TIMER = 30;
            int qrScanTimer = INITIAL_QR_SCAN_TIMER;

            @Override
            public void imageUpdated(BufferedImage bufferedImage) {
                latestReceivedImage = bufferedImage;

                if (QRCodeScaningEnabled) {
                    qrScanTimer--;
                    if (qrScanTimer == 0) {
                        qrScanTimer = INITIAL_QR_SCAN_TIMER;
                        scanImageForQRCode(bufferedImage);
                    }
                } else if (RingScaningEnabled) {
                    // søg efter ringe
                }
            }
        });
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

    private void initQrCodeMap() {
        for (int mapNumber = 0; mapNumber <= 7; mapNumber++) {
            qrCodeMap.put(mapNumber, null);
        }

        addMessage("QR Code Target: " + getTargetQrCode());
    }

    /**
     * Helper method to set a LED animation of the drone.
     */
    private void setLEDAnimation(LEDAnimation ledAnimation, int freq, int duration) {
        commandManager.setLedsAnimation(ledAnimation, freq, duration);
    }

    @Override
    public void addMessage(String msg) {
        messageList.add(msg);
    }

    @Override
    public List<String> getNewMessages() {
        return messageList;
    }

    public void updateQrCodeMapData(int mapNumber, QRCodeData qrCodeData) {
        /*
         * If the qrCodeMap already contains QR Code data at the given mapNumber,
         * do not update it, since we don't want to overwrite it.
         */
        if (qrCodeMap.get(mapNumber) != null) {
            return;
        }

        /*
         * If the map does not contain it already, then put it into the map.
         *
         * TODO: Update the data is the height is larger than the previous data.
         */
        qrCodeMap.putIfAbsent(mapNumber, qrCodeData);
    }

    @Override
    public void rotateDrone(int targetYaw) throws DroneCommanderException {
        /*
         * First get the correct target yaw.
         */
        targetYaw = getCorrectTargetYaw(targetYaw);

        if (targetYaw < 0) {
            targetYaw -= 40;
        } else {
            targetYaw += 45;
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

    }

    @Override
    public int getTargetQrCode() {
        return targetQrCode;
    }

    @Override
    public Map<Integer, QRCodeData> getQrCodeMap() {
        return qrCodeMap;
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
    public float getAltitude() {
        return altitude;
    }

    @Override
    public int getBattery() {
        return battery;
    }

    @Override
    public boolean isQrCodeTarget(int possibleTarget) {
        return targetQrCode == possibleTarget;
    }

    @Override
    public void incQrCodeTarget() {
        if (targetQrCode < 7)
            targetQrCode++;
    }

    @Override
    public QRCodeData getQrCodeWithGreatestHeight() throws DroneCommanderException {
        int index = -1;
        int greatestHeight = -1;

        for (int key : qrCodeMap.keySet()) {
            QRCodeData qrCodeData = qrCodeMap.get(key);

            if (qrCodeData == null)
                continue;

            if (qrCodeData.getHeight() > greatestHeight) {
                greatestHeight = qrCodeData.getHeight();
                index = key;
            }
        }

        if (greatestHeight == -1 || index == -1) {
            throw new DroneCommanderException("No QR codes in the map!");
        }

        return qrCodeMap.get(index);
    }

    public int getMAX_ALTITUDE() {
        return MAX_ALTITUDE;
    }

    public boolean isQRCodeScaningEnabled() {
        return QRCodeScaningEnabled;
    }

    public void setQRCodeScaningEnabled(boolean QRCodeScaningEnabled) {
        this.QRCodeScaningEnabled = QRCodeScaningEnabled;
    }

    public boolean isRingScaningEnabled() {
        return RingScaningEnabled;
    }

    public void setRingScaningEnabled(boolean ringScaningEnabled) {
        RingScaningEnabled = ringScaningEnabled;
    }

}
