package cdio.controller.interfaces;

import cdio.model.QRImg;
import yadankdrone.IARDrone;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public interface IDroneCommander {
    /***************************
     * BASIC DRONE FUNCTIONALITY
     ***************************/
    void startDrone() throws DroneCommanderException;
    void stopDrone() throws DroneCommanderException;
    void initDrone() throws DroneCommanderException;
    void resetDrone() throws DroneCommanderException;
    void smoothInit() throws DroneCommanderException;
    void takeOffDrone() throws DroneCommanderException;
    void landDrone() throws DroneCommanderException;
    void hoverDrone(int timeMillis) throws DroneCommanderException;
    void hoverDrone() throws DroneCommanderException;

    void flyForward(int distanceMilli) throws DroneCommanderException;
    void flyBackward(int distanceMilli) throws DroneCommanderException;
    void flyUp(int distanceMilli) throws DroneCommanderException;
    void flyDown(int distanceMilli) throws DroneCommanderException;
    void flyLeft(int distanceMilli) throws DroneCommanderException;
    void flyRight(int distanceMilli) throws DroneCommanderException;
    void flyUpToAltitude(int altitude);

    void rejeHop();
    void flyDownToAltitude(float altitude);
    void circleAroundObjectV2();
    void pointToQRSpin(int range);
    void spinToQR();
    void lockOn(int maxRange, int minRange, int step);
    void flyUpAltitudePlus(int altitudePlus);

    void flyToAltitude(int altitude);

    QRImg searchForQRCode() throws DroneCommanderException;

    QRImg searchForQRCodeDetect(boolean reverseDirection) throws DroneCommanderException;
    void circleAroundObject() throws DroneCommanderException;
    boolean leftSideCheck() throws DroneCommanderException;
    boolean rightSideCheck() throws DroneCommanderException;
    void rotateDrone(int targetYaw) throws DroneCommanderException;

    void adjustToCenterFromQR(int range) throws DroneCommanderException;
    void adjustToCenterFromQRRotate() throws DroneCommanderException;

    void adjustHeightToCenterFromQR() throws DroneCommanderException;

    void centerOnRing() throws DroneCommanderException;

    boolean flyToTargetQRCode(boolean centerOnTheWay, int target, int range) throws DroneCommanderException;

    /************************
     * MESSAGES
     ***********************/
    void addMessage(String msg);
    List<String> getNewMessages();

    /************************
     * GETTERS & SETTERS
     ***********************/
    void setSpeed(int speed);
    int getSpeed();
    IARDrone getDrone() throws DroneCommanderException;
    boolean isQRCodeScanningEnabled();
    void setQRCodeScanningEnabled(boolean isQRCodeScanningEnabled);
    boolean isRingScanningEnabled();
    void setRingScanningEnabled(boolean isRingScanningEnabled);
    boolean isVideoConnected();
    boolean isNavManagerConnected();

    float getPitch();
    float getRoll();
    float getYaw();
    float getCorrectYaw(float yaw);
    int getCorrectTargetYaw(int targetYaw);
    float getAltitude();
    int getMinAltitude();
    int getMaxAltitude();
    int getBattery();
    long getWiFiStrength();
    BufferedImage getLatestReceivedImage();

    /*******************
     * QR CODE MAPPING
     ********************/
    void updateQRMap(int qrNumber, QRImg qrImg) throws DroneCommanderException;
    QRImg getTallestQRCode() throws DroneCommanderException;
    int getTargetQRCode();
    Map<Integer, QRImg> getQRMap();
    boolean isQRCodeTarget(int target);
    void incQRCodeTarget();

    void sleep(int timeMillis);

    class DroneCommanderException extends Exception {

        public DroneCommanderException(String msg) {
            super(msg);
        }

    }

}