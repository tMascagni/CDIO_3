package cdio.main;

import cdio.controller.DroneController;
import cdio.controller.interfaces.IDroneController;
import cdio.ui.interfaces.MessageListener;

import java.awt.*;

public final class Main {

    private final static IDroneController droneController = DroneController.getInstance();

    public static void main(String[] args) {

        droneController.setMessageListener(new MessageListener() {
            @Override
            public void messageCommandStartEventOccurred(String title) {

            }

            @Override
            public void messageCommandEventOccurred(Object obj, String msg) {

            }

            @Override
            public void messageCommandEndEventOccurred() {

            }

            @Override
            public void setBattery(int battery) {

            }

            @Override
            public void setSpeed(int speed) {

            }

            @Override
            public void setPitch(int pitch) {

            }

            @Override
            public void setRoll(int roll) {

            }

            @Override
            public void setYaw(int yaw) {

            }

            @Override
            public void setAltitude(int altitude) {

            }
        });

        /* ######### TEST ######### */
        EventQueue.invokeLater(() -> {
            try {
                droneController.startDrone();
                droneController.initDrone();

                droneController.takeOffDrone();

                droneController.hoverDrone(1000);

                droneController.flyForward(200);


                droneController.landDrone();
                droneController.stopDrone();
            } catch (IDroneController.DroneControllerException e) {
                e.printStackTrace();
            }
        });
        /* ######### TEST ######### */

    }

}