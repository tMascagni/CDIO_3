package cdio.drone.interfaces;

import cdio.cv.QRImg;
import cdio.model.QRCodeData;
import yadankdrone.IARDrone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IDroneCommander {
    /** BASIC DRONE FUNCTIONALITY **/
    void startDrone() throws DroneCommanderException;
    void initDrone() throws DroneCommanderException;
    void stopDrone() throws DroneCommanderException;

    void takeOffDrone() throws DroneCommanderException;
    void landDrone() throws DroneCommanderException;
    void hoverDrone(int timeMillis) throws DroneCommanderException;
    void hoverDrone() throws DroneCommanderException;

    QRCodeData searchForQRCode() throws DroneCommanderException;
    void rotateDrone(int targetYaw) throws DroneCommanderException;
    void circleAroundObject() throws DroneCommanderException;

    void flyForward(int distanceMilli) throws DroneCommanderException;
    void flyBackward(int distanceMilli) throws DroneCommanderException;
    void flyUp(int distanceMilli) throws DroneCommanderException;
    void flyDown(int distanceMilli) throws DroneCommanderException;
    void flyLeft(int distanceMilli) throws DroneCommanderException;
    void flyRight(int distanceMilli) throws DroneCommanderException;

    void setSpeed(int speed) throws DroneCommanderException;
    int getSpeed() throws DroneCommanderException;
    void resetDrone() throws DroneCommanderException;

    void addMessage(String msg);
    List<String> getNewMessages();

    void flyDroneTest(double distance);

    IARDrone getDrone() throws DroneCommanderException;

    /* QR Code Mapping */
    void updateQrCodeMapData(int mapNumber, QRCodeData qrCodeData) throws DroneCommanderException;
    QRCodeData getQrCodeWithGreatestHeight() throws DroneCommanderException;
    int getTargetQrCode();
    Map<Integer, QRCodeData> getQrCodeMap();
    boolean isQrCodeTarget(int possibleTarget);
    void incQrCodeTarget();

    /* OpenCV */
    ArrayList<QRImg> getQrImgs();

    /* Getters and setters */
    boolean isQRCodeScanningEnabled();
    void setQRCodeScanningEnabled(boolean isQRCodeScanningEnabled);
    boolean isRingScanningEnabled();
    void setRingScanningEnabled(boolean isRingScanningEnabled);
    float getPitch();
    float getRoll();
    float getYaw();
    float getCorrectYaw(float yaw);
    int getCorrectTargetYaw(int targetYaw);
    float getAltitude();
    int getMinAltitude();
    int getMaxAltitude();
    int getBattery();

    class DroneCommanderException extends Exception {

        public DroneCommanderException(String msg) {
            super(msg);
        }

    }

}