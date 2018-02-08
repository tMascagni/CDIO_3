package main;

import com.codeminders.ardrone.*;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class Server implements DroneStatusChangeListener, NavDataListener{

    private static final long CONNECT_TIMEOUT = 10000L;

    ARDrone drone;
    NavData data;
    Boolean readyStatus = false;
    ARDrone.VideoChannel camH;
    ARDrone.VideoChannel camV;

    private final VideoPanel video = new VideoPanel();

    private static Server singleton = new Server();

    private Server() {
    }

    /* Static 'instance' method */
    public static Server getInstance( ) {
        return singleton;
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Server.singleton.initDrone();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initDrone() throws IOException {
        System.out.println("Connecting to drone");
        drone = new ARDrone();
        data = new NavData();
        drone.playLED(10,10,10);

        drone.connect();
        drone.clearEmergencySignal();
        // Wait until drone is ready
        drone.waitForReady(CONNECT_TIMEOUT);
        System.err.println("Drone State: " + data.getControlState());

        drone.trim(); // calibrate ??

        System.err.println("connected to Drone! \nbattery level: " + data.getBattery());

        drone.addStatusChangeListener(this);
        drone.addNavDataListener(this);

        drone.enableAutomaticVideoBitrate();
        drone.selectVideoChannel(ARDrone.VideoChannel.HORIZONTAL_IN_VERTICAL);
    }

    public void ready() {
        readyStatus = true;
        try {
            drone.takeOff();
            drone.hover();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navDataReceived(NavData navData) {
        data = navData;
    }

    public void land() throws IOException, InterruptedException {
        // Land the drone
        drone.land();
        // Give it some time to land
        Thread.sleep(2000);
    }

    public void hoverAndWait(int millisecToWait) throws IOException, InterruptedException {
        // hover the drone
        drone.hover();
        // time off hovering
        Thread.sleep(millisecToWait);
    }



    public ARDrone getDrone() {
        return drone;
    }

    public NavData getData() {
        return data;
    }

}