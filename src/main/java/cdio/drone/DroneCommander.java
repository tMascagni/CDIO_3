package cdio.drone;

import cdio.cv.CVHelper;
import cdio.cv.QRDetector;
import cdio.cv.QRImg;
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
    private Map<Integer, QRImg> qrCodeMap = new HashMap<>();

    private ArrayList<QRImg> qrImgs = new ArrayList<>();

    private static IDroneCommander instance;

    private boolean isQRCodeScanningEnabled = true;
    private boolean isRingScanningEnabled = false;

    private QRDetector qrDetector = new QRDetector();
    private CVHelper cvHelper = new CVHelper();

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
    public final void startDrone() {
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
    public final void initDrone() {
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
    public final void stopDrone() {
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
    public final void takeOffDrone() {
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
    public final void landDrone() {
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
    public final void hoverDrone(int timeMillis) {
        addMessage("Drone hovering for " + timeMillis + " ms...");

        commandManager.hover().waitFor(timeMillis);

        addMessage("Drone finished hovering!");
    }

    /**
     * Method to make the drone hover.
     */
    @Override
    public void hoverDrone() {
        addMessage("Drone hovering...");

        commandManager.hover();

        addMessage("Drone finished hovering!");
    }


    /**
     * Method to make the drone rotate to a target yaw.
     */
    @Override
    public final QRImg searchForQRCode() throws IQRCodeHandler.QRCodeHandlerException {
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

                    QRImg qrImg = qrCodeHandler.scanImageForBest(latestReceivedImage, this);

                    if (qrImg != null && qrImg.getQrCodeData() != null) {

                        int qrCodeResult = qrImg.getQrCodeData().getResult();

                        // QR CODE TARGET FOUND!
                        if (isQrCodeTarget(qrCodeResult)) {
                            updateQrCodeMapData(qrCodeResult, qrImg);
                            incQrCodeTarget(); // TODO: Do this after the ring has been passed.
                            addMessage("Found correct QR code: " + qrCodeResult + ", Yaw: " + yaw + ", New QR Code target: " + targetQrCode);
                            return qrImg;
                            // TODO: Fly to the code.
                            // clear the map after the drone has flown through the ring.
                            // qrCodeMap.clear();
                        } else {
                            // FOUND QR CODE, BUT NOT TARGET!
                            updateQrCodeMapData(qrCodeResult, qrImg);
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

        return null;
    }

    @Override
    public final void circleAroundObject() {

    }

    /**
     * Method to make the drone fly forwards.
     */
    @Override
    public final void flyForward(int timeMillis) {
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
    public final void flyBackward(int timeMillis) {
        addMessage("Drone flying backward for " + timeMillis + " ms...");

        commandManager.backward(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        addMessage("Drone done flying backward!");
    }

    /**
     * Method to make the drone fly upwards.
     */
    @Override
    public final void flyUp(int timeMillis) {
        addMessage("Drone flying upwards for " + timeMillis + " ms...");

        commandManager.up(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        addMessage("Drone done flying upwards!");
    }

    /**
     * Method to make the drone fly downwards.
     */
    @Override
    public final void flyDown(int timeMillis) {
        addMessage("Drone flying downwards for " + timeMillis + " ms...");

        commandManager.down(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        addMessage("Drone done flying downwards!");
    }

    /**
     * Method to make the drone fly left.
     */
    @Override
    public final void flyLeft(int timeMillis) {
        addMessage("Drone flying left for " + timeMillis + " ms...");

        commandManager.goLeft(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        addMessage("Drone done flying left!");
    }

    /**
     * Method to make the drone fly right.
     */
    @Override
    public final void flyRight(int timeMillis) {
        addMessage("Drone flying right for " + timeMillis + " ms...");

        commandManager.goRight(INITIAL_SPEED).doFor(timeMillis);
        sleep(100);

        addMessage("Drone done flying right!");
    }

    public void adjustToCenterFromQR() throws IQRCodeHandler.QRCodeHandlerException {
        int centerOfFrameX = latestReceivedImage.getWidth() / 2;
        QRImg qrImg = null;

        do {

            do {
                qrImg = qrCodeHandler.scanImageForBest(latestReceivedImage, this);
            } while (qrImg == null);


            if (qrImg.getPosition().x > centerOfFrameX) {
                flyRight(200);
            } else {
                flyLeft(200);
            }

            hoverDrone(100);
            sleep(500);

        } while (qrImg.getPosition().x <= centerOfFrameX - 50 || qrImg.getPosition().x >= centerOfFrameX + 50);
    }

    @Override
    public int getSpeed() {
        return drone.getSpeed();
    }

    @Override
    public void setSpeed(int speed) {

        if (speed > MAX_SPEED || speed < MIN_SPEED) {
            addMessage("Attempted to set illegal drone speed: " + speed + "!");
            return;
        }

        drone.setSpeed(speed);

        addMessage("Sat drone speed: " + speed + "!");
    }

    /**
     * Method to reset the drone.
     */
    @Override
    public final void resetDrone() {
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

    private void scanImageForQRCode(BufferedImage bufferedImage) throws IQRCodeHandler.QRCodeHandlerException {
        ArrayList<QRImg> qrCodes = qrDetector.processAll(cvHelper.buf2mat(bufferedImage));


        QRImg qrImg = null;

        qrImg = qrCodeHandler.scanImageForBest(bufferedImage, this);

        if (qrImg != null) {

                addMessage(qrImg.toString());
                System.out.println(qrCodes.toString());

            } else {
                addMessage("qrImg is null!");
                System.out.println("qrImg is null!");
            }


        this.qrImgs = qrCodes;
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

                /*
                if (isQRCodeScanningEnabled) {
                    qrScanTimer--;
                    if (qrScanTimer == 0) {
                        qrScanTimer = INITIAL_QR_SCAN_TIMER;
                        scanImageForQRCode(bufferedImage);
                    }
                } else if (isRingScanningEnabled) {
                    // søg efter ringe
                }
                */


                /*
                try {
                    QRImg qrImg = qrCodeHandler.scanImageForBest(latestReceivedImage, DroneCommander.this);

                    if (qrImg != null && qrImg.getQrCodeData() != null) {

                        int qrCodeResult = qrImg.getQrCodeData().getResult();

                        // QR CODE TARGET FOUND!
                        if (isQrCodeTarget(qrCodeResult)) {
                            updateQrCodeMapData(qrCodeResult, qrImg);
                            incQrCodeTarget(); // TODO: Do this after the ring has been passed.
                            addMessage("Found correct QR code: " + qrCodeResult);
                            addMessage("New Target QR: " + targetQrCode);
                            addMessage(qrCodeMap.toString());
                            QRImg heightQR = getQrCodeWithGreatestHeight();
                            addMessage("Greatest height code: " + heightQR.getQrCodeData().getResult() + ", Height: " + heightQR.getH());
                            //return qrImg;
                            // TODO: Fly to the code.
                            // clear the map after the drone has flown through the ring.
                            // qrCodeMap.clear();
                        } else {
                            // FOUND QR CODE, BUT NOT TARGET!
                            updateQrCodeMapData(qrCodeResult, qrImg);
                            addMessage("Found incorrect QR code: " + qrCodeResult + ", Target: " + targetQrCode);
                            addMessage(qrCodeMap.toString());
                            QRImg heightQR = getQrCodeWithGreatestHeight();
                            addMessage("Greatest height code: " + heightQR.getQrCodeData().getResult() + ", Height: " + heightQR.getH());
                            //continue;
                        }
                    }

                } catch (IQRCodeHandler.QRCodeHandlerException ignored) {
                    // no qr detected which is fine.
                } catch (DroneCommanderException e) {
                    e.printStackTrace();
                }
                */


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


    private void startConLisner() {
        navDataManager.addWifiListener(new WifiListener() {
            @Override
            public void received(long l) {
                System.out.println("wifi Lis : " + l);
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
        List<String> msgs = new ArrayList<>(messageList);
        messageList.clear();
        return msgs;
    }

    @Override
    public void updateQrCodeMapData(int mapNumber, QRImg qrImg) {
        /*
         * If the qrCodeMap already contains QR Code data at the given mapNumber,
         * do not update it, since we don't want to overwrite it.
         *
         * Only overwrite it, if the angle of the new QRImg is smaller than the
         * previous QRImg, since that means we have a new, more centered, image of the
         * same QRCode.
         */
        if (qrCodeMap.get(mapNumber) != null) {
            // If the angle of the new image is smaller than the
            // previous image angle, then update it, since it is a better picture.
            if (qrImg.getAngle() < qrCodeMap.get(mapNumber).getAngle()) {
                qrCodeMap.put(mapNumber, qrImg);
            }
        }

        /*
         * If the map does not contain it already, then put it into the map.
         */
        qrCodeMap.putIfAbsent(mapNumber, qrImg);
    }

    @Override
    public void rotateDrone(int targetYaw) {
        /*
         * First get the correct target yaw.
         */
        targetYaw = getCorrectTargetYaw(targetYaw);
        addMessage("Rotating to targetYaw: " + targetYaw);


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

    }

    /**************************
     * QR Code Mapping Methods
     **************************/
    @Override
    public QRImg getQrCodeWithGreatestHeight() {
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

    @Override
    public int getTargetQrCode() {
        return targetQrCode;
    }

    @Override
    public Map<Integer, QRImg> getQrCodeMap() {
        return qrCodeMap;
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

    /**************************
     * OpenCV
     **************************/
    @Override
    public ArrayList<QRImg> getQrImgs() {
        return qrImgs;
    }

    /**************************
     * Getters and Setters
     **************************/
    @Override
    public boolean isQRCodeScanningEnabled() {
        return isQRCodeScanningEnabled;
    }

    @Override
    public void setQRCodeScanningEnabled(boolean isQRCodeScanningEnabled) {
        this.isQRCodeScanningEnabled = isQRCodeScanningEnabled;
    }

    @Override
    public boolean isRingScanningEnabled() {
        return isRingScanningEnabled;
    }

    @Override
    public void setRingScanningEnabled(boolean isRingScanningEnabled) {
        this.isRingScanningEnabled = isRingScanningEnabled;
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
        if (yaw >= 180)
            yaw = yaw - 360;
        else if (yaw <= -180)
            yaw = 360 - yaw;

        return yaw;
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
    public int getMaxAltitude() {
        return MAX_ALTITUDE;
    }

    @Override
    public int getMinAltitude() {
        return MIN_ALTITUDE;
    }

    @Override
    public int getBattery() {
        return battery;
    }

    public BufferedImage getLatestReceivedImage() {
        return latestReceivedImage;
    }
}