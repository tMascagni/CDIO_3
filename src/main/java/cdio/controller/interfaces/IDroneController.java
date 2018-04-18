package cdio.controller.interfaces;

import cdio.model.QRCodeData;
import cdio.ui.interfaces.MessageListener;
import de.yadrone.base.IARDrone;

import java.util.ArrayList;

public interface IDroneController {
    /** BASIC DRONE FUNCTIONALITY **/
    void startDrone() throws DroneControllerException;
    void initDrone() throws DroneControllerException;
    void stopDrone() throws DroneControllerException;

    void takeOffDrone() throws DroneControllerException;
    void landDrone() throws DroneControllerException;
    void hoverDrone(int timeMillis) throws DroneControllerException;
    void hoverDrone() throws DroneControllerException;

    void doSearchRotation() throws DroneControllerException;
    void circleAroundObject() throws DroneControllerException;

    void flyForward(int distanceMilli) throws DroneControllerException;
    void flyBackward(int distanceMilli) throws DroneControllerException;
    void flyUp(int distanceMilli) throws DroneControllerException;
    void flyDown(int distanceMilli) throws DroneControllerException;
    void flyLeft(int distanceMilli) throws DroneControllerException;
    void flyRight(int distanceMilli) throws DroneControllerException;

    void setSpeed(int speed) throws DroneControllerException;
    int getSpeed() throws DroneControllerException;
    void resetDrone() throws DroneControllerException;

    void setMessageListener(MessageListener messageListener);

    IARDrone getDrone() throws DroneControllerException;

    float getPitch();
    float getRoll();
    float getYaw();
    float getCorrectedYaw();
    float getAltitude();
    int getBattery();

    public void addMSG(String s);
    public ArrayList<String> getNewMSG();

    public void flyDroneTest(double dist);

    QRCodeData getQrData();

    class DroneControllerException extends Exception {

        public DroneControllerException(String msg) {
            super(msg);
        }

    }

}