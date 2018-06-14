package cdio.main;

import cdio.cv.QRDetector;
import cdio.drone.DroneCommander;
import cdio.drone.interfaces.IDroneCommander;
import cdio.handler.QRCodeHandler;
import cdio.handler.interfaces.IQRCodeHandler;
import cdio.ui.MainFrame;
import org.opencv.core.Core;

import javax.swing.*;

public class RingTest {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private final static IDroneCommander droneCommander = DroneCommander.getInstance();

    private static QRDetector qrDetector = new QRDetector();

    public static void main(String[] args) {

        IQRCodeHandler qrCodeHandler = QRCodeHandler.getInstance();
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(droneCommander);
        });

        try {
            droneCommander.startDrone();
            droneCommander.initDrone();
            droneCommander.centerOnRing();

            while (true) {
                System.out.println(droneCommander.getAltitude());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
