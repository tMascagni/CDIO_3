package cdio.controller.interfaces;

import de.yadrone.base.IARDrone;

public interface IDroneController {
    void startDrone() throws DroneControllerException;
    void initDrone() throws DroneControllerException;
    void stopDrone() throws DroneControllerException;

    void takeOffDrone() throws DroneControllerException;
    void landDrone() throws DroneControllerException;

    void hoverDrone(int timeMillis) throws DroneControllerException;

    void searchRotation() throws DroneControllerException;
    void circleAroundObject() throws DroneControllerException;

    void flyForward(int distanceMilli) throws DroneControllerException;
    void flyBackward(int distanceMilli) throws DroneControllerException;

    void flyUp(int distanceMilli) throws DroneControllerException;
    void flyRight(int distanceMilli) throws DroneControllerException;
    void flyLeft(int distanceMilli) throws DroneControllerException;
    void flyDown(int distanceMilli) throws DroneControllerException;

    void resetDrone() throws DroneControllerException;
    IARDrone getDrone() throws DroneControllerException;

    class DroneControllerException extends Exception {

        public DroneControllerException(String msg) {
            super(msg);
        }

    }

}