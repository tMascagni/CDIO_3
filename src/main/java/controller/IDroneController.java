package controller;

import de.yadrone.base.IARDrone;

public interface IDroneController {
    void startDrone() throws DroneControllerException;
    void initDrone() throws DroneControllerException;
    void stopDrone() throws DroneControllerException;

    void takeOffDrone() throws DroneControllerException;
    void landDrone() throws DroneControllerException;

    void hoverDrone(int timeMillis) throws DroneControllerException;

    void resetDrone() throws DroneControllerException;
    IARDrone getDrone() throws DroneControllerException;
}