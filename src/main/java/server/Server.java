package server;

import com.codeminders.ardrone.ARDrone;
import com.codeminders.ardrone.DroneStatusChangeListener;
import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.NavDataListener;
import video.VideoPanel;

import java.io.IOException;

public final class Server implements DroneStatusChangeListener, NavDataListener {

    private static final long CONNECT_TIMEOUT = 10000L;

    private ARDrone drone;
    private NavData navData;
    private Boolean isReady = false;
    private ARDrone.VideoChannel camH;
    private ARDrone.VideoChannel camV;

    private final VideoPanel videoPanel = new VideoPanel();

    private static Server instance;

    static {
        try {
            instance = new Server();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate Server!");
        }
    }

    private Server() {

    }

    public synchronized static Server getInstance() {
        return instance;
    }

    public void initDrone() throws IOException {
        System.out.println("Connecting to drone...");
        drone = new ARDrone();
        navData = new NavData();
        drone.playLED(10, 10, 10);

        // TODO: Should .connect not be one of the first things that is called?
        drone.connect();
        drone.clearEmergencySignal();

        /* Wait until drone is ready */
        drone.waitForReady(CONNECT_TIMEOUT);
        System.err.println("Drone State: " + navData.getControlState());

        drone.trim(); // calibrate ??

        System.err.println("Successfully connected to drone.");
        System.out.println("DATA: Drone Battery Level: " + navData.getBattery());

        drone.addStatusChangeListener(this);
        drone.addNavDataListener(this);

        drone.enableAutomaticVideoBitrate();
        drone.selectVideoChannel(ARDrone.VideoChannel.HORIZONTAL_IN_VERTICAL);
    }

    public void ready() {
        isReady = true;
        try {
            drone.takeOff();
            drone.hover();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navDataReceived(NavData navData) {
        this.navData = navData;
        System.out.println("##### Drone Data ######");
        System.out.println("Battery: " + navData.getBattery());
        System.out.println("Control State: " + navData.getControlState());
        System.out.println("Altitude: " + navData.getAltitude());
        System.out.println("Control Algorithm: " + navData.getControlAlgorithm());
        System.out.println("Flying State: " + navData.getFlyingState());
        System.out.println("Mode: " + navData.getMode());
        System.out.println("Pitch: " + navData.getPitch());
        System.out.println("Roll: " + navData.getRoll());
        System.out.println("Vx: " + navData.getVx());
        System.out.println("Vy: " + navData.getVz());
        System.out.println("Yaw: " + navData.getYaw());

        System.out.println("isAcquisitionThreadOn: " + navData.isAcquisitionThreadOn());
        System.out.println("isADCWatchDogDelayed: " + navData.isADCWatchdogDelayed());
        System.out.println("isAltitudeControlActive: " + navData.isAltitudeControlActive());
        System.out.println("isAngelsOutOufRange: " + navData.isAngelsOutOufRange());
        System.out.println("isTrimSucceeded: " + navData.isTrimSucceeded());
        System.out.println("isVideoEnabled: " + navData.isVideoEnabled());
        System.out.println("isMotorsDown: " + navData.isMotorsDown());
        System.out.println("isBatteryTooLow: " + navData.isBatteryTooLow());
        System.out.println("isCommunicationProblemOccurred: " + navData.isCommunicationProblemOccurred());
        System.out.println("isEmergency: " + navData.isEmergency());
        System.out.println("isGyrometersDown(): " + navData.isGyrometersDown());

        System.out.println("Sequence: " + navData.getSequence());
        System.out.println("VisionTags: " + navData.getVisionTags());
        System.out.println("######################");
    }

    public void land() throws IOException, InterruptedException {
        /* Land the drone */
        drone.land();
        /* Give it some time to land */
        Thread.sleep(2000);
    }

    public void hoverAndWait(int millisecToWait) throws IOException, InterruptedException {
        /* hover the drone */
        drone.hover();

        /* Time off hovering */
        /*
         * TODO: Is it really safe to use Thread.sleep()? Maybe we should reevaluate this.
         */
        Thread.sleep(millisecToWait);
    }

    public ARDrone getDrone() {
        return drone;
    }

    public NavData getNavData() {
        return navData;
    }

}